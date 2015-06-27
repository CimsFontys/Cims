/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import CommunicationClient.ComManager;
import CommunicationClient.LogManager;
import chat.AudioMessage;
import chat.ChatMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ObservableList;
import GUI.ClienttestGUIController;
import Protocol.Message;
import Protocol.MessageBuilder;
import chat.EmergencyUnit;
import java.util.Random;

/**
 * Class that connects with the server when initialized and sends the messages to the server.
 * @author Leo
 */
public class ChatClient 
{
    private ObjectOutputStream out;
    private Thread t;
    private EmergencyUnit unit;
    private ArrayList<String> clients;
    private ObservableList<String> observableClients;
    private ArrayList<ChatMessage> messages;
    private ObservableList<ChatMessage> observableMessages;
    private String reciever;
    private Random random;
    private MessageBuilder mb;
    
    private ComManager comManager = ComManager.getInstance();
    private LogManager logManager = LogManager.getInstance();
    
    public ChatClient(String user, int type, int persontype)
    {
        OutputStream outStream = null;
        try 
        {
            mb = new MessageBuilder();
            clients = new ArrayList<String>();
            random = new Random();
            observableClients = observableList(clients);
            messages = new ArrayList<ChatMessage>();
            observableMessages = observableList(messages);
            Socket s = new Socket("145.93.33.200", 8189); // connect with the server.
            outStream = s.getOutputStream(); // retrives the input and output stream so that these can be used for communication.
            InputStream inStream = s.getInputStream();
            out = new ObjectOutputStream(outStream);
            ObjectInputStream in = new ObjectInputStream(inStream);
            this.unit = new EmergencyUnit(user, type, persontype); 
            unit.setLatidude(generateRandomLatidude());
            unit.setLongitude(generateRandomLongitude());
            out.writeObject(unit); // sends the logged in emergencyunit to the server so that is logged on at the server.
            ClientThread ct = new ClientThread(in,this); // creates a new thread where messages can be recieved and processed.
            t = new Thread(ct);
            t.start();
            File file = new File("Opnames");
            if(!file.exists())
            {
                file.mkdir();
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    /**
     * 
     * @return the current reciever for the messages.
     */
    public String getReciever() {
        return reciever;
    }
    /**
     * 
     * @return a random latidude for the location of the logged in emergency unit.
     */
    private double generateRandomLatidude()
    {
        return 4.0383366 + (6.0383366 - 4.0383366) * random.nextDouble();
    }
    /**
     * 
     * @return a random longtidude for the location of the logged in emergency unit.
     */
    private double generateRandomLongitude()
    {
        return 51.5467726 + (52.400000 - 51.5467726) * random.nextDouble();
    }

    public ObservableList<ChatMessage> getObservableMessages() 
    {
        return observableMessages;
    }
    /**
     * sets the current reciever.
     * @param reciever the new reciever for the chatmessages that this client sends.
     */
    public void setReciever(String reciever) 
    {
        this.reciever = reciever;
    }
    
    public void sendMessage(String bericht)
    {
        try 
        {
            Message chat = mb.buildInsertMessage(logManager.getPersonId(), 8, "test", null);
            comManager.addMessage(chat);
            
            ChatMessage message = new ChatMessage(bericht, this.unit.getNaam(), reciever);
            out.writeObject(message);
            out.flush();
            addMessage(message);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * sends a new audiomessage to the current reciever,
     * @param audiofile the audio that needs to be sent.
     * @param path to the audio file so that it can be played.
     */
    public void sendAudioMessage(byte[] audiofile, String path)
    {
        try
        {
            AudioMessage audiomessage = new AudioMessage("Audiobericht ontvangen van: " + this.unit.getNaam(), this.unit.getNaam(), reciever, audiofile);
            audiomessage.setAudiopath(path);
            out.writeObject(audiomessage);
            out.flush();
            addMessage(audiomessage);
            
        }
        catch(IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }                
    }
    /**
     * Adds a new message to the observable list so that it is automaticly added to the gui.
     * @param message the messages that needs to be added.
     */
    public void addMessage(ChatMessage message)
    {
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                observableMessages.add(message);
            }
        });
        
    }
    /**
     * adds a client to the list of clients that is online. 
     * If the client is already in the list it is removed from the list because 
     * that whould mean the client went ofline.
     * @param name the name of the client that goes online or ofline.
     */
    public void addClient(String name)
    {
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                if(clients.contains(name))
                {
                    observableClients.remove(name);
                }
                else
                {
                    observableClients.add(name);
                }                
            }
        });
    }
    /**
     * 
     * @return the observablelist with the clients.
     */
    public ObservableList getObservableClients()
    {
        return this.observableClients;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

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
import chat.EmergencyUnit;
import java.util.Random;

/**
 *
 * @author pieter
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
    private String ontvanger;
    private Random random;
    
    public ChatClient(String user, int type, int persontype)
    {
        OutputStream outStream = null;
        try 
        {
            clients = new ArrayList<String>();
            random = new Random();
            observableClients = observableList(clients);
            messages = new ArrayList<ChatMessage>();
            observableMessages = observableList(messages);
            Socket s = new Socket("localhost", 8189);
            outStream = s.getOutputStream();
            InputStream inStream = s.getInputStream();
//          Let op: volgorde is van belang!
            out = new ObjectOutputStream(outStream);
            ObjectInputStream in = new ObjectInputStream(inStream);
            this.unit = new EmergencyUnit(user, type, persontype);
            unit.setLatidude(generateRandomLatidude());
            unit.setLongitude(generateRandomLongitude());
            out.writeObject(unit);
            ClientThread ct = new ClientThread(in,this);
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
    
    private double generateRandomLatidude()
    {
        return 4.0383366 + (6.0383366 - 4.0383366) * random.nextDouble();
    }
    
    private double generateRandomLongitude()
    {
        return 51.5467726 + (52.400000 - 51.5467726) * random.nextDouble();
    }

    public ObservableList<ChatMessage> getObservableMessages() 
    {
        return observableMessages;
    }
    
    public void setOntvanger(String ontvanger) 
    {
        this.ontvanger = ontvanger;
    }
    
    public void sendMessage(String bericht)
    {
        try 
        {
            ChatMessage message = new ChatMessage(bericht, this.unit.getNaam(), ontvanger);
            out.writeObject(message);
            out.flush();
            addMessage(message);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendAudioMessage(byte[] audiofile, String path)
    {
        try
        {
            AudioMessage audiomessage = new AudioMessage("Audiobericht ontvangen van: " + this.unit.getNaam(), this.unit.getNaam(), ontvanger, audiofile);
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
    
    public ObservableList getObservableClients()
    {
        return this.observableClients;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.chatserver;

import chat.AudioMessage;
import chat.EmergencyUnit;
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
/**
 *
 * @author Leo
 */
public class Client implements Runnable
{
    private ObjectInputStream  in;
    private ObjectOutputStream out;
    private EmergencyUnit unit;
    private Server server;
    private ArrayList<ChatMessage> messages;
    private ObservableList<ChatMessage> observableMessages;
    
    public Client(Socket incoming, Server server) throws IOException, ClassNotFoundException
    {
        OutputStream outStream = incoming.getOutputStream();
        InputStream inStream = incoming.getInputStream();
        
        in = new ObjectInputStream(inStream);
        out = new ObjectOutputStream(outStream);
        this.unit = (EmergencyUnit) in.readObject();
        System.out.println("Naam: " + unit.getNaam() + " Longitude: " + unit.getLongitude() + " Latidude: " + unit.getLatidude());
        File dir = new File("Opnames\\" + unit.getNaam());
        if(!dir.exists())
        {
            dir.mkdir();
        }
        System.out.println(unit.getNaam());
        this.server = server;
        messages = new ArrayList<ChatMessage>();
        observableMessages = observableList(messages);
    }
    /**
     * sends a message to this client.
     * @param message that needs to be send
     */
    public void sendMessage(ChatMessage message)
    {
        try 
        {
            out.writeObject(message);
            out.flush();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    /**
     * sends a list with the other clientnames to this client.
     * @param clientnames the list that needs to be send to this client.
     */
    public void sendClients(ArrayList<String> clientnames)
    {
        try 
        {
            out.writeObject(clientnames);
            out.flush();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * sends a new clientname to this client because a new client came online.
     * @param name from the client that has come online.
     */
    public void sendClientName(String name)
    {
        try 
        {
            out.writeObject(name);
            out.flush();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    /**
     * adds the message to a obeserble list so that it can be shown in the gui.
     * @param message the message that is added.
     */
    public void addMessageToObservable(ChatMessage message)
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
     * 
     * @return the name of the connected client.
     */
    public String getNaam()
    {
        return unit.getNaam();
    }
    /**
     * 
     * @return the observable list with chatmessages so that it can be linked
     * to the gui.
     */
    public ObservableList<ChatMessage> getMessages()
    {
        return this.observableMessages;
    }            
    /**
     * method to recieve chatmessages from this connected client. If the method
     * is for the emergency room the message is added to the observable list otherwise 
     * the object is send to the destined client.
     */
    @Override
    public void run() 
    {
        while(!Thread.currentThread().isInterrupted())
        {
            try 
            {
                ChatMessage message = (ChatMessage) in.readObject();
                System.out.println("bericht ontvangen");
                if(message.getOntvanger().equals("Meldkamer"))
                {
                    if(message instanceof AudioMessage)
                    {
                        AudioMessage audiomessage = (AudioMessage) message;
                        Thread t = new Thread(new WriteFileThread(audiomessage, this));
                        t.start();
                    }
                    else
                    {
                        addMessageToObservable(message);
                    }
                }
                else
                {
                    server.sendMessage(message);
                }
            } 
            catch (IOException | ClassNotFoundException ex) 
            {
                //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                Thread.currentThread().interrupt();
                System.out.println( unit.getNaam() + " is gone");
                server.removeClient(unit.getNaam());
            }
        }
    }
}
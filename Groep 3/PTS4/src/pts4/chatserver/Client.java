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
    
    public String getNaam()
    {
        return unit.getNaam();
    }
    
    public ObservableList<ChatMessage> getMessages()
    {
        return this.observableMessages;
    }

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
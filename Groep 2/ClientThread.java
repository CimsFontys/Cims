/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientApp;

import chat.AudioMessage;
import chat.ChatMessage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Leo
 */
public class ClientThread implements Runnable 
{
    private ObjectInputStream stream;
    private ChatClient cc;
    private SimpleDateFormat sdfDate;
    private InputStream instream;
    
    public ClientThread(ObjectInputStream stream, ChatClient cc)
    {
        this.instream = instream;
        this.stream = stream;
        this.cc =cc;
        sdfDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss.SSS");;//dd/MM/yyyy
    }
    
    private String getSystemTimeAsString()
    {
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    @Override
    public void run() 
    {
        while(!Thread.currentThread().isInterrupted())
        {
            try 
            {
                Object object = stream.readObject();
                if(object instanceof ArrayList)
                {
                    for(String s : (ArrayList<String>) object)
                    {
                        cc.addClient(s);
                    }
                }
                if(object instanceof String)
                {
                    // voeg toe aan lijst met beschikbare clients
                    System.out.println((String) object);
                    cc.addClient((String) object);
                }
                if(object instanceof ChatMessage)
                {
                    ChatMessage message = (ChatMessage) object;
                    if(message instanceof AudioMessage)
                    {
                        AudioMessage audiomessage = (AudioMessage) message;
                        Thread t = new Thread(new WriteFileThread(audiomessage, cc));
                        t.start();
                    }
                    else
                    {
                        cc.addMessage(message);
                    }
                }                
            } 
            catch (IOException | ClassNotFoundException ex) 
            {
                Thread.currentThread().interrupt();
                System.out.println("Mattie de server is weg");
            }
        }
    }    
}
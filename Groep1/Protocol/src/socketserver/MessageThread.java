/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Protocol.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class MessageThread implements Runnable {

    private Thread thread = null;
    private boolean shouldStop;
    private static MessageThread INSTANCE;
    private ArrayList<Message> messages = null;
    
    private static ArrayList<MessageListener> messageListeners = null;

    private final String host = "localhost";
    private final int portNumber = 9000;
    private Socket socket1 = null;

    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    private MessageThread() {
        this.INSTANCE = this;
        shouldStop = false;
        messages = new ArrayList<Message>();
        messageListeners = new ArrayList<MessageListener>();

        try {
            socket1 = new Socket(host, portNumber);
            oos = new ObjectOutputStream(socket1.getOutputStream());
            ois = new ObjectInputStream(socket1.getInputStream());

        } catch (IOException e) {
        }

        thread = new Thread(this);

        
    }
    
    public void start()
    {
        if (!thread.isAlive()) {
            try {
                thread.start();
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }
    
    public void stop()
    {
        if(thread.isAlive())
        {
            try
            {
                this.shouldStop = true;
                System.out.println("Message Thread stopped, Communication Finished");
            }
            catch(Exception e)
            {
                
            }
        }
    }

    @Override
    public void run() 
    {
        /**
         * shouldStop equals false so messages can be send
         */
        while(!shouldStop)
        {
            Message message = null;
            
            if(messages.size() > 0)
            {
                try {
                    oos.writeObject(messages.get(0));
                    System.out.println("Message Send");
                } catch (IOException ex) {
                    Logger.getLogger(MessageThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                messages.remove(0);
            }
            try {
                if ((message = (Message) ois.readObject()) != null)
                {
                    for(MessageListener ml : messageListeners)
                    {
                        ml.proces(message);
                        System.out.println("Processing Message");
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MessageThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MessageThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    public static MessageThread getInstance(MessageListener listener) {
        if (INSTANCE == null) {
            new MessageThread();
        }
        
        messageListeners.add(listener);

        return INSTANCE;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
    
    public void removeListener(MessageListener listener)
    {
        messageListeners.remove(listener);
    }
}

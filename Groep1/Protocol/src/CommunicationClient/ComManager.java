/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CommunicationClient;

import Protocol.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merijn
 */
public class ComManager implements CommMessageListener{
    
    private Thread sendThread;
    private Thread recieveThread;
    
    private MessageRecieverThread mrt;
    private MessageSenderThread mst;
    
    private static final String host = "localhost";
    private static final int portNumber = 9000;
    private Socket socket;
    
    private List<MessageListener> listeners = null;
    
    private ComManager() {
        try {
            this.socket = new Socket(host,portNumber);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        
        this.mrt = new MessageRecieverThread(socket, this);
        this.mst = new MessageSenderThread(socket);
        
        this.sendThread = new Thread(mst);
        this.recieveThread = new Thread(mrt);
        this.sendThread.start();
        this.recieveThread.start();
        this.listeners = new ArrayList<>();
    }
    
    public static ComManager getInstance() {
        return ComManagerHolder.INSTANCE;
    }
    
    public void addListener(MessageListener ml)
    {
        this.listeners.add((ml));
    }
    
    public void sendMessage(Message message)
    {
        this.mst.addMessage(message);
    }
    
    public void stopService() 
    {
        try {
            this.mrt.stop();
            this.mst.stop();
            
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ComManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void recieved(Message message) {
        for (MessageListener ml : this.listeners) {
            ml.proces(message);
        }
    }
    
    private static class ComManagerHolder {

        private static final ComManager INSTANCE = new ComManager();
    }
}

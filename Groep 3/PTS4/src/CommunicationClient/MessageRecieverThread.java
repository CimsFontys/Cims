/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CommunicationClient;

import Protocol.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merijn
 */
public class MessageRecieverThread implements Runnable {
    
    private CommMessageListener listener;
    
    private ObjectInputStream ois;
    
    private boolean running = true;
    
    public MessageRecieverThread(ObjectInputStream ois, CommMessageListener listener) {
        this.listener = listener;
        this.ois = ois;
    }
    
    public void stop()
    {
        this.running = false;
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                Message message = null;
                if ((message = (Message) ois.readObject()) != null) {
                    listener.recieved(message);
                }
            } catch (IOException ex) {
                Logger.getLogger(MessageRecieverThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MessageRecieverThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

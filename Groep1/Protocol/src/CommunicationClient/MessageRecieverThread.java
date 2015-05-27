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
    
    private ReadWrite rw;
    
    private boolean running = true;
    
    public MessageRecieverThread(ReadWrite rw, CommMessageListener listener) {
        this.listener = listener;
        this.rw = rw;
    }
    
    public void stop()
    {
        this.running = false;
    }
    
    @Override
    public void run() {
        while (running) {
            listener.recieved(rw.readMessage());
        }
    }
    
}

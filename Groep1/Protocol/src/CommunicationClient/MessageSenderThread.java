/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CommunicationClient;

import Protocol.Message;
import java.io.IOException;
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
public class MessageSenderThread implements Runnable {

    private ReadWrite rw;
    
    private boolean running = true;
    
    private List<Message> toSend;
    
    public MessageSenderThread(ReadWrite rw) {
        this.rw = rw;
        this.toSend = new ArrayList<>();
    }
    
    public void addMessage(Message message)
    {
        this.toSend.add(message);
    }
    
    public void stop()
    {
        this.running = false;
    }

    @Override
    public void run() {
        while(running){
            if (toSend.size() > 0)
            { 
                this.rw.sendMessage(this.toSend.get(0));
                this.toSend.remove(0);
            }
        }
    }
    
}

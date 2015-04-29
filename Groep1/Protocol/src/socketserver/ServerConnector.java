/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Michael
 */
public class ServerConnector implements MessageListener
{
    private MessageThread start;
    public static void main(String args[]) throws IOException, ClassNotFoundException 
    {
        ServerConnector go = new ServerConnector();
    }
    
    public ServerConnector()
    {
        start = MessageThread.getInstance(this);
        
        MessageBuilder builder = new MessageBuilder();
        Message message = builder.buildLoginMessage("JHD", "Passwordofzo", 1);
        start.start();
        start.addMessage(message);
    }
    @Override
    public void proces(Message message) {
        System.out.println(message.getText());
    }
}

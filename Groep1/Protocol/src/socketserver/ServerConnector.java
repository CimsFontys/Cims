/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.IOException;

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
        Message message = builder.buildLoginMessage("michaelvaneck", "cims", 1);
        start.start();
        start.addMessage(message);
    }
    @Override
    public void proces(Message message) {
        System.out.println(message.getText());
        start.stop();
    }
}

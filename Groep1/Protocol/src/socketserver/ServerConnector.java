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
    private MessageBuilder messageBuilder;
    
    public static void main(String args[]) throws IOException, ClassNotFoundException 
    {
        ServerConnector go = new ServerConnector();
    }
    
    public ServerConnector()
    {
        start = MessageThread.getInstance(this);
        messageBuilder = new MessageBuilder();
        this.buildLoginMessage("michaelvaneck", "cims", 2);
        start.start();
    }
    
    //HIER KOMEN DE REPLIES VAN DE CIMSSERVER
    @Override
    public void proces(Message message) {
        System.out.println(message.getText());
        
    }
    
    public void buildLoginMessage(String username, String password, int usertype)
    {
        Message message = messageBuilder.buildLoginMessage(username, password, usertype);
        start.addMessage(message);
    }
    
    public void buildRetrieveAllCalamities()
    {
        Message message = messageBuilder.buildRetrieveAllCalamitiesMessage();
        start.addMessage(message);
    }
}

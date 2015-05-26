/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Michael
 */
public class ClientConnector implements MessageListener {

    private MessageThread start;
    private MessageBuilder messageBuilder;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        ClientConnector go = new ClientConnector();
    }
    
    public ClientConnector()
    {
        start = MessageThread.getInstance(this);
        messageBuilder = new MessageBuilder();
    }

    @Override
    public void proces(Message message) 
    {
        System.out.println(message.getText());
    }

    public void buildLoginMessage(String username, String password) {
        Message message = messageBuilder.buildLoginMessage(username, password);
        start.addMessage(message);
        start.start();
    }

    public void buildRetrieveAllCalamities() {
        Message message = messageBuilder.buildRetrieveAllCalamitiesMessage();
        start.addMessage(message);
    }
    
    public void buildInsertPerson(int persontypeid, String firstname, String middlename, 
            String lastname, String username, String password, String SSN, String email,
            String phonenumber, String street, String city, String postal, String region,
            boolean configurator)
    {
        Message message = messageBuilder.buildInsertPerson(persontypeid, firstname, lastname, middlename, username, password, SSN, email, new Date(), phonenumber, street, city, postal, region, city);
        start.addMessage(message);
    }
}

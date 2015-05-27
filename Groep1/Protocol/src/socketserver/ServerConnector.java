/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import CommunicationClient.MessageListener;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.IOException;
import java.util.Date;

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
        
            int persontypeid = 2;
            String firstname = "henk";
            String middlename = "henk";
            String lastname = "henk";
            String username = "henk";
            String password = "henk";
            String ssn = "henk";
            String email = "henk@henk.nl";
            String birthdate;
            String phonenumber = "henk06123213";
            String street = "henkstraat";
            String city = "henkstraat";
            String postal = "henkstraat";
            String region = "henkstraat";
            String configurator ="YES";
         
        //start.addMessage(messageBuilder.buildInsertPerson(persontypeid, firstname, lastname, middlename, username, password, ssn, email, new Date(), phonenumber, street, city, postal, region, configurator));
        
        start.addMessage(messageBuilder.buildLoginMessage("test", "test"));
        
        start.addMessage(messageBuilder.buildRetrieveAllCalamitiesMessage());
        start.addMessage(messageBuilder.buildRetrieveLocations());
        start.addMessage(messageBuilder.buildRetrieveRegions());
        start.addMessage(messageBuilder.buildRetrieveLogs(2));
        start.addMessage(messageBuilder.buildRetrieveLocationTypes());
        start.addMessage(messageBuilder.buildPersonInformation(2));
        start.start();
        
        
    }
    
    //HIER KOMEN DE REPLIES VAN DE CIMSSERVER
    @Override
    public void proces(Message message) {
        System.out.println(message.getText());
    }
    
    public void buildLoginMessage(String username, String password, int usertype)
    {
        //Message message = messageBuilder.buildLoginMessage(username, password, usertype);
        //start.addMessage(message);
    }
    
    public void buildRetrieveAllCalamities()
    {
        Message message = messageBuilder.buildRetrieveAllCalamitiesMessage();
        start.addMessage(message);
    }
}

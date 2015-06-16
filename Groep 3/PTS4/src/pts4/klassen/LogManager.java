/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.klassen;

import CommunicationClient.ComManager;
import CommunicationClient.MessageListener;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.util.Date;
import javafx.animation.ParallelTransitionBuilder;

/**
 *
 * @author Oomis
 */
public class LogManager implements MessageListener {
    
    private CommunicationClient.ComManager comManager;
    private MessageBuilder mb;
    private int personid;
    private String calamityname;
    
    public String getCalamityName()
    {
        return this.calamityname;
    }
    
    public void setPersonId(int personid)
    {
        this.personid = personid;
    }
    
    public int getPersonId()
    {
        return this.personid;
    }
    
    private LogManager() {
      comManager = ComManager.getInstance();
      mb = new MessageBuilder();
    }
    
    public static LogManager getInstance() {
        return LogManagerHolder.INSTANCE;
    }
    
    private static class LogManagerHolder {

        private static final LogManager INSTANCE = new LogManager();
    }
    
    public void insertLog(String text)
    {
        comManager.addMessage(mb.buildInsertLog(personid, text));
    }
    
    public void insertCalamity(String longi, String lat, int personid, String name, String description, Date timestamp, String urgent, String region)
    {
        comManager.addMessage(mb.buildInsertCalamity(region, lat, personid, name, description, new Date(), name, region));
    }
    
    public void getCalamityWithName(String name)
    {
        MessageBuilder messageBuilder = new MessageBuilder();
        Message salami = messageBuilder.buildRetrieveCalamityWithName(name);
        comManager.addMessage(salami); 
    }
    
    public void parseCalamityWithNameResponse(Message message)
    {
        
    }   
    
    @Override
    public void proces(Message message)
    {
        if(message.getType().equals("retrievecalamitywithname"))
        {
            System.out.println("1");
        }
        if(message.getType().equals("retrievecalamitywithnamereply"))
        {
            System.out.println("2");
        }
    }
}

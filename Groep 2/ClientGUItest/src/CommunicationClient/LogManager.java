/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommunicationClient;

import CommunicationClient.ComManager;
import CommunicationClient.MessageListener;
import Protocol.Message;
import Protocol.MessageBuilder;
import javafx.animation.ParallelTransitionBuilder;

/**
 *
 * @author Oomis
 */
public class LogManager implements MessageListener {
    
    private CommunicationClient.ComManager comManager;
    private MessageBuilder mb;
    private int personid;
    
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

    @Override
    public void proces(Message message)
    {
        
    }
    
    private static class LogManagerHolder {

        private static final LogManager INSTANCE = new LogManager();
    }
    
    public void insertLog(String text)
    {
        comManager.addMessage(mb.buildInsertLog(personid, text));
    }
    
}

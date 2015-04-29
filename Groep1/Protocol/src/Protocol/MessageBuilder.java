package Protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Merijn
 */
public class MessageBuilder 
{  
    //private data needed
    private static final int IntLogin = 3;
    private static final int IntCreateAcount = 14;
    private static final int IntLogout = 1;
    private static final int IntMessageTo = 3;
    private static final int IntNewCalamity = 5;
    private static final int IntAddInfo = 6;
    private static final int IntRequestCalamities = 0;
    private static final int IntRequestCalamitie = 1;
    private static final int IntSendLocation = 3;
    private static final int IntRequestStations = 0;
    private static final int IntAddStation = 3;
    private static final int IntLogAction = 3;
    private static final int IntRequestActionsLog = 0;
    
    public static final String Login = "login";
    public static final String LoginReply = "loginreply";
    
    private static final String token = "-";
    
    private final boolean canHaveFile = false;
    //Variables
    private String[] text;
    private File file;
    
    /**
     * Consturctor of the the message builder
     * Takes a type.
     * Creates the needed info to create a message and 
     * validates is bassed on the type.
     * @param type Use like MessageBuilder.LOGIN enz.
     */
    public MessageBuilder()
    {
        
    }
  
    public Message buildLoginMessage(String username, String password, int type)
    {      
        StringBuilder sb = new StringBuilder();
        sb.append(Login).append(token);
        sb.append(username).append(token);
        sb.append(password).append(token);
        sb.append(type);
        
        Message message = new Message();
        message.setText(sb.toString());
        
        return message;
    }
    
    public Message buildLoginReply(int ReceiverID)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(LoginReply).append(token);
        sb.append(ReceiverID);
        
        Message message = new Message();
        message.setText(sb.toString());
        
        return message;
    }
    
}

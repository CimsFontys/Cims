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
    
    /**
     * Static variables of types available to send.
     * Used to prevent mistakes
     */
    public static final MessageType LOGIN = MessageType.login;
    public static final MessageType CREATEACOUNT = MessageType.createAcount;
    public static final MessageType LOGOUT = MessageType.logout;
    public static final MessageType MESSAGETO = MessageType.messageTo;
    public static final MessageType NEWCALAMITIE = MessageType.newCalamitie;
    public static final MessageType ADDINFO = MessageType.addInfo;
    public static final MessageType REQUESTCALAMITIES = MessageType.requestCalamities;
    public static final MessageType REQUESTCALAMITY = MessageType.requestCalamity;
    public static final MessageType SENDLOCATION = MessageType.sendLocation;
    public static final MessageType REQUESTSTATIONS = MessageType.requestStations;
    public static final MessageType ADDSTATION = MessageType.addStation;
    public static final MessageType LOGACTION = MessageType.logAction;
    public static final MessageType REQUESTACTIONSLOG = MessageType.requestActionsLog;
    
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
    
    private static final String token = "-";
    
    private final boolean canHaveFile;
    //Variables
    private MessageType type;
    private String[] text;
    private File file;
    
    /**
     * Consturctor of the the message builder
     * Takes a type.
     * Creates the needed info to create a message and 
     * validates is bassed on the type.
     * @param type Use like MessageBuilder.LOGIN enz.
     */
    public MessageBuilder(MessageType type)
    {
        this.type = type;
        
        switch(this.type)
        {
            case login:
                this.text = new String[IntLogin];
                this.canHaveFile = false;
                break;
            case createAcount:
                this.text = new String[IntCreateAcount];
                this.canHaveFile = false;
                break;
            case logout:
                this.text = new String[IntLogout];
                this.canHaveFile = false;
                break;
            case messageTo:
                this.text = new String[IntMessageTo];
                this.canHaveFile = true;
                break;
            case newCalamitie:
                this.text = new String[IntNewCalamity];
                this.canHaveFile = false;
                break;
            case addInfo:
                this.text = new String[IntAddInfo];
                this.canHaveFile = true;
                break;
            case requestCalamities:
                this.text = new String[IntRequestCalamities];
                this.canHaveFile = false;
                break;
            case requestCalamity:
                this.text = new String[IntRequestCalamitie];
                this.canHaveFile = false;
                break;    
            case sendLocation:
                this.text = new String[IntSendLocation];
                this.canHaveFile = false;
                break;
            case requestStations:
                this.text = new String[IntRequestStations];
                this.canHaveFile = false;
                break;
            case addStation:
                this.text = new String[IntAddStation];
                this.canHaveFile = false;
                break;
            case logAction:
                this.text = new String[IntLogAction];
                this.canHaveFile = false;
                break;
            case requestActionsLog:
                this.text = new String[IntRequestActionsLog];
                this.canHaveFile = false;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    
    /**
     * 
     * @param personelType
     * @param username
     * @param Pasword 
     */
    public void prepMessageLogin(int personelType, String username, String pasword) throws InvalidTypeException
    {
        if(this.type == LOGIN)
        {
            text[0] = Integer.toString(personelType);
            text[1] = username;
            text[2] = pasword;
        }
        else
        {
            throw new InvalidTypeException("Need: " + LOGIN.toString() + " Got: " + type.toString());
        }
    }
    
    public void prepMessageCreateAcount() throws InvalidTypeException
    {
        if(this.type == CREATEACOUNT)
        {
            //TODO
        }
        else
        {
            throw new InvalidTypeException("Need: " + CREATEACOUNT.toString() + " Got: " + type.toString());
        }
    }
    
    public void prepMessageLogout(int id) throws InvalidTypeException
    {
        if(this.type == LOGOUT)
        {
            text[0] = Integer.toString(id);
        }
        else
        {
            throw new InvalidTypeException("Need: " + LOGOUT.toString() + " Got: " + type.toString());
        }
    }
    
    public void prepMessageMessageTo(int idSender, int idReciever, String message) throws InvalidTypeException
    {
        if(this.type == MESSAGETO)
        {
            text[0] = Integer.toString(idSender);
            text[1] = Integer.toString(idReciever);
            text[2] = message;
        }
        else
        {
            throw new InvalidTypeException("Need: " + MESSAGETO.toString() + " Got: " + type.toString());
        }
    }
    
    public void setFile(File file) throws InvalidTypeException
    {
        if(canHaveFile)
        {
            this.file = file;
        }
        else
        {
            throw new InvalidTypeException("Type: " + type.toString() + " Does not suport files");
        }
    }
    public void buildMessage() throws FileNotFoundException, IOException
    {
        Message m = new Message();
        StringBuilder sb = new StringBuilder();
        sb.append(type.toString());
        for(String s : text)
        {
            sb.append(token).append(s);
        }
        m.setText(sb.toString());
        if(this.canHaveFile)
        {     
            FileInputStream in = new FileInputStream(file);
            byte[] fileData = new byte[(int)file.length()];
            in.read(fileData);
            in.close();
            m.setFile(fileData);
        }
       
    }
        
}

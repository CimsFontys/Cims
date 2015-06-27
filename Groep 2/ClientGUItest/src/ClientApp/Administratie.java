/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import Audio.AudioHandler;
import CommunicationClient.ComManager;
import CommunicationClient.LogManager;
import CommunicationClient.MessageListener;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 * Singleton class that communicates with the other classes.
 * @author Leo
 */
public class Administratie implements MessageListener
{
    private static Administratie admin = null;
    private AudioHandler handler;
    private ChatClient cc;
    private ComManager commanager;
    private MessageBuilder mBuilder;
    private int personid;
    private int persontypeid;
    private LogManager logmanager;
    private boolean configurator;
    
    private Administratie()
    {
        //database = new SQL();
        logmanager = LogManager.getInstance();
        handler = new AudioHandler();
        commanager = ComManager.getInstance();
        commanager.addListener(this);
        mBuilder = new MessageBuilder();
    }
    /**
     * sets the reciever to communicate with.
     * @param naam 
     */
    public void setReciever(String naam) 
    {
        this.cc.setReciever(naam);
    }  
    /**
     * 
     * @return the chatclient object that does all the communication between the server and the clients.
     */
    public ChatClient getCc() 
    {
        return cc;
    } 
    /**
     * logs a new emergency unit on.
     * @param username username from the user.
     * @param password password from the user.
     * @return true when the credentials are correct. False when the credentials are incorrect.
     */
    public boolean loginEmergencyService(String username, String password)
    {
        commanager.addMessage(mBuilder.buildLoginMessage(username, password));
        try 
        {
            Thread.sleep(1000);
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(Administratie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(personid != 0)
        {
            return true;
        }
        
        return false;
    }
    /**
     * Makes a new administration object if it does not exsist. 
     * @return An instance of the administration class.
     */
    public static Administratie getInstance()
    {
        if(admin == null)
        {
            admin = new Administratie();
        }
        return admin;
    }
    /**
     * starts the recording process for audiomessages.
     */
    public void startRecordingAudio()
    {
        handler.startRecording();
    }
    /**
     * stops the recording process for audiomessges.
     */
    public void stopRecordingAudio()
    {
        handler.stopRecording();
    }
    /**
     * makes a new chatclient class and connects with the server.
     * @param user the logged in user that wants to connect.
     */
    public void setChatClient(String user)
    {
        cc = new ChatClient(user, persontypeid, personid);
    }
    /**
     * calls on the method to send a new chat message to the current reciever.
     * @param chatmessagestring that as to be send to the the recieving client.
     */
    public void sendMessage(String chatmessagestring)
    {
        cc.sendMessage(chatmessagestring);
    }
    /**
     * calls the method to send a audiomessage to the current reciever.
     */
    public void sendAudioMessage()
    {
        cc.sendAudioMessage(handler.getAudiofile(), handler.getPath());
    }
    /**
     * Reads the login respones recieved from the protocol.
     * Sets the attributes that is needed for a emergency unit.
     * @param message 
     */
    private void readLoginResponse(Message message)
    {
        personid = 0;
        persontypeid = 0;
        configurator = false;

        StringReader reader = new StringReader(message.getText());
        JsonParser parser = Json.createParser(reader);
        Event event = parser.next();

        while (parser.hasNext()) {
            if (event.equals(Event.KEY_NAME)) {
                String keyname = parser.getString();
                event = parser.next();

                switch (keyname) 
                {
                    case "personid":
                        personid = parser.getInt();
                        logmanager.setPersonId(personid);
                        break;
                    case "persontypeid":
                        persontypeid = parser.getInt();
                        break;
                    case "personconfigurator":
                        if (parser.getString() == "YES") {
                            configurator = true;
                        } else {
                            configurator = false;
                        }
                        break;
                    default:
                        break;
                }
                event = parser.next();
            } else {
                event = parser.next();
            }
        }
        
        //hier de code met bovenstaande variabelen

    }

    @Override
    public void proces(Message message) 
    {
        switch (message.getType()) {
            case MessageBuilder.LoginReply:
                System.out.println(message.getText());
                readLoginResponse(message);
                break;

            default:
                break;
        }
    }
}

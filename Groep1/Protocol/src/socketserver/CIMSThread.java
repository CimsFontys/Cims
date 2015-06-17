/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Database.SQL;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 *
 * @author Michael & Merijn
 */
public class CIMSThread implements Runnable {

    protected Socket clientSocket = null;
    protected String serverText = null;
    
    int receiverID = 0;
    boolean configurator = false;
    int persontype = 0;
    boolean running = true;
    MessageBuilder messageBuilder;
    Message message;
    ArrayList<Message> toSend;
    ThreadAdministration administration;

    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    OutputStream os = null;

    public CIMSThread(Socket clientSocket, String serverText) 
    {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
        toSend = new ArrayList<Message>();
        administration = ThreadAdministration.getInstance();
        administration.addClient(this);
        messageBuilder = new MessageBuilder();
    }
    
    public int getReceiverid()
    {
        return this.receiverID;
    }
    
    public void addMessage(Message message) 
    {
        this.toSend.add(message);
    }

    public void run() 
    {        
        synchronized(this)
        {
            try 
            {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
            
            System.out.println("Establishing Connection With: " + clientSocket.getLocalSocketAddress().toString());
            try 
            {
                while (running) 
                {
                    if(clientSocket == null)
                    {
                        break;
                    }
                    
                    while ((message = (Message) ois.readObject()) != null && clientSocket != null) 
                    {
                        if (receiverID == 0) 
                        {
                            switch (message.getType()) 
                            {
                                case MessageBuilder.Login:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Login");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.login(message);
                                    break;
                                case MessageBuilder.InsertPerson:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Insert Person");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.insertPerson(message);
                                    break;
                                default:
                                    break;
                            }
                        } 
                        else 
                        {
                            //ALLE RETRIEVES
                            switch(message.getType())
                            {
                                case MessageBuilder.RetrieveAllCalamities:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Retrieve All Calamities");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrieveAllCalamities(message);
                                    break;
                                case MessageBuilder.RetrieveCalamityWithName:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Retrieve Calamity With Name");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrieveCalamitiesWithName(message);
                                    break;
                                case MessageBuilder.RetrieveAllLocations:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Retrieve All Locations");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrieveAllLocations(message);
                                    break;
                                case MessageBuilder.RetrieveAllRegions:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Retrieve All Regions");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrieveAllRegions(message);
                                    break;
                                case MessageBuilder.RetrieveLogs:
                                    System.out.println("MessageType: Retrieve All Logs");
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrieveLogs(message);
                                    break;
                                case MessageBuilder.RetrieveAllLocationTypes:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Retrieve Location Types");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrieveAllLocationTypes(message);
                                    break;
                                case MessageBuilder.RetrievePersonInformation:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Retrieve Person Information");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrievePersonInformation(message);
                                    break;
                                case MessageBuilder.RetrieveAllCalamitiesDetailed:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Retrieve All Calamities Detailed");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.retrieveAllCalamitiesDetailed(message);
                                    break;
                                case MessageBuilder.InsertLog:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Insert Log");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.insertLog(message);
                                    break;
                                case MessageBuilder.InsertMessage:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Chat Send Message / Insert Message");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.insertMessage(message);
                                    break;
                                case MessageBuilder.InsertCalamity:
                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("CIMSServer: Message Request by: " + clientSocket.getInetAddress().toString());
                                    System.out.println("MessageType: Insert Calamity");
                                    System.out.println("Message Requested By: " + clientSocket.toString());
                                    this.insertCalamity(message);
                                    break;
                                default:
                                    break;
                            }
                        }
                        if(toSend.size() > 0)
                        {
                            oos.writeObject(toSend.get(0));
                            toSend.remove(0);
                            
                        }
                    }
                    
                     if(toSend.size() > 0)
                        {
                            oos.writeObject(toSend.get(0));
                            toSend.remove(0);
                        }
                }
            } 
            catch (ClassNotFoundException ex) 
            {
                administration.removeClient(this);
                running = false;
                Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        catch (IOException ex) 
        {
            administration.removeClient(this);
            running = false;
        }
            
        }
    }
    
     private void insertMessage(Message message)
    {
        if(receiverID != 0)
        {
            StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            JsonParser.Event event = parser.next();
            
            int personid = 0;
            int receiverid = 0;
            String messagetext = "";
            
            while (parser.hasNext()) 
            {
                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();

                    switch (keyname) {
                        case "personid":
                            personid = parser.getInt();
                            break;
                        case "receiverid":
                            receiverid = parser.getInt();
                            break;
                        case "message":
                            messagetext = parser.getString();
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                } else {
                    event = parser.next();
                }
            }
            
            CIMSThread f = administration.findClient(receiverid);
            f.addMessage(message);
                        
            //MESSAGE NAAR JEZELF STUREN?
            
            //SQL retrieve = new SQL();
            //boolean succes = retrieve.insertLog(personid, logdescription);
            //System.out.println("CIMS Server: " + "Log Added To System PID: " + personid);
        }
    }
    
    private void retrieveAllCalamities(Message message)
    {
        if(receiverID != 0)
        {
            SQL retrieve = new SQL();
            String result = retrieve.retrieveAllCalamities();
            
            this.addMessage(messageBuilder.buildRetrieveAllCalamitiesReply(result));
        }
    }
    
    private void retrieveAllCalamitiesDetailed(Message message)
    {
        if(receiverID != 0)
        {
            SQL retrieve = new SQL();
            String result = retrieve.retrieveAllCalamitiesDetailed();
            
            this.addMessage(messageBuilder.buildRetrieveAllCalamitiesDetailedReply(result));
        }
    }
    
    private void retrieveAllLocations(Message message)
    {
        if(receiverID != 0)
        {
            SQL retrieve = new SQL();
            String result = retrieve.retrieveLocations();
            
            this.addMessage(messageBuilder.buildRetrieveLocationsReply(result));
        }
    }
    
    private void retrieveAllRegions(Message message)
    {
        if(receiverID != 0)
        {
            SQL retrieve = new SQL();
            String result = retrieve.retrieveRegions();
            
            this.addMessage(messageBuilder.buildRetrieveRegionsReply(result));
        }
    }
    
    private void retrieveCalamitiesWithName(Message message)
    {
        String calamityname = "";
        int personid = 0;
        
        if (receiverID != 0) {
            StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            JsonParser.Event event = parser.next();

            while (parser.hasNext()) {
                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();

                    switch (keyname) {
                        case "calamityname":
                            calamityname = parser.getString();
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                } else {
                    event = parser.next();
                }

            }
            SQL retrieve = new SQL();
            String response = retrieve.retrieveCalamityWithName(calamityname);
            this.addMessage(messageBuilder.buildRetrieveCalamityWithNameReply(response));
        }
    }
    
    private void retrieveLogs(Message message)
    {
        int personid = 0;
        
        if (receiverID != 0) {
            StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            JsonParser.Event event = parser.next();

            while (parser.hasNext()) {
                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();

                    switch (keyname) {
                        case "personid":
                            personid = parser.getInt();
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                } else {
                    event = parser.next();
                }

            }
            SQL retrieve = new SQL();
            String response = retrieve.retrieveLogs(personid);
            this.addMessage(messageBuilder.buildRetrieveLogsReply(response));
        }
    }
    
    private void retrievePersonInformation(Message message)
    {
        int personid = 0;
        
        if (receiverID != 0) {
            StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            JsonParser.Event event = parser.next();

            while (parser.hasNext()) {
                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();

                    switch (keyname) {
                        case "personid":
                            personid = parser.getInt();
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                } else {
                    event = parser.next();
                }
            }
            SQL retrieve = new SQL();
            String response = retrieve.getPersonInformation(personid);
            this.addMessage(messageBuilder.buildPersonInformationReply(response));
        }
    }
    
    private void retrieveAllLocationTypes(Message message)
    {
        if(receiverID != 0)
        {
            SQL retrieve = new SQL();
            String result = retrieve.retrieveLocationTypes();
            
            this.addMessage(messageBuilder.buildRetrieveLocationTypesReply(result));
        }
    }
    
    private void insertLog(Message message)
    {
        StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            JsonParser.Event event = parser.next();
            
            int personid = 0;
            String logdescription = "";
            
            while (parser.hasNext()) {
                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();

                    switch (keyname) {
                        case "personid":
                            personid = parser.getInt();
                            break;
                        case "logdescription":
                            logdescription = parser.getString();
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                } else {
                    event = parser.next();
                }

            }
            SQL retrieve = new SQL();
            boolean succes = retrieve.insertLog(personid, logdescription);
            System.out.println("CIMS Server: " + "Log Added To System PID: " + personid);
    }
    
    private void insertCalamity(Message message)
    {
        
    }
    
    private void insertPerson(Message message)
    {
        StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            JsonParser.Event event = parser.next();
            
            int persontypeid = 0;
            String firstname = "";
            String middlename = "";
            String lastname = "";
            String username = "";
            String password = "";
            String ssn = "";
            String email = "";
            String birthdate;
            String phonenumber = "";
            String street = "";
            String city = "";
            String postal = "";
            String region = "";
            String configurator ="";
            

            while (parser.hasNext()) {
                if (event.equals(JsonParser.Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();

                    switch (keyname) {
                        case "persontype":
                            persontypeid = parser.getInt();
                            break;
                        case "firstname":
                            firstname = parser.getString();
                            break;
                        case "middlename":
                            middlename = parser.getString();
                            break;
                        case "lastname":    
                            lastname = parser.getString();
                            break;
                        case "username":
                            username = parser.getString();
                            break;
                        case "password":
                            password = parser.getString();
                            break;
                        case "ssn":
                            ssn = parser.getString();
                            break;
                        case "email":
                            email = parser.getString();
                            break;
                        case "birthdate":
                            birthdate = parser.getString();
                            break;
                        case "phonenumber":
                            phonenumber = parser.getString();
                            break;
                        case "street":
                            street = parser.getString();
                            break;
                        case "city":
                            city = parser.getString();
                            break;
                        case "postal":
                            postal = parser.getString();
                            break;
                        case "region":
                            region = parser.getString();
                            break;
                        case "configurator":
                            configurator = parser.getString();
                            break;
                        default:
                            break;
                    }
                    event = parser.next();
                } else {
                    event = parser.next();
                }

            }
            SQL retrieve = new SQL();
            boolean succes = retrieve.insertPerson(persontypeid, firstname, lastname, middlename, username, password, ssn, email, new Date(), phonenumber, street, city, postal, region, configurator);
            System.out.println("CIMS Server: " + "Person Added To System: " + username);
    }

    private synchronized void login(Message message) 
    {        
        StringReader reader = new StringReader(message.getText());
        JsonParser parser = Json.createParser(reader);
        JsonParser.Event event = parser.next();

        String username = "";
        String password = "";
        int persontype = 0;
        while (parser.hasNext()) 
        {
            if (event.equals(JsonParser.Event.KEY_NAME)) 
            { 
                String keyname = parser.getString();
                event = parser.next();

                switch (keyname) {
                    case "username":
                        username = parser.getString();
                        break;
                    case "password":
                        password = parser.getString();
                        break;
                    case "persontype":
                        persontype = parser.getInt();
                        break;
                    default:
                        break;
                }
            } 
            else if (event.equals(JsonParser.Event.END_OBJECT)) 
            {
                SQL login = new SQL();
                String result = login.loginPerson(username, password);

                StringReader reader2 = new StringReader(result);
                JsonParser parser2 = Json.createParser(reader2);
                JsonParser.Event event2 = parser2.next();

                int personid = 0;
                int persontypeid = 0;
                String configurator = "";

                while (parser2.hasNext()) 
                {
                    if (event2.equals(JsonParser.Event.KEY_NAME)) {
                        String keyname = parser2.getString();
                        event2 = parser2.next();

                        switch (keyname) {
                            case "personid":
                                personid = parser2.getInt();
                                break;
                            case "persontypeid":
                                persontypeid = parser2.getInt();
                                break;
                            case "personconfigurator":
                                configurator = parser2.getString();
                                break;
                            default:
                                break;
                        }
                    }
                    else if(event2.equals(JsonParser.Event.END_OBJECT))
                    {
                        if(personid == 0)
                        {
                            
                        }
                        else
                        {
                            this.receiverID = personid;
                            this.persontype = persontypeid;
                            
                            if(configurator.equals("YES"))
                            {
                                this.configurator = true;
                            }
                            else
                            {
                                this.configurator = false;
                            }
                            
                            this.addMessage(new MessageBuilder().buildLoginReply(result));
                            parser.close();
                            parser2.close();
                            break;
                            
                        }
                    }
                    else
                    {
                        event2 = parser2.next();
                    }
                }
                break;
            } 
            else 
            {
                event = parser.next();
            }

        }
    }
}

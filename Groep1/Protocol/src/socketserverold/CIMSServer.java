/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Database.SQL;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 *
 * @author Michael
 */
public class CIMSServer implements Runnable{

    int receiverID = 0;
    boolean configurator = false;
    int persontype = 0;
    boolean running = true;
    Socket csocket;
    MessageBuilder messageBuilder;
    Message message;
    ArrayList<Message> toSend;
    ThreadAdministration administration;

    static ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    OutputStream os = null;

    CIMSServer(Socket csocket) 
    {
        this.csocket = csocket;
        toSend = new ArrayList<Message>();
        administration = ThreadAdministration.getInstance();
        administration.addClient(this);
        messageBuilder = new MessageBuilder();
    }

    public void addMessage(Message message) 
    {
        this.toSend.add(message);
    }

    public static void main(String args[])
            throws Exception {
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("CIMS Communication Server, Starting Up.....");
        System.out.println("Awaiting Communications");
        while (true) 
        {
            ExecutorService executor = Executors.newFixedThreadPool(100);
            for (int i = 0; i < 10; i++) {
            //Runnable worker = new WorkerThread('' + i);
            //executor.execute(worker);
          }
            Socket sock = serverSocket.accept();
            System.out.println("Connecting to Client on: " + sock.getRemoteSocketAddress().toString());
            new Thread(new CIMSServer(sock)).start();           
        }
    }
    
    private void insertCalamity(Message message)
    {
        
    }
        
    private void insertMessage(Message message)
    {
        if(receiverID != 0)
        {
            StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            Event event = parser.next();
            
            int personid = 0;
            int receiverid = 0;
            String messagetext = "";
            
            while (parser.hasNext()) 
            {
                if (event.equals(Event.KEY_NAME)) {
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
            
            CIMSServer f = administration.findClient(receiverid);
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
            Event event = parser.next();

            while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
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
            Event event = parser.next();

            while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
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
            Event event = parser.next();

            while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
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
            Event event = parser.next();
            
            int personid = 0;
            String logdescription = "";
            
            while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
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
    
    private void insertPerson(Message message)
    {
        StringReader reader = new StringReader(message.getText());
            JsonParser parser = Json.createParser(reader);
            Event event = parser.next();
            
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
                if (event.equals(Event.KEY_NAME)) {
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
        Event event = parser.next();

        String username = "";
        String password = "";
        int persontype = 0;
        while (parser.hasNext()) 
        {
            if (event.equals(Event.KEY_NAME)) 
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
            else if (event.equals(Event.END_OBJECT)) 
            {
                SQL login = new SQL();
                String result = login.loginPerson(username, password);

                StringReader reader2 = new StringReader(result);
                JsonParser parser2 = Json.createParser(reader2);
                Event event2 = parser2.next();

                int personid = 0;
                int persontypeid = 0;
                String configurator = "";

                while (parser2.hasNext()) 
                {
                    if (event2.equals(Event.KEY_NAME)) {
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
                    else if(event2.equals(Event.END_OBJECT))
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

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(csocket.getOutputStream());
            ois = new ObjectInputStream(csocket.getInputStream());

            try 
            {
                while (running) 
                {
                    if(csocket == null)
                    {
                        break;
                    }
                    
                    while ((message = (Message) ois.readObject()) != null && csocket != null) 
                    {
                        if (receiverID == 0) 
                        {
                            switch (message.getType()) 
                            {
                                case MessageBuilder.Login:
                                    System.out.println("MessageType: Login");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.login(message);
                                    break;
                                case MessageBuilder.InsertPerson:
                                    System.out.println("MessageType: Insert Person");
                                    System.out.println("Message Requested By: " + csocket.toString());
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
                                    System.out.println("MessageType: Retrieve All Calamities");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrieveAllCalamities(message);
                                    break;
                                case MessageBuilder.RetrieveCalamityWithName:
                                    System.out.println("MessageType: Retrieve Calamity With Name");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrieveCalamitiesWithName(message);
                                    break;
                                case MessageBuilder.RetrieveAllLocations:
                                    System.out.println("MessageType: Retrieve All Locations");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrieveAllLocations(message);
                                    break;
                                case MessageBuilder.RetrieveAllRegions:
                                    System.out.println("MessageType: Retrieve All Regions");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrieveAllRegions(message);
                                    break;
                                case MessageBuilder.RetrieveLogs:
                                    System.out.println("MessageType: Retrieve All Logs");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrieveLogs(message);
                                    break;
                                case MessageBuilder.RetrieveAllLocationTypes:
                                    System.out.println("MessageType: Retrieve Location Types");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrieveAllLocationTypes(message);
                                    break;
                                case MessageBuilder.RetrievePersonInformation:
                                    System.out.println("MessageType: Retrieve Person Information");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrievePersonInformation(message);
                                    break;
                                case MessageBuilder.RetrieveAllCalamitiesDetailed:
                                    System.out.println("MessageType: Retrieve All Calamities Detailed");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.retrieveAllCalamitiesDetailed(message);
                                    break;
                                case MessageBuilder.InsertLog:
                                    System.out.println("MessageType: Insert Log");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.insertLog(message);
                                    break;
                                case MessageBuilder.InsertMessage:
                                    System.out.println("MessageType: Chat Send Message / Insert Message");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.insertMessage(message);
                                    break;
                                case MessageBuilder.InsertCalamity:
                                    System.out.println("MessageType: Insert Calamity");
                                    System.out.println("Message Requested By: " + csocket.toString());
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
//    @Override
//    public void run() 
//    {
//        try {
//            oos = new ObjectOutputStream(csocket.getOutputStream());
//            ois = new ObjectInputStream(csocket.getInputStream());
//
//        } catch (IOException ex) {
//            Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        try 
//        { 
//            while ((message = (Message) ois.readObject()) != null && csocket != null) 
//            {
//                //oos.writeObject("Message Received");
//                System.out.println("Message Received");
//
//                if (receiverID == 0) 
//                {
//                    String messagetext = message.getText();
//                    String[] messagesplit = messagetext.split("-");
//
//                    if (messagesplit[0].equals("login")) 
//                    {
//                        System.out.println("MessageType: " + "Login");
//                        System.out.println("Requested By: " + csocket.toString());
//                        //SHIT OM IN TE LOGGEN EN ZET DE ID VAN DE RECEIVER
//                        //USER MOET HIER EERST INLOGGEN
//                        //ONTLEED MESSAGE
//                        //ONDERSTAANDE VARIABELEN MOETEN UIT MESSAGE GEHAALD WORDEN
//                        String username = messagesplit[1];
//                        String password = messagesplit[2];
//                        int emergencytype = Integer.parseInt(messagesplit[3]);
//                        
//                        String test = new SQL().loginPerson(username, password);
//                        System.out.println("Reply Message: " + test);
//                        
//                        Message replyMessage = null;
//                        replyMessage = messageBuilder.buildLoginReply(test);
//                        
//                        oos.writeObject(replyMessage);
//                    }
//                    else
//                    {
//                        //RETURN ERRORMESSAGE USER NIET INGELOGD
//                    }
//                    if(messagesplit[0].equals("retrieveallcalamities"))
//                    { 
//                        System.out.println("MessageType: " + "Retrieve All Calamities");
//                        System.out.println("Requested By: " + csocket.toString());
//                    }
//                    else
//                    {
//                    }
//                } 
//                else {
//                    messageReceiver = new ServerMessageReceiver();
//                    Message reply = messageReceiver.processMessage(message);
//
//                    if (reply != null) {
//                        //SEND REPLY TO SENDER
//                        oos.writeObject(reply);
//                    }
//                }
//
//                //STUUR RESPONSE NAAR RESPONSECLIENT
////                Message newMessage = new Message();
////                newMessage.setText("yay");
////
////                oos.writeObject(newMessage);
//                //fromClientSocket.close();
//            }
//        } catch (IOException ex) {
//            //Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}

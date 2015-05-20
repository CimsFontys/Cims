/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Protocol.Message;
import Protocol.MessageBuilder;
import Protocol.ServerMessageReceiver;
import cims.startup.SQL;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

/**
 *
 * @author Michael
 */
public class CIMSServer implements Runnable {

    int receiverID = 0;
    boolean running = true;
    Socket csocket;
    MessageBuilder messageBuilder;
    ServerMessageReceiver messageReceiver;
    Message message;
    ArrayList<Message> toSend;
    ThreadAdministration administration;

    static ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    OutputStream os = null;

    ArrayList<String> requestedMessages;
    ArrayList<String> connectedClients;

    CIMSServer(Socket csocket) {
        this.csocket = csocket;
        toSend = new ArrayList<Message>();
        requestedMessages = new ArrayList<String>();
        connectedClients = new ArrayList<String>();
        administration = ThreadAdministration.getInstance();
        administration.addClient(this);
        messageBuilder = new MessageBuilder();
    }

    public void addMessage(Message message) {
        this.toSend.add(message);
    }

    public static void main(String args[])
            throws Exception {
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("CIMS Communication Server, Starting Up.....");
        System.out.println("Awaiting Communications");
        while (true) {
            Socket sock = serverSocket.accept();
            System.out.println("Connecting to Client on: " + sock.getLocalAddress().toString());
            new Thread(new CIMSServer(sock)).start();
        }
    }

    private void login(Message message) 
    {
        StringReader reader = new StringReader(message.getText());
        JsonParser parser = Json.createParser(reader);
        System.out.println(message.getText());
        Event event = parser.next();
        
        String username = "";
        String password = "";
        int persontype = 0;
        while (parser.hasNext()) 
        {
            if (event.equals(Event.KEY_NAME)) 
            {
                String keyname = parser.getString();
                parser.next();

                switch (keyname) 
                {
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
            else if(event.equals(Event.END_OBJECT))
            {
                SQL login = new SQL();
                String result = login.loginPerson(username, password);
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

            try {
                while (running) {
                    while ((message = (Message) ois.readObject()) != null && csocket != null) {
                        if (receiverID == 0) 
                        {
                            switch(message.getType())
                            {
                                case "login":
                                    System.out.println("MessageType: Login");
                                    System.out.println("Message Requested By: " + csocket.toString());
                                    this.login(message);
                                    break;
                                default:
                                    break;
                            }
                        } 
                        else {

                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
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

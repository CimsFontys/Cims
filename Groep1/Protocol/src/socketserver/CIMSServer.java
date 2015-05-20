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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Michael
 */
public class CIMSServer implements Runnable 
{
    int receiverID = 0;
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

    CIMSServer(Socket csocket) 
    {
        this.csocket = csocket;
        toSend = new ArrayList<Message>();
        requestedMessages = new ArrayList<String>();
        connectedClients = new ArrayList<String>();
        administration = ThreadAdministration.getInstance();
        administration.addClient(this);
        messageBuilder = new MessageBuilder();
    }
    
    public ArrayList<String> getRequestedMessages()
    {
        return this.requestedMessages;
    }

    public void addMessage(Message message) {
        this.toSend.add(message);
    }

    public static void main(String args[])
    throws Exception 
    {       
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("CIMS Communication Server, Starting Up.....");
        System.out.println("Awaiting Communications");
        while (true) 
        {
            Socket sock = serverSocket.accept();
            System.out.println("Connecting to Client on: " + sock.getLocalAddress().toString());
            new Thread(new CIMSServer(sock)).start();
        }
    }

    @Override
    public void run() 
    {
        try {
            oos = new ObjectOutputStream(csocket.getOutputStream());
            ois = new ObjectInputStream(csocket.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try 
        { 
            while ((message = (Message) ois.readObject()) != null && csocket != null) 
            {
                //oos.writeObject("Message Received");
                System.out.println("Message Received");

                if (receiverID == 0) 
                {
                    String messagetext = message.getText();
                    String[] messagesplit = messagetext.split("-");

                    if (messagesplit[0].equals("login")) 
                    {
                        System.out.println("MessageType: " + "Login");
                        System.out.println("Requested By: " + csocket.toString());
                        //SHIT OM IN TE LOGGEN EN ZET DE ID VAN DE RECEIVER
                        //USER MOET HIER EERST INLOGGEN
                        //ONTLEED MESSAGE
                        //ONDERSTAANDE VARIABELEN MOETEN UIT MESSAGE GEHAALD WORDEN
                        String username = messagesplit[1];
                        String password = messagesplit[2];
                        int emergencytype = Integer.parseInt(messagesplit[3]);
                        
                        String test = new SQL().loginPerson(username, password);
                        System.out.println("Reply Message: " + test);
                        
                        Message replyMessage = null;
                        replyMessage = messageBuilder.buildLoginReply(test);
                        
                        oos.writeObject(replyMessage);
                    }
                    else
                    {
                        //RETURN ERRORMESSAGE USER NIET INGELOGD
                    }
                    if(messagesplit[0].equals("retrieveallcalamities"))
                    { 
                        System.out.println("MessageType: " + "Retrieve All Calamities");
                        System.out.println("Requested By: " + csocket.toString());
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievepersoninformation"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("insertperson"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("insertcalamity"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievealllocations"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievealllocationtypes"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("insertlocation"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrieveallregions"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievecalamitiesfromregion"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievecalamitiesfrompersonid"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievecalamitywithid"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("insertlog"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievelogs"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("insertmessage"))
                    {
                    }
                    else
                    {
                    }
                    if(messagesplit[0].equals("retrievemessages"))
                    {
                    }
                    else
                    {
                    }
                } 
                else {
                    messageReceiver = new ServerMessageReceiver();
                    Message reply = messageReceiver.processMessage(message);

                    if (reply != null) {
                        //SEND REPLY TO SENDER
                        oos.writeObject(reply);
                    }
                }

                //STUUR RESPONSE NAAR RESPONSECLIENT
//                Message newMessage = new Message();
//                newMessage.setText("yay");
//
//                oos.writeObject(newMessage);
                //fromClientSocket.close();
            }
        } catch (IOException ex) {
            //Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

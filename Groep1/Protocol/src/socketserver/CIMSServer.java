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
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class CIMSServer implements Runnable {

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

    CIMSServer(Socket csocket) 
    {
        this.csocket = csocket;
        toSend = new ArrayList<Message>();
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
        System.out.println("Listening for Clients");
        while (true) {
            Socket sock = serverSocket.accept();
            System.out.println("Connected to Client");
            new Thread(new CIMSServer(sock)).start();
        }
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(csocket.getOutputStream());
            ois = new ObjectInputStream(csocket.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while ((message = (Message) ois.readObject()) != null) {
                //oos.writeObject("Message Received");
                System.out.println("Message Received");

                if (receiverID == 0) 
                {
                    String messagetext = message.getText();
                    String[] messagesplit = messagetext.split("-");

                    if (messagesplit[0].equals("login")) {
                        //SHIT OM IN TE LOGGEN EN ZET DE ID VAN DE RECEIVER
                        //USER MOET HIER EERST INLOGGEN
                        //ONTLEED MESSAGE
                        //ONDERSTAANDE VARIABELEN MOETEN UIT MESSAGE GEHAALD WORDEN
                        String username = messagesplit[1];
                        String password = messagesplit[2];
                        int emergencytype = Integer.parseInt(messagesplit[3]);
                        
                        receiverID = new SQL().loginEmergencyService(username, password, emergencytype);
                        System.out.println("receiverID = " + receiverID);
                        
                        Message replyMessage = null;
                        replyMessage = messageBuilder.buildLoginReply(receiverID);
                        
                        oos.writeObject(replyMessage);
                    }
                    else
                    {
                        //RETURN ERRORMESSAGE USER NIET INGELOGD
                    }

                } else {
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
            Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CIMSServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

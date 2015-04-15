/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Protocol.Message;
import Protocol.MessageBuilder;
import Protocol.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Michael
 */
public class MultiThreadedSocketServer 
{   
    public static void main(String args[]) throws IOException
    {
        final int portNumber = 9000;
        System.out.println("Starting CIMS server on port " + portNumber);
        ServerSocket serverSocket = new ServerSocket(portNumber);
        
        /**
         * MESSAGEBUILDER OBJECT THAT CREATES EN DECREATES THE MESSAGE OBJEC
         */
        MessageBuilder messageBuilder;
         
        /**
         * MESSAGE TYPE ENUM TO DETERMINE MESSAGETYPE
         */
        MessageType messageType;
        
        /**
         * SOCKET FROM MESSAGE SENDER
         */
        Socket fromClientSocket = null;
        
        /**
         * SOCKET FROM MESSAGE RECEIVER
         */
        Socket toClientSocket = null;
        
        /**
         * MESSAGE OBJECT THATS TRANSMITTED
         */
        Message message;
        
        /**
         * FILE BYTE[] FROM FILE
         */
        byte[] file;
        
        /**
         * TEXT FROM MESSAGE OBJECT
         */
        String text;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        OutputStream os = null;
        
        while (true) 
        {
            fromClientSocket = serverSocket.accept();
            
            try
            {
                oos = new ObjectOutputStream(fromClientSocket.getOutputStream());
            }
            catch(IOException e)
            {
                
            }
            
            try
            {
                ois = new ObjectInputStream(fromClientSocket.getInputStream());
            }
            catch(IOException e)
            {
                
            }
            
            try
            {
                os = fromClientSocket.getOutputStream();
            }
            catch(IOException e)
            {
                
            }
                    
            
            try
            {
                /**
                 * GAAT HIER IN EEN WOESTE INFINITE LOOP!??!?!
                 */
                while((message = (Message) ois.readObject()) != null)
                {
                    oos.writeObject("Message Received");
                    System.out.println("Message Received");
                    text = message.getText();
                    file = message.getFile();
                    //DOE SHIT IN DATABASE OFZO?
                    //STUUR RESPONSE NAAR RESPONSECLIENT

                    PrintWriter pw = new PrintWriter(os, true);

                    pw.println("Message Received, Processing!");
                    
                    oos.writeObject("Finished Processing");
                    fromClientSocket.close();
                    break;
                }
            }
            
            catch(IOException | ClassNotFoundException e)
            {
                fromClientSocket.close();
                toClientSocket.close();
            }
            
//            Socket socket = serverSocket.accept();
//            OutputStream os = socket.getOutputStream();
//            PrintWriter pw = new PrintWriter(os, true);
//            pw.println("What's you name?");
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String str = br.readLine();
//
//            pw.println("Hello, " + str);
//            pw.close();
//            socket.close();
//
//            System.out.println("Just said hello to:" + str);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import Protocol.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Michael
 */
public class ServerConnector {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        /**
         * IP ADRESS FROM CIMS SERVER
         */
        final String host = "localhost";
        final int portNumber = 9000;

        Message message = null;

        Socket socket1 = null;

        System.out.println("Creating socket to '" + host + "' on port " + portNumber);

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        byte[] file;
        String text = "test";

        String str = "";
        
        String response1 = "Message Received";

        while (true) {
            file = new byte[2];
            file[0] = 1;
            file[1] = 1;

            socket1 = new Socket(host, portNumber);

            try {
                ois = new ObjectInputStream(socket1.getInputStream());

            } catch (IOException e) {

            }

            try {
                oos = new ObjectOutputStream(socket1.getOutputStream());

            } catch (Exception e) {

            }

            message = new Message();
            message.setText(text);
            message.setFile(file);

            oos.writeObject(message);
            
            if((String)ois.readObject() == response1)
            {
                System.out.println("SHIT ONTVANGE");
            }

            try {
                while ((str = (String) ois.readObject()) != null) 
                {
                    if (str.equals("Finished Processing")) {
                        break;
                    }
                }

                ois.close();
                oos.close();
                socket1.close();
            } catch (IOException | ClassNotFoundException e) {

            }

        }
    }

}

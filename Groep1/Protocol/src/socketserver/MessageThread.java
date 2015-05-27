/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import CommunicationClient.MessageListener;
import Protocol.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class MessageThread implements Runnable {

    private static Thread thread = null;
    private static boolean shouldStop = false;
    private static MessageThread INSTANCE;
    private static ArrayList<Message> messages = null;

    private static ArrayList<MessageListener> messageListeners = null;

    private static final String host = "localhost";
    private static final int portNumber = 9000;
    private static Socket socket1 = null;

    private static ObjectOutputStream oos = null;
    private static ObjectInputStream ois = null;

    private MessageThread() {
        INSTANCE = this;
        messages = new ArrayList<>();
        messageListeners = new ArrayList<>();

        try {
            socket1 = new Socket(host, portNumber);
            oos = new ObjectOutputStream(socket1.getOutputStream());
            ois = new ObjectInputStream(socket1.getInputStream());

        } catch (IOException e) {
        }

        thread = new Thread(this);

    }

    public void start() {
        try {
            thread.start();
            System.out.println(thread.isAlive());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void stop() {
        if (thread.isAlive()) {
            try {
                shouldStop = true;
                System.out.println("Message Thread stopped, Communication Finished");
                socket1 = null;
                ois = null;
                oos = null;
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void run() {
        /**
         * shouldStop equals false so messages can be send
         */
        while (!MessageThread.shouldStop) //while(true)
        {
            Message message = null;

            if (messages.size() > 0) {
                System.out.println("size is grooter dan 0");
                try {
                    oos.writeObject(messages.get(0));
                    System.out.println("Message Send: " + messages.get(0).getType());
                    System.out.println("send");
                } catch (IOException ex) {
                    System.out.println("nope");
                }
                messages.remove(0);
            }
            try {

                if ((message = (Message) ois.readObject()) != null) {
                    for (MessageListener ml : messageListeners) {
                        ml.proces(message);
                        System.out.println("Processing Message");
                    }
                }

            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

    /**
     *
     * @param listener
     * @return
     */
    public static MessageThread getInstance(MessageListener listener) {
        if (INSTANCE == null) {
            new MessageThread();
        }

        messageListeners.add(listener);

        return INSTANCE;
    }

    public void addMessage(Message message) {
        messages.add(message);
        System.out.println(thread.isAlive() + " thread status");
    }

    public void removeListener(MessageListener listener) {
        messageListeners.remove(listener);
    }
}

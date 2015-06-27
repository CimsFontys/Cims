/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.chatserver;

import Audio.AudioHandler;
import chat.AudioMessage;
import chat.ChatMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.application.Platform;
import static javafx.collections.FXCollections.observableMap;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
 
/**
 *
 * @author pieter
 */

public class Server
{
    private Map<String, Client> clients;
    private ArrayList<String> clientNames;
    private ServerThread st;
    private transient ObservableMap<String, Client> observableClients;
    private AudioHandler handler;
    private String naam;
    
    public Server(MapChangeListener<String,Client> mcl) 
    {
        clients = new HashMap<String, Client>();
        observableClients = observableMap(clients);
        clientNames = new ArrayList<String>();
        clientNames.add("Meldkamer");
        st = new ServerThread(this);
        Thread t = new Thread(st);
        t.start();
        observableClients.addListener(mcl);
        this.naam = "Meldkamer";
        handler = new AudioHandler();
    }
    /**
     * Calls the method to start recordig.
     */
    public void startRecrding()
    {
        handler.startRecording();
    }
    /**
     * Calls the method to stop the recording process.
     */
    public void stopRecording()
    {
        handler.stopRecording();
    }
    /**
     * Sends the audio message to the reciever.
     * @param reciever that recieves the audiomessage.
     */
    public void sendAudioMessage(String reciever)
    {
        AudioMessage audiomessage = new AudioMessage("Audiobericht ontvagen van: " + this.naam, this.naam, reciever, handler.getAudiofile());
        audiomessage.setAudiopath(handler.getPath());
        sendMessage(audiomessage);
    }
    /**
     * removes a client from the server this happens when a client disconnects.
     * @param client that disconnected from the server.
     */
    public synchronized void removeClient(String client)
    {
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() {
                observableClients.remove(client);
                clientNames.remove(client);
                sendClientName(client);
            }
        });        
    }
    /**
     * sends a chatmessage to the recieving client.
     * @param message that needs to be send to the client.
     */
    public synchronized void sendMessage(ChatMessage message)
    {
        System.out.println("ik stuur een bericht naar: " + message.getOntvanger());
        Client c = clients.get(message.getOntvanger());
        if(message.getAfzender().equals("Meldkamer"))
        {
            c.addMessageToObservable(message);
        }
        c.sendMessage(message);        
    }
    /**
     * sends a new client name to all the conencted client.
     * @param name of the newly connected client.
     */
    public void sendClientName(String name)
    {
        Iterator it = clients.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String, Client> entry = (Map.Entry) it.next();
            Client c = entry.getValue();
            System.out.println(c.getNaam() + " " + name);
            c.sendClientName(name);
        }
    }
    /**
     * adds a new client to the observable clients hashmap.
     * @param client that is newly connectedand needs to be added.
     */
    public void putClient(Client client)
    {
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                client.sendClients(clientNames);
                clientNames.add(client.getNaam());  
                sendClientName(client.getNaam());
                observableClients.put(client.getNaam(), client);   
            }
        });        
    }
    /**
     * gets a certian client form the observable hashmap.
     * @param naam of the client that needs to be retrieved.
     * @return 
     */
    public Client getClient(String naam)
    {
        return clients.get(naam);
    }
}

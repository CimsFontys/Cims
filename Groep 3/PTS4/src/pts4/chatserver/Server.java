/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.chatserver;

import chat.Message;
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

import pts4.gui.ServerGUIController;
public class Server
{
    private Map<String, Client> clients;
    private ArrayList<String> clientNames;
    private serverThread st;
    private transient ObservableMap<String, Client> observableClients;
    
    public Server(MapChangeListener<String,Client> mcl) 
    {
        clients = new HashMap<String, Client>();
        observableClients = observableMap(clients);
        clientNames = new ArrayList<String>();
        clientNames.add("Meldkamer");
        st = new serverThread(this);
        Thread t = new Thread(st);
        t.start();
        observableClients.addListener(mcl);
    }
    
    public synchronized void removeClient(String client)
    {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                observableClients.remove(client);
                sendClientName(client);
            }
        });        
    }
    
    public synchronized void sendMessage(Message message)
    {
        System.out.println("ik stuur een bericht naar: " + message.getOntvanger());
        clients.get(message.getOntvanger()).sendMessage(message);
        
    }
    
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
    
    public void putClient(Client client)
    {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                client.sendClients(clientNames);
                clientNames.add(client.getNaam());  
                sendClientName(client.getNaam());
                observableClients.put(client.getNaam(), client);   
            }
        });        
    }
    
    public Client getClient(String naam)
    {
        return clients.get(naam);
    }
}

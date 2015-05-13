package pts4.klassen;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import pts4.chatserver.Client;
import pts4.chatserver.Server;
import pts4.database.IDatabase;
import pts4.database.SQL;
import pts4.rssfeed.RSSReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Max
 */
public class Administration {
    
    public static ArrayList<Incident> incidents;
    public ArrayList<Incident> pendingIncidents;
    private Server server;
    private IDatabase databaseconn;
    
    public Administration() throws MalformedURLException {
        incidents = new ArrayList<>();
        pendingIncidents = new ArrayList<>();
        java.sql.Timestamp  sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
        /*databaseconn = new SQL();
        for (String s: databaseconn.retrieveAllCalamities())
        {
            incidents.add(stringToIncident(s));
        }*/
        
        loadFromRSSFeed();
    }
    
    public Incident stringToIncident(String input) {
        Incident returnIncident = null;
        
        int indexId = input.indexOf("-ID_CALAMITY:");
        int indexLong = input.indexOf("-GEO_LONG:");
        int indexLat = input.indexOf("-GEO_LAT:");
        int indexName = input.indexOf("-NAME:");
        int indexDescription = input.indexOf("-DESCRIPTION:");
        int indexDate = input.indexOf("-DATE:");
        
        String stringId = input.substring(indexId + 13, indexLong);
        String longitude = input.substring(indexLong + 10, indexLat);
        String latitude = input.substring(indexLat + 9, indexName);
        String name = input.substring(indexName + 6, indexDescription);
        String description = input.substring(indexDescription + 13, indexDate);
        String stringDate = input.substring(indexDate + 6);
        
        int id = Integer.parseInt(stringId);
        Date date = Date.valueOf(stringDate);
        
        try {
            returnIncident = new Incident(id, longitude, latitude, name, description, date);
        } catch (Exception e) {
            
        }
        
        return returnIncident;
    }
    
    public void loadFromRSSFeed() throws MalformedURLException {
        RSSReader reader = RSSReader.getInstance();
        reader.setURL(new URL("http://rss.politie.nl/rss/algemeen/ab/algemeen.xml"));
        reader.writeFeed();
        pendingIncidents.clear();
        for (Incident i : reader.getIncidents()) {
            pendingIncidents.add(i);
        }
    }
    
    public void loadFromRSSFeed(String zoekTerm) throws MalformedURLException {
        RSSReader reader = RSSReader.getInstance();
        reader.setURL(new URL("http://rss.politie.nl/rss/nb/provincies/" + zoekTerm.replaceAll("00000", "-") +".xml"));
        reader.writeFeed();
        pendingIncidents.clear();
        for (Incident i : reader.getIncidents()) {
            pendingIncidents.add(i);
        }
    }
    
    public synchronized void syncAcceptedAndPending() {
        
//        ArrayList<Incident> templist = (ArrayList<Incident>) pendingIncidents.clone();
//        for (Incident i : incidents) {
//            for (Incident pi : pendingIncidents) {
//                if (pi.getName().matches(i.getName())) {
//                    templist.remove(pendingIncidents.indexOf(pi));
//                }
//            }
//        }
//        pendingIncidents = templist;
        
//        Iterator<Incident> iter = pendingIncidents.iterator();
//        while (iter.hasNext()) {
//            for (Incident i : incidents) {
//                if (iter.next().getName().matches(i.getName())) {
//                    iter.remove();
//                }
//            }
//        }
    }
    
    public ObservableList<Incident> getIncidents() {
        
        return FXCollections.observableArrayList(incidents);
    }
    
    public ObservableList<Incident> getPendingIncidents() {
        
        return FXCollections.observableArrayList(pendingIncidents);
    }
    
    public void removePendingIncident(Incident incident) {
        try {
            this.pendingIncidents.remove(incident);
        }
        catch (Exception e) {
            
        }
    }
    
    public void addIncident(Incident i) {
        incidents.add(i);
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, HH:mm");    
        Date resultdate = new Date(yourmilliseconds);
        //databaseconn.insertCalamity(i.getLongitude(), i.getLatitude(), i.getId(), i.getName(), i.getDescription(), i.getDate(), i.getExplosion(), i.getFire(), i.getToxicity(), i.getUrgent(), i.getViolent());
    }
    
    public Server getServer() {
        return this.server;
    }
    
    public void setServer(MapChangeListener<String,Client> s) {
        this.server = new Server(s);
    }
}

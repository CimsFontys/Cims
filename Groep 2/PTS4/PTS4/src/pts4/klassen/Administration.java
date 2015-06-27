package pts4.klassen;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import pts4.chatserver.Client;
import pts4.chatserver.Server;

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
    
    private ArrayList<Incident> incidents;
    private Server server;
    
    public Administration() {
        incidents = new ArrayList<>();
        incidents.add(new Incident("name", "description", 12345, 123456));
        incidents.add(new Incident("name2", "description2iuhnlhliu", 1234345, 12345356));
        incidents.add(new Incident("nametje3", "description3ugiyvtykbgilyfcjtrfhgjhjh", 1236445, 123454356));
    }
    
    public ObservableList<Incident> getIncidenten() {
        
        return FXCollections.observableArrayList(incidents);
    }
    
    public void addIncident(Incident i) {
        incidents.add(i);
    }
    
    public Server getServer() {
        return this.server;
    }
    
    public void setServer(MapChangeListener<String,Client> s) {
        this.server = new Server(s);
    }
}

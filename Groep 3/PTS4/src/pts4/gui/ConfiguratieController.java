/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.io.IOException;
import java.io.StringReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import pts4.database.SQL;
import pts4.klassen.LogManager;
/**
 *
 * @author Gijs
 */
public class ConfiguratieController implements Initializable {
    
    private SQL dbcon;
    @FXML
    private Button btnLogOut;
    
    @FXML
    private TextField VoorNaamTB;
    
    @FXML
    private TextField VoorLatTB;
    
    @FXML
    private TextField VoorLonTB;
    
    @FXML
    private Button AddBT;
    
    @FXML
    private ComboBox<String> VoorzieningCB;
        
    @FXML 
    private ComboBox<String> PersoneelCB;
    
    @FXML
    private ListView<String> LogListView; 
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
      dbcon = new SQL();
      
        VoorzieningCB.getItems().add("Police");
        VoorzieningCB.getItems().add("Hospital");
        VoorzieningCB.getItems().add("Fire department");
       
        
    }
    
    @FXML
    public void AddFaciity()
    {
        int noodtype = 0;
        String facilityType = "";
        if(VoorzieningCB.getSelectionModel().getSelectedItem().toString().equals("Fire department"))
        {
            noodtype = 1; 
            facilityType = "Fire department";
            
        }
        else if(VoorzieningCB.getSelectionModel().getSelectedItem().toString().equals("Police"))
        {
            noodtype = 2;
            facilityType = "Police";
        }
        else if(VoorzieningCB.getSelectionModel().getSelectedItem().toString().equals("Hospital")) 
        {
            noodtype = 3; 
            facilityType = "Hospital";
        }
       
             

            dbcon.insertLocation(VoorNaamTB.getText(), VoorLonTB.getText(), VoorLatTB.getText(), noodtype);
            LogManager.getInstance().insertLog("Facility (" + facilityType + ") has been added: Name:" + VoorNaamTB.getText() + "Longitude: " + VoorLonTB.getText() + ", Latitude: " + VoorLatTB.getText() );

    }
    
    @FXML
    public HashMap<Integer, String> SetPeopleInCB()
    {
           /*
        HashMap<Integer, String> Info = new HashMap<Integer, String>();
     
        ArrayList<Integer> PersonID = new ArrayList<>();
        ArrayList<String> PersonNames = new ArrayList<>();
        
      String jsonStr = dbcon.retrieveAllPersons();
      int personid = 0;
      String firstname = "";
      String lastname = "";
      
        StringReader reader = new StringReader(jsonStr);
        JsonParser parser = Json.createParser(reader);
        Event event = parser.next();
        
        while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();
                    switch (keyname) {
                        default:
                        case "personid":
                            personid = parser.getInt();
                            
                           

                            break;
                       case "personfirstname":
                        firstname = parser.getString();
                            break;
                       case "lastpersonname":
                           lastname = parser.getString();
                            break;
                           
                    }
                    Info.put(personid, firstname + " " + lastname);
                    event = parser.next();
                } else {
                    event = parser.next();
                }
                

            }
        
                for(HashMap.Entry<Integer, String> key : Info.entrySet())
                {
                    PersoneelCB.getItems().add(key.getValue());            
                }
                
                */
                return null;
    }
    
    @FXML
    public void SetLogList()
    {
        /*
        HashMap<Integer, String> Info  = SetPeopleInCB();
        String jsonStr = "";
        
         for(HashMap.Entry<Integer, String> key : Info.entrySet())
                {
                    if(key.getValue() == PersoneelCB.getSelectionModel().getSelectedItem())
                    {
                        jsonStr = dbcon.retrieveLogs(key.getKey());
                    }
                }
        
    
        String logdate = "";
        String foundlog = "";
        ArrayList<String> Found = new ArrayList<>();
        StringReader reader = new StringReader(jsonStr);
        JsonParser parser = Json.createParser(reader);
        Event event = parser.next();
        
        while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();
                    switch (keyname) {
                        default:
                        case "logdescription":
                            foundlog = parser.getString();
                            

                            break;
                       case "logdate":
                        logdate = parser.getString();
                            break;
                     
                           
                    }
                   Found.add(logdate + " : "  + foundlog);
                    event = parser.next();
                } else {
                    event = parser.next();
                }
                

            }
        
        for(String S : Found)
        {
            LogListView.getItems().add(S);
        }
       */
    }
    
   public void btnLogOut_Click() throws IOException
   {
        Stage currentstage = (Stage) btnLogOut.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Inloggen.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        currentstage.close();
    }              
}

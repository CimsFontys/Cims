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
import java.util.Collections;
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
    private ComboBox<String> cbLogNames;
    
    @FXML
    private ListView LogListView; 
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
      dbcon = new SQL();
      
        VoorzieningCB.getItems().add("Police");
        VoorzieningCB.getItems().add("Hospital");
        VoorzieningCB.getItems().add("Fire department");
        
        cbLogNames.getItems().clear();
        SetPeopleInCB();
    }
    
    @FXML
    public void AddFacility()
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
        
       
            LogManager.getInstance().insertLocation(VoorNaamTB.getText(), VoorLonTB.getText(), VoorLatTB.getText(), noodtype);
            //dbcon.insertLocation(VoorNaamTB.getText(), VoorLonTB.getText(), VoorLatTB.getText(), noodtype);
            LogManager.getInstance().insertLog("Facility (" + facilityType + "): '" + VoorNaamTB.getText() + "' has been added!");

    }
    
    @FXML
    public void SetPeopleInCB()
    {
        
     // HashMap<Integer, String>
        HashMap<Integer, String> Info = new HashMap<Integer, String>();
  
        ArrayList<Integer> PersonID = new ArrayList<>();
        ArrayList<String> Firstnames = new ArrayList<>();
        ArrayList<String> Middlenames = new ArrayList<>();
        ArrayList<String> Lastnames = new ArrayList<>();

        String jsonStr = dbcon.retrieveAllPersons();
        int personid = 0;
        String firstname = "";
        String middlename = "";
        String lastname = "";
      
        StringReader reader = new StringReader(jsonStr);
        JsonParser parser = Json.createParser(reader);
        Event event = parser.next();
        
        while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();
                    switch (keyname) {
                        case "personid":
                        personid = parser.getInt(); 
                        PersonID.add(personid);
                        break;
                    case "firstname":
                        firstname = parser.getString();
                        Firstnames.add(firstname);

                        break;
                    case "middlename":
                        middlename = parser.getString();
                         Middlenames.add(middlename);
                    case "lastname":
                        lastname = parser.getString();
                        Lastnames.add(lastname);
                        break;
                    default:
                        break;
                    }
                    
                   
                    event = parser.next();
                } else {
                    event = parser.next();
                }
                

            }
        
                for(int i = 0; i < PersonID.size(); i++)
                {
                    cbLogNames.getItems().add(PersonID.get(i) + ". " + Firstnames.get(i) + " " + Middlenames.get(i) + Lastnames.get(i));
                }
                
                
               // return Info;
    }
    
    @FXML
    public void SetLogList()
    {
        String selectedItem = cbLogNames.getSelectionModel().getSelectedItem();
        int personindex = selectedItem.indexOf(".");
        int selectedPersonID = Integer.parseInt(selectedItem.substring(0, personindex));
        System.out.println(selectedItem.substring(0, personindex));
        
        
        
        String jsonStr = dbcon.retrieveLogs(selectedPersonID);
        
     
    
        String logdate = "";
        String foundlog = "";
        ArrayList<String> FoundDates = new ArrayList<>();
        ArrayList<String> FoundLogStrings = new ArrayList<>();
        StringReader reader = new StringReader(jsonStr);
        JsonParser parser = Json.createParser(reader);
        Event event = parser.next();
        
        while (parser.hasNext()) {
                if (event.equals(Event.KEY_NAME)) {
                    String keyname = parser.getString();
                    event = parser.next();
                      switch (keyname) {
                        case "logdate":
                        logdate = parser.getString(); 
                        FoundDates.add(logdate);
                        break;
                    case "logdescription":
                        foundlog = parser.getString();
                        FoundLogStrings.add(foundlog);
                        break;        
                    default:
                        break;
                    }
                  
                    event = parser.next();
                } else {
                    event = parser.next();
                }
                

            }
        
        for(int j = 0; j < FoundLogStrings.size(); j++)
        {
            LogListView.getItems().add(FoundDates.get(j) + " " +  FoundLogStrings.get(j));
          
        }
    
    }
    
   public void btnLogOut_Click() throws IOException
   {
        Stage currentstage = (Stage) btnLogOut.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Inloggen.fxml"));
        LogManager.getInstance().insertLog("User has logged out");
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        currentstage.close();
    }              
}

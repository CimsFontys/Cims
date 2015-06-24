/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.gui;

import CommunicationClient.ComManager;
import CommunicationClient.MessageListener;
import Protocol.Message;
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
 * FXML Controller class
 *
 * @author Oomis
 */
public class ConfigurationController implements Initializable, MessageListener {
    
    
    private SQL dbcon;
    @FXML
    private TextField tbFacilityName;
    @FXML
    private TextField tbFacilityLongitude;
    @FXML
    private TextField tbFacilityLatitude;
    @FXML
    private Button btnAddFacility;
    @FXML
    private ListView<String> lvLogs;
    @FXML
    private ComboBox<String> cbFacility;
    @FXML
    private ComboBox<String> cbLogNames;
    
    @FXML
    private Button btn_LogOut;
    
    private LogManager manager = LogManager.getInstance();
    private ComManager comManager = ComManager.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        dbcon = new SQL();
        
        comManager.addListener(this);
      
        cbFacility.getItems().add("Police");
        cbFacility.getItems().add("Hospital");
        cbFacility.getItems().add("Fire department");
        
        cbLogNames.getItems().clear();
        SetPeopleInCB(cbLogNames);
    }
    
     @FXML
    public void AddFacility()
    {
        int facilitynumber = 0;
        String facilityType = "";
        if(cbFacility.getSelectionModel().getSelectedItem().toString().equals("Fire department"))
        {
            facilitynumber = 1; 
            facilityType = "Fire department";
            
        }
        else if(cbFacility.getSelectionModel().getSelectedItem().toString().equals("Police"))
        {
            facilitynumber = 2;
            facilityType = "Police";
        }
        else if(cbFacility.getSelectionModel().getSelectedItem().toString().equals("Hospital")) 
        {
            facilitynumber = 3; 
            facilityType = "Hospital";
        }
        
      
       
            LogManager.getInstance().insertLocation(tbFacilityName.getText(), tbFacilityLongitude.getText(), tbFacilityLatitude.getText(), facilitynumber);
            //dbcon.insertLocation(VoorNaamTB.getText(), VoorLonTB.getText(), VoorLatTB.getText(), noodtype);
            LogManager.getInstance().insertLog("Facility (" + facilityType + "): '" + tbFacilityName.getText() + "' has been added!");

    }
    
    /**
     * Fills the chosen ComboBox with all users. 
     * @param userComboBox the ComboBox that needs to be filled.
     */
    @FXML
    public void SetPeopleInCB(ComboBox<String> userComboBox)
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
                    userComboBox.getItems().add(PersonID.get(i) + ". " + Firstnames.get(i) + " " + Middlenames.get(i) + Lastnames.get(i));
                }
                
                
               // return Info;
    }
    
    @FXML
    public void getLogs()
    {
        String selectedItem = cbLogNames.getSelectionModel().getSelectedItem();
        int personindex = selectedItem.indexOf(".");
        int selectedPersonID = Integer.parseInt(selectedItem.substring(0, personindex));
        System.out.println(selectedItem.substring(0, personindex));
        manager.getLogs(selectedPersonID);
    }
    /**
     * Fills the listView lvLogs with logs, based on the selected user. 
     */
    public void SetLogListView(String jsonStr)
    {
        //String selectedItem = cbLogNames.getSelectionModel().getSelectedItem();
        //int personindex = selectedItem.indexOf(".");
        //int selectedPersonID = Integer.parseInt(selectedItem.substring(0, personindex));
        //System.out.println(selectedItem.substring(0, personindex));
        
        
        
        //String jsonStr = dbcon.retrieveLogs(selectedPersonID);
        
     
    
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
            lvLogs.getItems().add(FoundDates.get(j) + " " +  FoundLogStrings.get(j));
          
        }
    
    }
    
    /**
     * Logs out the currently logged in user.
     * @throws IOException 
     */
   @FXML
   public void btnLogOut_Click() throws IOException
   {
        Stage currentstage = (Stage) btn_LogOut.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        LogManager.getInstance().insertLog("User has logged out");
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        currentstage.close();
    }   

    @Override
    public void proces(Message message)
    {
     
        if(message.getType().equals("retrievelogsreply"))
        {
            
            SetLogListView(message.getText());
        }
      
        
    }
    
}

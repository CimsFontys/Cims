/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import CommunicationClient.ComManager;
import CommunicationClient.MessageListener;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import pts4.database.IDatabase;
import pts4.database.SQL;
import pts4.klassen.LogManager;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

/**
 *
 * @author Gijs
 */
public class InloggenController implements Initializable, MessageListener
{
    @FXML TextField tbGebruikersnaam;
    @FXML PasswordField tbWachtwoord;
    
    private IDatabase dbcon;
    
    private LogManager logManager;
    private ComManager comManager;
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
          dbcon = new SQL();
          logManager = LogManager.getInstance();
          comManager = ComManager.getInstance();
          comManager.addListener(this);
    }
    
    public void btnInloggen_Click(ActionEvent event) throws IOException
    {
        Stage currentstage = (Stage) tbGebruikersnaam.getScene().getWindow();
        String response = dbcon.loginPerson(tbGebruikersnaam.getText(), tbWachtwoord.getText());
        System.out.println(response);
        
        MessageBuilder messageBuilder = new MessageBuilder();
        Message inlog = messageBuilder.buildLoginMessage(tbGebruikersnaam.getText(), tbWachtwoord.getText());
        comManager.addMessage(inlog);
        
        StringReader reader = new StringReader(response);
        JsonParser parser = Json.createParser(reader);
        Event event2 = parser.next();
        
        int personid = 0;
        int persontypeid = 0;
        String configurator = "";
        
        while(parser.hasNext())
        {
            if(event2.equals(Event.KEY_NAME))
            {
                String keyname = parser.getString();
                event2 = parser.next();
                
                switch(keyname)
                {
                    case "personid":
                        personid = parser.getInt();
                        logManager.setPersonId(personid);
                        System.out.println("Personid: " + personid);
                        break;
                    case "persontypeid":
                        persontypeid = parser.getInt();
                        break;
                    case "personconfigurator":
                        configurator = parser.getString();
                        break;
                    default:
                        break;
                }
                
                event2 = parser.next();
            }
            else
            {
                event2 = parser.next();
            }
        }
        
        if (response.equals(""))
        {
            MessageBox mb = new MessageBox("Onjuiste gebruikersnaam en/of wachtwoord.",MessageBoxType.OK_ONLY);
            mb.show();
            tbGebruikersnaam.clear();
            tbWachtwoord.clear();
        }
        else
        {
            int index = response.indexOf("configurator");
            String conf = response.substring(index);          
            if (!conf.contains("YES"))
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUIFX.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));  
                stage.show();  
                currentstage.close();
            }
            else
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Configuratie.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));  
                stage.show();   
                currentstage.close();
            }
        }
    }

    @Override
    public void proces(Message message) 
    {
        System.out.println(message.getText());
    }
}

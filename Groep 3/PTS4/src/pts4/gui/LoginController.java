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
public class LoginController implements Initializable, MessageListener
{
    @FXML TextField tbUsername;
    @FXML PasswordField tbPassword;
    
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
    
    /**
     * 
     * @param event
     * @throws IOException 
     * Tis handler makes logging in possible. It checks the response it gets from the database and loops over the parameters to check if the account is a configurator or not.
     */
    public void btnInloggen_Click(ActionEvent event) throws IOException
    {
        //LogManager.getInstance().login(tbUsername.getText(), tbPassword.getText());
        Stage currentstage = (Stage) tbUsername.getScene().getWindow();
        String response = dbcon.loginPerson(tbUsername.getText(), tbPassword.getText());
        System.out.println(response);
        
        MessageBuilder messageBuilder = new MessageBuilder();
        Message inlog = messageBuilder.buildLoginMessage(tbUsername.getText(), tbPassword.getText());
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
            MessageBox mb = new MessageBox("Wrong username and/or password.",MessageBoxType.OK_ONLY);
            mb.show();
            tbUsername.clear();
            tbPassword.clear();
        }
        else
        {        
            if (configurator.equals("YES"))
            {
                LogManager.getInstance().insertLog("User has logged in, will be directed to the main application");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUIFX.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));  
                stage.show();  
                currentstage.close();
            }
            else

            {
                LogManager.getInstance().insertLog("User has logged in, will be directed to the configuration screen");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Configuration.fxml"));
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

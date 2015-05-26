/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socketserver.ClientConnector;

/**
 *
 * @author Michael
 */
public class DemoFXController implements Initializable
{
    private Parent root;
    private Scene scene;
    private Stage stage;
    private Scene currentscene;
    private Stage currentstage;
    
    private ClientConnector clientConnector;
    
    @FXML private TextField textfield_username;
    @FXML private TextField textfield_password;
    @FXML private Button button_login;
    @FXML private Button button_calamities;
            
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        clientConnector = new ClientConnector();
    }
    
    public void login()
    {
        String username = textfield_username.getText();
        String password = textfield_password.getText();
        
        clientConnector.buildLoginMessage(username, password);
    }
    
    public void allCalamities()
    {
        clientConnector.buildRetrieveAllCalamities();
    }
}

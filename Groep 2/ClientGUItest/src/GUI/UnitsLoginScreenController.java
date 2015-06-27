/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import ClientApp.Administratie;
import CommunicationClient.LogManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Leo
 */
public class UnitsLoginScreenController implements Initializable {
    @FXML
    private TextField tfGebruikersnaam;
    @FXML
    private TextField tfWachtwoord;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lbError;
    
    private Administratie admin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        admin = Administratie.getInstance();
    }    
    /**
     * logs in a new emergency unit with the give credentials. 
     * If these are incorrect the user gets feedback from the gui.
     * @param event 
     */
    @FXML
    private void btnLogin_onClick(ActionEvent event) 
    {
        String username = tfGebruikersnaam.getText();
        if(tfWachtwoord.getText().equals(""))
        {
            lbError.setText("Put in a password");
        }
        else if(tfGebruikersnaam.getText().equals(""))
        {
            lbError.setText("Put in a username");
        }
        else
        {
            if(admin.loginEmergencyService(username, tfWachtwoord.getText()) == true)
            {
                LogManager logman = LogManager.getInstance();
                logman.insertLog("Log: Login Chat Client By: " + logman.getPersonId());
                try 
                {
                    URL location1 = getClass().getResource("clienttestGUI.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location1);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

                    Parent root = (Parent) (Node) fxmlLoader.load(location1.openStream());

                    ClienttestGUIController ctrl1 = (ClienttestGUIController) fxmlLoader.getController();
                    ctrl1.setUser(username);
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    Stage stage2 = (Stage) btnLogin.getScene().getWindow();
                    // do what you have to do
                    stage2.close();
                    //show the stage
                    stage.showAndWait();
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(UnitsLoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                lbError.setText("Username or password is incorrect.");
            }
        }
    }
    
}

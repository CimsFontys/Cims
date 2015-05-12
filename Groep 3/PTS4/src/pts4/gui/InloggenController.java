/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pts4.database.IDatabase;
import pts4.database.SQL;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

/**
 *
 * @author Gijs
 */
public class InloggenController implements Initializable
{
    @FXML TextField tbGebruikersnaam;
    @FXML TextField tbWachtwoord;
    
    IDatabase dbcon;
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //dbcon = new SQL();
    }
    
    public void btnInloggen_Click(ActionEvent event)
    {
        if (tbGebruikersnaam.getText().equals("meldkamer") && tbWachtwoord.getText().equals("meldkamer"))
        {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("GUIFX.fxml"));
                    GUIController controller = new GUIController();
                    loader.setController(controller);
                    loader.setRoot(controller);
                    Parent root;
                    try 
                    {
                        root = (Parent) loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();  
                    } 
                    catch (IOException ex) 
                    {
                           MessageBox mb = new MessageBox("Er is iets misgegaan. Mogelijk is er geen verbinding met de database.",MessageBoxType.OK_ONLY);
                           mb.show();
                    }
        }
        else if (tbGebruikersnaam.getText().equals("conf") && tbWachtwoord.getText().equals("conf"))
        {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Configuratie.fxml"));
                    ConfiguratieController controller = new ConfiguratieController();
                    loader.setController(controller);
                    loader.setRoot(controller);
                    Parent root;
                    try 
                    {
                        root = (Parent) loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();  

                    } 
                    catch (IOException ex) 
                    {
                           MessageBox mb = new MessageBox("Er is iets misgegaan. Mogelijk is er geen verbinding met de database.",MessageBoxType.OK_ONLY);
                           mb.show();
                    }
        }
        else
        {
            MessageBox mb = new MessageBox("Account niet bekend of niet goed ingevuld.",MessageBoxType.OK_ONLY);
        }
    }
}

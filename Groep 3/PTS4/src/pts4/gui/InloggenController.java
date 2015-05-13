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
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
    }
    
    public void btnInloggen_Click(ActionEvent event)
    {
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

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

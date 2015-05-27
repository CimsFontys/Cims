/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.io.IOException;
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
    @FXML PasswordField tbWachtwoord;
    
    private IDatabase dbcon;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
          dbcon = new SQL();
    }
    
    public void btnInloggen_Click(ActionEvent event) throws IOException
    {
        Stage currentstage = (Stage) tbGebruikersnaam.getScene().getWindow();
        String response = dbcon.loginPerson(tbGebruikersnaam.getText(), tbWachtwoord.getText());
        System.out.println(response);
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pts4.database.SQL;

/**
 *
 * @author Gijs
 */
public class ConfiguratieController implements Initializable {
    
    private SQL dbcon;

    
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
        
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
      dbcon = new SQL();
      
        VoorzieningCB.getItems().add("Politiebureau");
        VoorzieningCB.getItems().add("Ziekenhuis");
        VoorzieningCB.getItems().add("Brandweerkazerne");
        
    }
    
    @FXML
    public void AddFaciity(ActionEvent event)
    {
        int noodtype = 0;
        if(VoorzieningCB.getSelectionModel().getSelectedItem().toString().equals("Brandweerkazerne"))
        {
            noodtype = 1; 
        }
        else if(VoorzieningCB.getSelectionModel().getSelectedItem().toString().equals("Politiebureau"))
        {
            noodtype = 2;
        }
        else if(VoorzieningCB.getSelectionModel().getSelectedItem().toString().equals("Ziekenhuis")) 
        {
            noodtype = 3; 
        }
       
            dbcon.insertLocation(VoorNaamTB.getText(), VoorLonTB.getText(), VoorLatTB.getText(), noodtype);
       

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import pts4.database.IDatabase;
import pts4.database.SQL;

/**
 *
 * @author Gijs
 */
public class ConfiguratieController implements Initializable {
   
    @FXML
    private ComboBox CbNoodType;
    
    private IDatabase dbcon;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        CbNoodType.getItems().add("Politiebureau");
        CbNoodType.getItems().add("Ziekehuizen");
        CbNoodType.getItems().add("Brandweerkazerne");
        CbNoodType.getItems().add("Comandopost");
        
    }
}

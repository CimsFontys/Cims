/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import pts4.database.IDatabase;
import pts4.database.SQL;

/**
 *
 * @author Gijs
 */
public class ConfiguratieController implements Initializable {
    
    private IDatabase dbcon;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        dbcon = new SQL();
    }
}

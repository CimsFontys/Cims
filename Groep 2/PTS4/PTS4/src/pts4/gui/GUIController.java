/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.gui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pts4.chatserver.*;
import pts4.klassen.*;

/**
 *
 * @author Max
 */
public class GUIController implements Initializable, MapChangeListener<String, Client> {

    private Administration admin;
    
    @FXML TextField tfName;
    @FXML TextArea tfDescription;
    @FXML TextField tfLongitude;
    @FXML TextField tfLatitude;
    
    @FXML TextField tfAddName;
    @FXML TextArea tfAddDescription;
    @FXML TextField tfAddLongitude;
    @FXML TextField tfAddLatitude;
    
    @FXML ListView lvIncidents;
    @FXML ListView lvIncidents2;
    
    @FXML ComboBox cbUnits;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        admin = new Administration();
        admin.setServer(this);
        cbUnits.getItems().clear();
        initComboboxes();
    }
    
    private void initComboboxes() {
        
        //cbCategory.setItems(FXCollections.observableList(categorylist));
        lvIncidents.setItems(admin.getIncidenten());
        lvIncidents2.setItems(admin.getIncidenten());
    }
    
    public void selectIncident() {

        for (Incident a : admin.getIncidenten()) {
            if (a.equals(lvIncidents2.getSelectionModel().getSelectedItem())) {

                tfName.setText(a.getName());
                tfDescription.setText(a.getDescription());
                tfLongitude.setText(Long.toString(a.getLongitude()));
                tfLatitude.setText(Long.toString(a.getLatitude()));
            }
        }
    }
    
    public void addIncident() {
        
        String name = tfAddName.getText();
        String description = tfAddDescription.getText();
        String longitude = tfAddLongitude.getText();
        String latitude = tfAddLatitude.getText();
        
        try {
            admin.addIncident(new Incident(name, description, Long.parseLong(longitude, 10), Long.parseLong(latitude, 10)));
            this.initComboboxes();
        }
        catch (Exception e) {
            System.out.println("Incident not added. Check your longitude and latitude.");
        }
        
        tfAddName.setText("");
        tfAddDescription.setText("");
        tfAddLongitude.setText("");
        tfAddLatitude.setText("");
    }
    
    @FXML
    public void itemSelected() throws MalformedURLException, URISyntaxException, InterruptedException, IOException 
    {
        System.out.println("hallo");
        URL location1 = getClass().getResource("ServerGUI.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location1);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) (Node) fxmlLoader.load(location1.openStream());

        ServerGUIController ctrl1 = (ServerGUIController) fxmlLoader.getController();
        ctrl1.setServer(admin.getServer(), (String) cbUnits.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        //show the stage
        stage.showAndWait();
    }

    @Override
    public void onChanged(Change<? extends String, ? extends Client> change) {
        if(change.wasAdded())
        {
            cbUnits.getItems().add(change.getValueAdded().getNaam());
        }
        else if(change.wasRemoved())
        {
            cbUnits.getItems().remove(change.getValueRemoved().getNaam());
        }
    }
}

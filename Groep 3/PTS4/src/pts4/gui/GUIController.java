/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.gui;

import chat.EmergencyUnit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pts4.chatserver.*;
import pts4.googlemaps.Gmaps;
import pts4.klassen.*;
import pts4.rssfeed.EnumProvincies;

/**
 *
 * @author Max
 */
public class GUIController implements Initializable, MapChangeListener<String, Client> {

    private Administration admin;
    private Gmaps g;
    @FXML
    Button btnLogOut;
    @FXML
    TextField tfName;
    @FXML
    TextArea tfDescription;
    @FXML
    TextField tfLongitude;
    @FXML
    TextField tfLatitude;
    @FXML
    TextField tfGiftig;
    @FXML
    TextField tfAantalGewonden;
    @FXML
    ChoiceBox cbExplosie;
    @FXML
    ChoiceBox cbBrand;
    @FXML
    ChoiceBox cbGewelddadig;
    @FXML
    ChoiceBox cbSpoed;
    @FXML
    TextArea txtDescription;


    @FXML
    Button btnLocation;
    @FXML
    Button btnSubmit;
    @FXML
    TextField tfAddName;
    @FXML
    TextArea tfAddDescription;
    @FXML
    TextField tfAddLongitude;
    @FXML
    TextField tfAddLatitude;
    @FXML
    StackPane gmapsstack;
    @FXML
    Pane gmaps;
    @FXML
    AnchorPane anchor;
    @FXML
    ListView lvunits;
    @FXML
    ListView lvIncidents;
    @FXML
    ListView lvIncidents2;
    @FXML
    ListView lvPendingIncidents;
    @FXML
    ListView lvAcceptedIncidents;
    @FXML
    TextArea taPendingName;
    @FXML
    TextArea taPendingDescription;

    @FXML
    ComboBox cbUnits;
    @FXML
    ComboBox cbProvincies;
    @FXML
    ComboBox cbincident;
    @FXML
    ComboBox cbUnit;
    @FXML
    TextField txtlongitude;
    @FXML
    TextField txtlatitude;
    @FXML
    Button btnincident;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            admin = new Administration();
        } catch (MalformedURLException ex) {
            Logger.getLogger(GUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        final SwingNode swingNode = new SwingNode();

        

        admin.setServer(this);
        g = new Gmaps(this, admin.getServer());
        g.createSwingContent(swingNode);
        gmapsstack.getChildren().add(swingNode);
        initComboboxes();
        cbUnits.getItems().clear();
        cbExplosie.getItems().clear();
        cbBrand.getItems().clear();
        cbGewelddadig.getItems().clear();
        cbSpoed.getItems().clear();
        
        cbProvincies.getItems().setAll(EnumProvincies.values());
        cbProvincies.setValue(EnumProvincies.Nederland);
        cbExplosie.getItems().add("Ja");
        cbExplosie.getItems().add("Nee");
        cbBrand.getItems().add("Ja");
        cbBrand.getItems().add("Nee");
        cbGewelddadig.getItems().add("Ja");
        cbGewelddadig.getItems().add("Nee");
        cbSpoed.getItems().add("Ja");
        cbSpoed.getItems().add("Nee");
    }

    public void initComboboxes() {

        //cbCategory.setItems(FXCollections.observableList(categorylist));
        cbUnit.setItems(admin.getUnits());
        cbincident.setItems(admin.getIncidents());
        lvIncidents.setItems(admin.getIncidents());
        lvIncidents2.setItems(admin.getIncidents());
        lvunits.setItems(admin.getUnits());
        lvPendingIncidents.setItems(admin.getPendingIncidents());
        lvAcceptedIncidents.setItems(admin.getIncidents());
        
    }
    
    public void goIncident() {
        for (Incident a : admin.getIncidents()) {
            if (a.equals(lvIncidents.getSelectionModel().getSelectedItem())) {
                //g.goIncident(Double.parseDouble(a.getLongitude()), Double.parseDouble(a.getLatitude()));
                //tfAddName.setText(a.getName());
                //tfAddDescription.setText(a.getDescription());
                //tfAddLongitude.setText(a.getLongitude());
                //tfAddLatitude.setText(a.getLatitude());
            }
        }
    }
    
    public void setProvincie() throws MalformedURLException {
        
        String selected = cbProvincies.getValue().toString();
        if (selected.equalsIgnoreCase("Nederland")) {
            admin.loadFromRSSFeed();
        }
        else {
            admin.loadFromRSSFeed(selected);
        }
        admin.syncAcceptedAndPending();
        lvPendingIncidents.setItems(admin.getPendingIncidents());
    }

    public void giveOrder()
    {
        g.unitAanmaak = true;
        g.id = cbUnit.getSelectionModel().getSelectedItem().toString();
    }
    
    public void selectIncident() {

        for (Incident a : admin.getIncidents()) {
            if (a.equals(lvIncidents2.getSelectionModel().getSelectedItem())) {
                initComboboxes();
                g.drawUnits();
                g.goIncident(Double.parseDouble(a.getLongitude()), Double.parseDouble(a.getLatitude()));
                tfName.setText(a.getName());
                tfDescription.setText(a.getDescription());
                tfLongitude.setText(a.getLongitude());
                tfLatitude.setText(a.getLatitude());
            }
        }
    }
    
    public void selectUnit() {
        for (Unit a : admin.getUnits())
        {
            if (a.equals(lvunits.getSelectionModel().getSelectedItem()))
            {
                g.goIncident(a.getLongitude(), a.getLatidude());
            }
        }
    }
    
    public void goPendingIncident() {

        for (Incident a : admin.getPendingIncidents()) {
            if (a.equals(lvPendingIncidents.getSelectionModel().getSelectedItem())) {

                taPendingName.setText(a.getName());
                taPendingDescription.setText(a.getDescription());
            }
        }
    }
    
    public String getUnitDescription()
    {
        return txtDescription.getText();
    }
    
    public String getIncidentorder()
    {
        return cbincident.getSelectionModel().getSelectedItem().toString();
    }
    public void setUnit(double Longitude, double Latitude)
    {
        txtlongitude.setText(Double.toString(Longitude));
        txtlatitude.setText(Double.toString(Latitude));
    }
    
    public void acceptIncident() {

        for (Incident a : admin.getPendingIncidents()) {
            if (a.equals(lvPendingIncidents.getSelectionModel().getSelectedItem())) {

                admin.addIncident(a);
                admin.removePendingIncident(a);
                taPendingName.setText("");
                taPendingDescription.setText("");
                g.addIncidentOnMap(a);
                this.initComboboxes();
            }
        }
    }

    public void addIncident() {

        String name = tfAddName.getText();
        String description = tfAddDescription.getText();
        String longitude = tfAddLongitude.getText();
        String latitude = tfAddLatitude.getText();

        try {
            Incident incident = new Incident(longitude, latitude, name, description);
            admin.addIncident(incident);
            g.addIncidentOnMap(incident);
            this.initComboboxes();
        } catch (Exception e) {
            System.out.println("Incident not added. Check your longitude and latitude.");
        }
        
        tfAddName.setText("");
        tfAddDescription.setText("");
        tfAddLongitude.setText("");
        tfAddLatitude.setText("");
    }

    @FXML
    public void itemSelected() throws MalformedURLException, URISyntaxException, InterruptedException, IOException {
//        try{
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerGUI.fxml"));
//            ServerGUIController controller = fxmlLoader.<ServerGUIController>getController();
//            controller.setServer(admin.getServer(), (String) cbUnits.getSelectionModel().getSelectedItem());
//            Parent root1 = (Parent) fxmlLoader.load();
//            Stage stage = new Stage();
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.initStyle(StageStyle.UNDECORATED);
//            stage.setTitle("Chat");
//            stage.setScene(new Scene(root1));  
//            stage.show();
//          }
//        catch (IOException e) {
//            
//        }

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("ServerGUI.fxml"));
//        ServerGUIController controller = new ServerGUIController();
//        controller.setServer(admin.getServer(), (String) cbUnits.getSelectionModel().getSelectedItem());
//        loader.setController(controller);
//        loader.setRoot(controller);
//        Parent root;
//        try {
//            root = (Parent) loader.load();
//            Scene scene = new Scene(root);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException ex) {
//            System.out.println("Yo maat des nie best");
//        }
        //ServerGUIController controller = new ServerGUIController();
        //controller.openGui(admin.getServer(), (String) cbUnits.getSelectionModel().getSelectedItem());
        //controller.setServer(admin.getServer(), (String) cbUnits.getSelectionModel().getSelectedItem());
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
        if (change.wasAdded()) {
            cbUnits.getItems().add(change.getValueAdded().getNaam());
        } else if (change.wasRemoved()) {
            cbUnits.getItems().remove(change.getValueRemoved().getNaam());
        }
    }
    
    public void btnLogOut_Click() throws IOException
    {
        Stage currentstage = (Stage) btnLogOut.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Inloggen.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));  
        stage.show();  
        currentstage.close();
    }
}

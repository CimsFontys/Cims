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
import static pts4.klassen.Administration.EndedIncidents;
import static pts4.klassen.Administration.incidents;
import pts4.rssfeed.EnumProvinces;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

/**
 *
 * @author Max
 */
public class GUIController implements Initializable, MapChangeListener<String, Client> {

    private Administration admin;
    private Gmaps g;
    @FXML
    ComboBox cbincidentsimulation;
    @FXML
    ComboBox cbunitsimulation;
    @FXML
    Button btnSimulation;
    @FXML
    ListView lvActiveIncidents;
    @FXML
    ListView lvEndedIncidents;
    @FXML
    TextArea tfEndName;
    @FXML
    TextArea tfEndDescription;
    @FXML
    TextArea tfEndDetails;
    @FXML
    TextArea tfEndSolvedBy;
    @FXML
    Button btnEndIncident;
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
    ListView lvunitsactief;
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

        cbProvincies.getItems().setAll(EnumProvinces.values());
        cbProvincies.setValue(EnumProvinces.Nederland);
        cbExplosie.getItems().add("Yes");
        cbExplosie.getItems().add("No");
        cbBrand.getItems().add("Yes");
        cbBrand.getItems().add("No");
        cbGewelddadig.getItems().add("Yes");
        cbGewelddadig.getItems().add("No");
        cbSpoed.getItems().add("Yes");
        cbSpoed.getItems().add("No");
    }

    /**
     * Occurs once an active incident is selected.
     */
    public void goActiveIncident() {

        for (Incident a : admin.getIncidents()) {
            if (a.equals(lvActiveIncidents.getSelectionModel().getSelectedItem())) {

                tfEndName.setText(a.getName());
                tfEndDescription.setText(a.getDescription());
                tfEndDetails.setText("Explosion: " + a.getExplosion() + ", Fire: " + a.getFire() + ", Toxicity: " + a.getToxicity() + ", Urgent: " + a.getUrgent() + ", Violent: " + a.getViolent());
                btnEndIncident.setDisable(false);
                tfEndSolvedBy.setText(null);
            }
        }
    }
    
    /**
     * Checks if it's possible to start a chat session with the chosen unit. 
     * @param unit the unit to send a message to. 
     * @return if the chat screen been opened correctly it will return true, if not, false 
     * @throws IOException 
     */
    public boolean messageToUnit(String unit) throws IOException {
        try {
        URL location1 = getClass().getResource("ServerGUI.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location1);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = (Parent) (Node) fxmlLoader.load(location1.openStream());
                LogManager.getInstance().insertLog("A chat to communicate with Unit: " + unit + " has opened");
         
        ServerGUIController ctrl1 = (ServerGUIController) fxmlLoader.getController();
        ctrl1.setServer(admin.getServer(), unit);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //show the stage
        stage.showAndWait();
        return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    /**
     * Occurs once an incident is selected in the listView lvEndedIncidents 
     */
        public void goEndedIncident() {

        for (Incident a : admin.getEndedIncidents()) {
            if (a.equals(lvEndedIncidents.getSelectionModel().getSelectedItem())) {

                tfEndName.setText(a.getName());
                tfEndDescription.setText(a.getDescription());
                tfEndDetails.setText("Explosion: " + a.getExplosion() + ", Fire: " + a.getFire() + ", Toxicity: " + a.getToxicity() + ", Urgent: " + a.getUrgent() + ", Violent: " + a.getViolent());
                tfEndSolvedBy.setText(a.getSolvedBy());
                btnEndIncident.setDisable(true);
            }
        }
    }

    /**
     * Occurs once the incident has been accepted as solved. 
     */
    public void endIncident() {

        if (!tfEndSolvedBy.getText().isEmpty()) {
            for (Incident a : admin.getIncidents()) {
                if (a.equals(lvActiveIncidents.getSelectionModel().getSelectedItem())) {

                    a.setSolvedBy(tfEndSolvedBy.getText());
                    LogManager.getInstance().insertLog("Incident '" + a.getName() + "' has been solved by '" + a.getSolvedBy() + "'");
                    EndedIncidents.add(a);
                    incidents.remove(a);
                    g.DrawIncidents();
                    initComboboxes();
                }
            }
        } else {
            MessageBox msg = new MessageBox("Please specify how the incident was solved", MessageBoxType.OK_ONLY);
            msg.show();
        }
    }

    /**
     * Fills every ComboBox in this controller with the needed information.
     */
    public void initComboboxes() {

        //cbCategory.setItems(FXCollections.observableList(categorylist));
        cbunitsimulation.setItems(admin.getUnits());
        cbincidentsimulation.setItems(admin.getIncidents());
        cbUnit.setItems(admin.getUnits());
        cbincident.setItems(admin.getIncidents());
        lvIncidents.setItems(admin.getIncidents());
        lvIncidents2.setItems(admin.getIncidents());
        lvActiveIncidents.setItems(admin.getIncidents());
        lvEndedIncidents.setItems(admin.getEndedIncidents());
        lvunits.setItems(admin.getUnits());
        lvPendingIncidents.setItems(admin.getPendingIncidents());
        lvAcceptedIncidents.setItems(admin.getIncidents());

    }

    /**
     * Occurs when an incident is selected in the listView lvIncidents.
     */
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

    /**
     * Occurs when a selection is made in the cbProvincies ComboBox.
     * @throws MalformedURLException 
     */
    public void setProvincie() throws MalformedURLException {

        String selected = cbProvincies.getValue().toString();
        if (selected.equalsIgnoreCase("Nederland")) 
        {
            LogManager.getInstance().insertLog("User has loaded pending incidents from the whole country");
            admin.loadFromRSSFeed();
        } else {
            LogManager.getInstance().insertLog("User has loaded pending incidents from the province: " + selected);
            admin.loadFromRSSFeed(selected);
        }
        admin.syncAcceptedAndPending();
        lvPendingIncidents.setItems(admin.getPendingIncidents());
    }

    /**
     * Gives an order to the selected unit in the ComboBox cbUnit.
     */
    public void giveOrder() {
        g.createUnit = true;
        g.id = cbUnit.getSelectionModel().getSelectedItem().toString();
        g.incidentstring = cbincident.getSelectionModel().getSelectedItem().toString();
    }

    /**
     * Creates a simulation with input from the user.
     */
    public void createSimulation()
    {
       g.simulation = true;
       LogManager.getInstance().insertLog("Employee has started a simulation");
       g.id = cbunitsimulation.getSelectionModel().getSelectedItem().toString();
       g.incidentstring = cbincidentsimulation.getSelectionModel().getSelectedItem().toString();
    }
    
    /**
     * Occurs once an incident is selected in the listView lvIncidents2
     */
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

    /**
     * Occurs when a unit is selected on the Map.
     */
    public void selectUnit() {
        for (Unit a : admin.getUnits()) {
            if (a.equals(lvunits.getSelectionModel().getSelectedItem())) {
                g.goIncident(a.getLongitude(), a.getLatidude());
            }
        }
    }

    /**
     * Occurs once an incident is selected in the listView lvPendingIncidents
     */
    public void goPendingIncident() {

        for (Incident a : admin.getPendingIncidents()) {
            if (a.equals(lvPendingIncidents.getSelectionModel().getSelectedItem())) {

                taPendingName.setText(a.getName());
                taPendingDescription.setText(a.getDescription());
            }
        }
    }

    /**
     * Gets text from the TextArea txtDescription
     * @return a String containing the text from the TextArea txtDescription
     */
    public String getUnitDescription() {
        return txtDescription.getText();
    }

    /**
     * Gets text from the current selection at the ComboBox cbIncident
     * @return a String containing the text from the selected item in the cbIncident
     */
    public String getIncidentorder() {
        return cbincident.getSelectionModel().getSelectedItem().toString();
    }

   /**
    * Sets the long- and latitude for Units for using them in the GoogleMap
    * @param Longitude the longitude which needs to be set.
    * @param Latitude the latitude which needs to be set.
    */
    public void setUnit(double Longitude, double Latitude) {
        txtlongitude.setText(Double.toString(Longitude));
        txtlatitude.setText(Double.toString(Latitude));
    }

    /**
     * Accepts a pending incident so it can be used further in the application
     */
    public void acceptIncident() {

        for (Incident a : admin.getPendingIncidents()) {
            if (a.equals(lvPendingIncidents.getSelectionModel().getSelectedItem())) {

                admin.addIncident(a);
                LogManager.getInstance().insertLog("The incident: " + "'" +  a.getName() + "'" + " has been accepted");
                admin.removePendingIncident(a);
                taPendingName.setText("");
                taPendingDescription.setText("");
                g.addIncidentOnMap(a);
                this.initComboboxes();
            }
        }
    }

    /**
     * Adds a new incident with information from user input.
     */
    public void addIncident() {

        String name = tfAddName.getText();
        String description = tfAddDescription.getText();
        String longitude = tfAddLongitude.getText();
        String latitude = tfAddLatitude.getText();

        try {
            Incident incident = new Incident(longitude, latitude, name, description);
            admin.addIncident(incident);
            LogManager.getInstance().insertLog("The incident: " + "'" + incident.getName() + "'" + " has been added");

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

    /**
     * Occurs once a unit has been selected in the cbUnits ComboBox.
     * Starts a chatting session.
     * @throws MalformedURLException
     * @throws URISyntaxException
     * @throws InterruptedException
     * @throws IOException 
     */
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
        LogManager.getInstance().insertLog("The chat screen has been opened");
        Parent root = (Parent) (Node) fxmlLoader.load(location1.openStream());

        ServerGUIController ctrl1 = (ServerGUIController) fxmlLoader.getController();
        ctrl1.setServer(admin.getServer(), (String) cbUnits.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //show the stage
        stage.showAndWait();
    }

    /**
     * Occurs once the amount of units in cbUnits has changed.
     * @param change 
     */
    @Override
    public void onChanged(Change<? extends String, ? extends Client> change) {
        if (change.wasAdded()) {
            cbUnits.getItems().add(change.getValueAdded().getNaam());
        } else if (change.wasRemoved()) {
            cbUnits.getItems().remove(change.getValueRemoved().getNaam());
        }
    }
    
    
  /**
     * Logs out the currently logged in user.
     * @throws IOException 
     */
    public void btnLogOut_Click() throws IOException {
        Stage currentstage = (Stage) btnLogOut.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        LogManager.getInstance().insertLog("User has logged out");
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        currentstage.close();
        this.admin.getServer().getSt().stopServer();
        this.admin = null;
        
    
    }
}

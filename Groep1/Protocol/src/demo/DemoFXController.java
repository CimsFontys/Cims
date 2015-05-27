/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package demo;

import CommunicationClient.ComManager;
import CommunicationClient.MessageListener;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Michael
 */
public class DemoFXController implements Initializable, MessageListener
{
    private Parent root;
    private Scene scene;
    private Stage stage;
    private Scene currentscene;
    private Stage currentstage;
    
    private ComManager commManager = ComManager.getInstance();
    private List<String> messageOutput;
    private ObservableList<String> observable = FXCollections.observableArrayList();
    
    @FXML private ListView<String> listview_output;
    @FXML private TextField textfield_username;
    @FXML private TextField textfield_password;
    @FXML private Button button_login;
    @FXML private Button button_add;
    @FXML private Button button_calamities;
    @FXML private TextField textfield_register_persontype;
    @FXML private TextField textfield_register_firstname;
    @FXML private TextField textfield_register_middlename;
    @FXML private TextField textfield_register_lastname;
    @FXML private TextField textfield_register_username;
    @FXML private TextField textfield_register_password;
    @FXML private TextField textfield_register_SSN;
    @FXML private TextField textfield_register_email;
    @FXML private TextField textfield_register_phonenumber;
    @FXML private TextField textfield_register_street;
    @FXML private TextField textfield_register_city; 
    @FXML private TextField textfield_register_postal;
    @FXML private TextField textfield_register_region;
    @FXML private TextField textfield_register_configurator;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        commManager.addListener(this);
        listview_output = new ListView<String>(observable);
        messageOutput = new ArrayList<>();
    }
    
    public void login()
    {
        String username = textfield_username.getText();
        String password = textfield_password.getText();
        
        MessageBuilder mb = new MessageBuilder();
        Message message = mb.buildLoginMessage(username, password);
        commManager.sendMessage(message);
    }
    
    public void insertPerson()
    {
        String persontypeText = textfield_register_persontype.getText();
        int persontype = Integer.parseInt(persontypeText);
        String firstname = textfield_register_firstname.getText();
        String middlename = textfield_register_middlename.getText();
        String lastname = textfield_register_lastname.getText();
        String username = textfield_register_username.getText();
        String password = textfield_register_password.getText();
        String SSN = textfield_register_SSN.getText();
        String email = textfield_register_email.getText();
        String phone = textfield_register_phonenumber.getText();
        String street = textfield_register_street.getText();
        String city = textfield_register_city.getText();
        String postal = textfield_register_postal.getText();
        String region = textfield_register_region.getText();
        String configutor = textfield_register_configurator.getText();
        
        MessageBuilder mb = new MessageBuilder();
        Message message = mb.buildInsertPerson(persontype, firstname, lastname, middlename, username, password, SSN, email, null, phone, street, city, postal, region, configutor);
        commManager.sendMessage(message);
    }   
    
    public void allCalamities()
    {
        MessageBuilder mb = new MessageBuilder();
        Message message = mb.buildRetrieveAllCalamitiesMessage();
        commManager.sendMessage(message);
    }

    @Override
    public void proces(Message message) 
    {
        messageOutput.add(message.getType());
        messageOutput.add(message.getText());
        showOutput();
        
        System.out.println("MessageType: " +message.getType());
        System.out.println("ServerReply: " + message.getText());
    }
    
    public void showOutput()
    {
        observable.addAll(messageOutput);
        listview_output.setItems(observable);
    }   
    
}

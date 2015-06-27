/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import chat.AudioMessage;
import chat.ChatMessage;
import ClientApp.Administratie;
import CommunicationClient.ComManager;
import CommunicationClient.LogManager;
import Protocol.Message;
import Protocol.MessageBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * FXML Controller class
 *
 * @author pieter
 */
public class ClienttestGUIController implements Initializable 
{
    @FXML private TextArea input;
    @FXML private ListView output;
    @FXML private ComboBox cbBeschikbareUnits;
    @FXML private Label lbGebruikersnaam;
    @FXML private Label lbRecordTimer;
    @FXML private Button btnRecord;
    
    private Administratie admin;
    private Timer timer;
    private boolean pressed;
    private int teller;
    private boolean audiomessage;
    private LogManager logman;
    private ComManager comManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        admin = Administratie.getInstance(); 
        logman = LogManager.getInstance();
        comManager = ComManager.getInstance();
    }
    /**
     * triggerd when a user clicks on a message in the chat box. 
     * If it is a audiomessage it plays the audio.
     * @param arg0
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException 
     */
    @FXML
    public void outputItem_Click(MouseEvent arg0) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        ChatMessage message = (ChatMessage) output.getSelectionModel().getSelectedItem();
        if(message instanceof AudioMessage)
        {
            AudioMessage audmessage = (AudioMessage) message;
            logman.insertLog("Log: Audiomessage listend by " + logman.getPersonId());
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(audmessage.getAudiopath()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }        
    }
    /**
     * retrives the selected unit from the combobox and sets a new reciever.
     * @param event 
     */
    @FXML
    private void selectUnit(ActionEvent event)
    {
        String naam = (String) cbBeschikbareUnits.getSelectionModel().getSelectedItem();
        admin.setReciever(naam);
        
        //HIER ID OPHALEN
        MessageBuilder mb = new MessageBuilder();
        Message retrieve = mb.buildRetrievePersonIdFromName(naam);
        comManager.addMessage(retrieve);
        
        addItemListView("[Algemeen]: je stuurt nu berichten naar: " + naam);
        logman.insertLog("Log: Communicator set by" + logman.getPersonId() + "to " + naam); // log in the database.
    }
    /**
     * gets the text from the input textarea and sends calls the methods to 
     * send it to the current reciever.
     * @param event 
     */
    @FXML
    private void btnSend_Click(ActionEvent event) 
    {
       if(!audiomessage)
       {
           String message = input.getText();
           input.clear();
           admin.sendMessage(message);
           logman.insertLog("Log: chatmessage send by " + logman.getPersonId() + " to: " + admin.getCc().getReciever()); // log in the database
       }
       else
       {
           input.clear();
           admin.sendAudioMessage();
           logman.insertLog("Log: audiomessage send by: " + logman.getPersonId() + " to: " + admin.getCc().getReciever()); // log in the database.
           audiomessage = false;
       }
    }
    /**
     * starts the recording process for an audiomessage so that it can be send.
     * @param event 
     */
    @FXML
    private void btnRecording_Click(ActionEvent event)
    {
        if(!pressed)
        {
            pressed = true;
            btnRecord.setText("Klik om te stoppen met opnemen");
            admin.startRecordingAudio();
            System.out.println("audio opnemen gestart");
            startTimer();
            logman.insertLog("Log: Started recording audiomessage " + logman.getPersonId());
        }
        else if(pressed)
        {
            pressed = false;
            btnRecord.setText("Neem nieuw audiobericht op");
            admin.stopRecordingAudio();
            System.out.println("audio opnemen gestopt");
            timer.purge();
            timer.cancel();
            teller = 0;
            input.setText("druk op send om het audiobericht te versturen");
            logman.insertLog("Log: Stopped recording audiomessage " + logman.getPersonId());
            lbRecordTimer.setText(String.valueOf(teller));
            audiomessage = true;
        }
    }
    /**
     * starts a timer in the gui so that the user can see how long the user 
     * is recording.
     */
    private void startTimer()
    {
        timer = new Timer();
        TimerTask task = new TimerTask() 
        {
            @Override
            public void run() 
            {
                Platform.runLater(new Runnable() 
                {
                    @Override
                    public void run() 
                    {
                        lbRecordTimer.setText(String.valueOf(teller));
                        teller++;
                    }
                });
            }
        };
        timer.schedule(task, 0 , 1000);
    }
    /**
     * adds an informationstring to the listview.
     * @param item 
     */
    public void addItemListView(String item)
    {
        Platform.runLater(new Runnable() {

            @Override
            public void run() 
            {
                output.getItems().add(item);            
            }
        });
    }
    /**
     * sets logged in user for this gui and  starts the communication protocol.
     * @param user the name of the user that is logged in.
     */
    public void setUser(String user)
    {
        admin.setChatClient(user);
        admin.setReciever("Meldkamer");
        lbGebruikersnaam.setText(user);
        cbBeschikbareUnits.setItems(admin.getCc().getObservableClients());
        addItemListView("[Algemeen]: je coummuniceerd nu met de meldkamer");
        output.setItems(admin.getCc().getObservableMessages());
    }
}

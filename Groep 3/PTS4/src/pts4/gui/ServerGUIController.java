/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.gui;

import chat.AudioMessage;
import chat.ChatMessage;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import pts4.chatserver.Server;
import pts4.klassen.LogManager;


/**
 * FXML Controller class
 *
 * @author pieter
 */
public class ServerGUIController extends AnchorPane implements Initializable {

    /**
     * Initializes the controller class.
     * 
     */
    @FXML TextArea input;
    @FXML ListView OutPut;
    @FXML ComboBox present;
    @FXML Button btnRecordNew;
    @FXML Label lbTeller;
    
    private Server server;
    private String communicator="";
    private boolean pressed;
    private Timer timer;
    private int counter;
    private boolean audiomessage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    /**
     * Occurs once the btnChat button is pressed. Will send a message depending on it's type. 
     */
    @FXML
    public void btnChat_Click()
    {
        if(!audiomessage)
        {
            String message = input.getText();
            input.clear();
            server.sendMessage(new ChatMessage(message, "Meldkamer", communicator));
            LogManager.getInstance().insertLog("Sent chat message: " + "" + message + "");
        }
        else
        {
            input.clear();
            server.sendAudioMessage(communicator);
            LogManager.getInstance().insertLog("An audio message has been sent!");
            audiomessage = false;
        }
    }
    
    /**
     * Opens an audio clip if the selected chat message is one. 
     * @param arg0
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException 
     */
    @FXML
    public void outputItem_Click(MouseEvent arg0) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        ChatMessage message = (ChatMessage) OutPut.getSelectionModel().getSelectedItem();
        if(message instanceof AudioMessage)
        {
            AudioMessage audmessage = (AudioMessage) message;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(audmessage.getAudiopath()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }        
    }
    
    /**
     * Starts the timer to for recording a new audio message
     */
    public void btnRecordNew_Click()
    {
        if(!pressed)
        {
            pressed = true;
            btnRecordNew.setText("Click to stop recording");
            server.startRecrding();
            startTimer();
            LogManager.getInstance().insertLog("Audio recording has started");
        }
        else if(pressed)
        {
            pressed = false;
            btnRecordNew.setText("Record new audiomessage");
            server.stopRecording();
            LogManager.getInstance().insertLog("Audio recording has stopped");

            // reset de timer
            timer.purge();
            timer.cancel();
            counter = 0;
            input.setText("Click send to send audiomessage");
            lbTeller.setText(String.valueOf(counter));
            audiomessage = true;
        }
    }
    
    /**
     * Timer for audio messages.
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
                        lbTeller.setText(String.valueOf(counter));
                        counter++;
                    }
                });
            }
        };
        timer.schedule(task, 0 , 1000);
    }
    
    /**
     * Adds an item(chat message) to the output listView
     * @param item a text- or audio message that has been received.
     */
    public void AddItemListview(String item)
    {
        Platform.runLater(new Runnable() {

            @Override
            public void run() 
            {
                OutPut.getItems().add(item);            
            }
        });
    }
    
    /**
     * Serves as configuration for chatting with units.
     * @param s the server used for communications.
     * @param communicator the unit which the server wants to communicate with.
     */
    public void setServer(Server s, String communicator)
    {
        this.server = s;
        this.communicator = communicator;
        this.OutPut.setItems(server.getClient(communicator).getMessages());
    }
}

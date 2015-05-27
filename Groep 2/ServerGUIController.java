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
    private int teller;
    private boolean audiomessage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    public void btnChat_Click()
    {
        if(!audiomessage)
        {
            String bericht = input.getText();
            input.clear();
            server.sendMessage(new ChatMessage(bericht, "Meldkamer", communicator));
        }
        else
        {
            input.clear();
            server.sendAudioMessage(communicator);
            audiomessage = false;
        }
    }
    
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
    
    public void btnRecordNew_Click()
    {
        if(!pressed)
        {
            pressed = true;
            btnRecordNew.setText("Klik om te stoppen met opnemen");
            server.startRecrding();
            startTimer();
        }
        else if(pressed)
        {
            pressed = false;
            btnRecordNew.setText("Neem nieuw audiobericht op");
            server.stopRecording();
            // reset de timer
            timer.purge();
            timer.cancel();
            teller = 0;
            input.setText("Druk op send om het audiobericht te versturen");
            lbTeller.setText(String.valueOf(teller));
            audiomessage = true;
        }
    }
    
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
                        lbTeller.setText(String.valueOf(teller));
                        teller++;
                    }
                });
            }
        };
        timer.schedule(task, 0 , 1000);
    }
    
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
    
    public void setServer(Server s, String communicator)
    {
        this.server = s;
        this.communicator = communicator;
        this.OutPut.setItems(server.getClient(communicator).getMessages());
    }
}

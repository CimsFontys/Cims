/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientApp;

import chat.AudioMessage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 * class that writes the audiomessage to a file.
 * @author Leo
 */
public class WriteFileThread implements Runnable
{
    private AudioMessage message;
    private SimpleDateFormat sdfDate;
    private ChatClient cc;
    
    public WriteFileThread(AudioMessage message, ChatClient cc)
    {
        this.message = message;
        sdfDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss.SSS");;//dd/MM/yyyy
        this.cc = cc;
    }
    /**
     * gets the systemtime as string in dd-MM-yyyy HH.mm.ss.SSS format.
     * @return the systemtime as string.
     */
    private String getSystemTimeAsString()
    {
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    /**
     * inititialezes a new file to write te recieved audio to and adds it to the observable messages list in the chatclient class.
     */
    @Override
    public void run() 
    {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try 
        {
            String time = getSystemTimeAsString();
            System.out.println(time);
            File file = new File("Opnames\\" + message.getAfzender());
            if(!file.exists())
            {
                file.mkdir();
            }
            String path = "Opnames\\" + message.getAfzender() + "\\" + "opname " + message.getAfzender() + " " + time + ".wav";
            fos = new FileOutputStream(new File(path));
            bos = new BufferedOutputStream(fos); // use a bufferedoutputstream because it is faster.
            bos.write(message.getAudiofile());
            message.setAudiopath(path);
            message.setAudiofile(null);
            cc.addMessage(message);
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(WriteFileThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) 
        {
            Logger.getLogger(WriteFileThread.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
            try 
            {
                fos.close();
                bos.close();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(WriteFileThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}

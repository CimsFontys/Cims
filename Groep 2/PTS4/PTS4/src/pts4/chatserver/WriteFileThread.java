/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.chatserver;

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

/**
 *
 * @author Leo
 */
public class WriteFileThread implements Runnable
{
    private AudioMessage message;
    private SimpleDateFormat sdfDate;
    private Client client;
    
    public WriteFileThread(AudioMessage message, Client c)
    {
        this.message = message;
        sdfDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss.SSS");;//dd/MM/yyyy
        this.client = c;
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
            String tijd = getSystemTimeAsString();
            System.out.println(tijd);
            String path = "Opnames\\" + message.getAfzender() + "\\" + "opname " + message.getAfzender() + " " + tijd + ".wav";
            fos = new FileOutputStream(new File(path));
            bos = new BufferedOutputStream(fos);
            bos.write(message.getAudiofile());
            message.setAudiopath(path);
            message.setAudiofile(null);
            client.addMessageToObservable(message);
            
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

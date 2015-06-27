/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Audio.AudioHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author Leo
 */
public class RecordingThread implements Callable<byte[]>
{
    private TargetDataLine line;
    private AudioFileFormat.Type targetType;
    private AudioInputStream audioInputStream;
    private File outputFile;
    
    public RecordingThread(TargetDataLine line, AudioFileFormat.Type targetType, File file)
    {
        this.line = line;
        this.targetType = targetType;
        this.outputFile = file;
        this.audioInputStream = new AudioInputStream(line);
    }
    
    public void stopRecording()
    {
        line.stop();
        line.close();
    }

    @Override
    public byte[] call() throws Exception 
    {
        byte[] audiofile  = null;
        try 
        {
           // schrijf de opgenomen audio naar de meegegeven file.
            line.start();
            System.out.println("ik begin met schrijven");
            AudioSystem.write(audioInputStream, targetType, outputFile);
            System.out.println("ik stop met schrijven");
            System.out.println("Naar de byte[]");
            audiofile = Files.readAllBytes(outputFile.toPath());
            System.out.println("En daar voorbij");
                                   
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(RecordingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return audiofile;
    }
}

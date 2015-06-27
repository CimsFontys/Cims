/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author Leo
 */
public class AudioHandler 
{
    private RecordingThread recorder;   
    private File outputFile;
    private TargetDataLine targetLine;
    private AudioFileFormat.Type targetType;
    private Thread opnameThread;
    private AudioFormat audioFormat;
    private String path;
    private byte[] audiofile;
    private ExecutorService threadpool;
    private Future<byte[]> fut;
    private DataLine.Info info;
    private SimpleDateFormat sdfDate; 
    
    public AudioHandler()
    {
        //creerer een format voor de dataline om uit te lezen.
        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100.0F, 16, 2, 4, 44100.0F, false);
        //maak correcte info voor de dataline zodat deze weet hoe en wat het moet lezen. 
        info = new DataLine.Info(TargetDataLine.class, audioFormat);
        sdfDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss.SSS");;//dd/MM/yyyy
        outputFile = new File("Opnames");
        if(!outputFile.exists())
        {
            outputFile.mkdir();
        }
        threadpool = Executors.newFixedThreadPool(1);
    }

    public String getPath() 
    {
        return path;
    }    
    
    private String getSystemTimeAsString()
    {
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public void setAudiofile(byte[] audiofile) 
    {
        this.audiofile = audiofile;
    }

    public byte[] getAudiofile() 
    {
        return audiofile;
    }
    
    public void startRecording()
    {
        path = "Opnames/opname " + getSystemTimeAsString() + ".wav";
        outputFile = new File(path);
        initFile();
        recorder = new RecordingThread(targetLine, targetType, outputFile);
        fut = threadpool.submit(recorder);
    }
    
    public void stopRecording()
    {
        recorder.stopRecording();
        while(!fut.isDone())
        {
            // wacht tot de thread klaar is.
        }
        try 
        {
            setAudiofile(fut.get());
        } 
        catch (InterruptedException ex) 
        {
            Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ExecutionException ex) 
        {
            Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initFile()
    {
        targetLine = null;
        try 
        {
            //open de audiolijn zodat er kan worden opgenomen.
            targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open(audioFormat);
        } 
        catch (LineUnavailableException ex) 
        {
            Logger.getLogger(AudioHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* Again for simplicity, we've hardcoded the audio filetype, too.*/
        targetType = AudioFileFormat.Type.WAVE;
    }
}

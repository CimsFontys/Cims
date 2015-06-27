/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

/**
 *
 * @author Leo
 */
public class AudioMessage extends ChatMessage
{
    private byte[] audiofile;
    private String audiopath;
    
    public AudioMessage(String bericht, String afzender, String ontvanger, byte[] audiofile) 
    {
        super(bericht, afzender, ontvanger);
        this.audiofile = audiofile;
    }

    public byte[] getAudiofile() 
    {
        return audiofile;
    }
    
    public String getAudiopath() 
    {
        return audiopath;
    }

    public void setAudiopath(String audiopath) 
    {
        this.audiopath = audiopath;
    }
    
    public void setAudiofile(byte[] audiofile) 
    {
        this.audiofile = audiofile;
    }
}
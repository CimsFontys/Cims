package Protocol;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Merijn
 */
public class Message implements Serializable {
    
    String text;
    byte[] file;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public Message()
    {
        
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    public void setFile(byte[] file)
    {
        this.file = file;
    }
    
    public String getText()
    {
        return this.text;
    }
    
    public byte[] getFile()
    {
        return this.file;
    }    
}

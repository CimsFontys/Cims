/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.Serializable;

/**
 *
 * @author Leo
 */
public class Message implements Serializable 
{
    private static final long serialVersionUID = -7480552314589874590L;
    private String bericht;

    
    private String afzender;
    private String ontvanger;
    
    public Message(String bericht, String afzender, String ontvanger)
    {
        this.bericht = bericht;
        this.afzender = afzender;
        this.ontvanger = ontvanger;
    }

    public String getOntvanger() 
    {
        return ontvanger;
    }
    
    public String getBericht() 
    {
        return bericht;
    }
    
    @Override
    public String toString()
    {
        return "[" + afzender + "]: " + bericht;
    }
}

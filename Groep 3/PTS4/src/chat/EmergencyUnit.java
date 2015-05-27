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
public class EmergencyUnit implements Serializable
{
    private String naam;
    private int type;
    private double longitude;
    private double latidude;
    
    public EmergencyUnit(String naam, int type)
    {
        this.naam = naam;
        this.type = type;
        this.longitude = longitude;
        this.latidude = latidude;       
    }

    public String getNaam() 
    {
        return naam;
    }

    public int getType() 
    {
        return type;
    }

    public double getLongitude() 
    {
        return longitude;
    }

    public double getLatidude() 
    {
        return latidude;
    }

    public void setLongitude(double longitude) 
    {
        this.longitude = longitude;
    }

    public void setLatidude(double latidude) 
    {
        this.latidude = latidude;
    }
}
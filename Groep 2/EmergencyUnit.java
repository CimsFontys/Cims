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
    private long serialVersionUID = 9212792848339440618L;
    private String naam;
    private int type;
    private int personid;
    private double longitude;
    private double latidude;
    
    public EmergencyUnit(String naam, int type, int personid)
    {
        this.naam = naam;
        this.type = type;     
        this.personid = personid;
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

    public int getPersonid() 
    {
        return personid;
    }

    public void setLongitude(double longitude) 
    {
        this.longitude = longitude;
    }

    public void setLatidude(double latidude) 
    {
        this.latidude = latidude;
    }
    
    @Override
    public String toString() 
    {
        return this.naam;
    }
}
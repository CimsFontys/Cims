/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.klassen;

import java.io.Serializable;

/**
 *
 * @author Leo
 */
public class Unit
{
    
    private String naam;
    private int type;
    private double longitude;
    private double latitude;
    private Incident incident;
    
    
    public Unit(String naam, int type, double longitude, double latitude)
    {
        this.naam = naam;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;       
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
        return latitude;
    }

    public void setLongitude(double longitude) 
    {
        this.longitude = longitude;
    }

    public void setLatidude(double latidude) 
    {
        this.latitude = latidude;
    }
    
    public void setIncident(Incident incident)
    {
        this.incident = incident;
    }
    
    public Incident getIncident()
    {
        return this.incident;
    }
    
    @Override
    public String toString() {
        return this.naam;
    }
}
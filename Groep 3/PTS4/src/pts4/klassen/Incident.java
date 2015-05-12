package pts4.klassen;

import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Max
 */
public class Incident {
    
    private String name;
    private String description;
    private String longitude;
    private String latitude;
    private int id;
    private Date date;
    
    public Incident(String longitude, String latitude, String name, String description)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.description = description;
    }
    public Incident(int Id, String longitude, String latitude, String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = Id;
        this.date = date;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getLongitude() {
        return this.longitude;
    }
    
    public String getLatitude() {
        return this.latitude;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public Date getDate()
    {
        return this.date;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}

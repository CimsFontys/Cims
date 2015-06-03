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
    private String toxicsubstances;
    private String explosiondanger;
    private String fire;
    private String numbercasualties;
    private String violent;
    private String urgent;
    private int id;
    private Date date;
    private String solvedBy;
    
    public Incident(String longitude, String latitude, String name, String description)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.description = description;
    }
    
    public Incident(String longitude, String latitude, String name, String description, String toxicSubstances, String explosionDanger, String fire, String numberCasualties, String violent, String urgent, int id, Date date)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.description = description;
        this.toxicsubstances = toxicSubstances;
        this.explosiondanger = explosionDanger;
        this.fire = fire;
        this.numbercasualties = numberCasualties;
        this.violent = violent;
        this.urgent = urgent;
        this.id = id;
        this.date = date;
    }
    
    
    // Voor de databasee
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
    
    public String getToxicity() {
        return this.toxicsubstances;
    }
    
    public String getExplosion()
    {
        return this.explosiondanger;
    }
    
    public String getFire()
    {
        return this.fire;
    }
    
    public String getCasualties()
    {
        return this.numbercasualties;
    }
    
    public String getViolent()
    {
        return this.violent;
    }
    
    public String getUrgent()
    {
        return this.urgent;
    }
    
    public void setSolvedBy(String solvedBy) {
        this.solvedBy = solvedBy;
    }
    
    public String getSolvedBy()
    {
        return this.solvedBy;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}

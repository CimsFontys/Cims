package pts4.klassen;

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
    private long longitude;
    private long latitude;
    
    
    public Incident(String name, String description, long longitude, long latitude) {
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public long getLongitude() {
        return this.longitude;
    }
    
    public long getLatitude() {
        return this.latitude;
    }
}

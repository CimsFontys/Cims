package pts4.googlemaps;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Max
 */
public class Unit {
    
    private String name;
    private String description;
    private String type;
    private double longitude;
    private double latitude;
    
    
    public Unit(String name, String description, String type, double longitude, double latitude) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getType()
    {
        return this.type;
    }
    
    public double getLongitude()
    {
        return this.longitude;
    }
    
    public double getLatitude()
    {
        return this.latitude;
    }
}

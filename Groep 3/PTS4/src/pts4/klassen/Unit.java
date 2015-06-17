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
public class Unit {

    private String name;
    private int type;
    private double longitude;
    private double latitude;
    private String incident;
    private Boolean active;

    public Unit(String name, int type, double longitude, double latitude) {
        this.name = name;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.active = false;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatidude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatidude(double latidude) {
        this.latitude = latidude;
    }

    public void setIncident(String incident) {
        this.incident = incident;
    }

    public String getIncident() {
        return this.incident;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.googlemaps;

import chat.EmergencyUnit;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import static javafx.util.Duration.seconds;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import static pts4.klassen.Administration.EmergencyUnits;
import pts4.klassen.Unit;

/**
 *
 * @author Sander
 */
public class Animation {

    private Timer timer;
    private GeoPosition goal, unit;
    private Set<MyWaypoint> units, orders;
    private String id;
    private Gmaps gmap;
    private JXMapViewer map;
    private double longitude, latitude, diflongitude, diflatitude;
    private WaypointPainter<MyWaypoint> waypointpainter;

    public Animation(GeoPosition goal, GeoPosition unit, String id, Set<MyWaypoint> orders, Set<MyWaypoint> units, Gmaps gmap, WaypointPainter<MyWaypoint> waypointpainter) {
        this.goal = goal;
        this.unit = unit;
        this.orders = orders;
        this.units = units;
        this.id = id;
        this.gmap = gmap;
        this.longitude = unit.getLongitude();
        this.latitude = unit.getLatitude();
        this.waypointpainter = waypointpainter;
        timer = new Timer();
        timer.schedule(new CrunchifyReminder(), 0, // initial delay
                1 * 1000); // subsequent rate
    }

    class CrunchifyReminder extends TimerTask {

        public void run() {
            for (MyWaypoint p : units) {
                if (p.getLabel().equals(id)) {
                    if (longitude < goal.getLongitude()) {
                        diflongitude = goal.getLongitude() - longitude;
                    }
                    if (longitude > goal.getLongitude()) {
                        diflongitude = longitude - goal.getLongitude();
                    }
                    if (latitude < goal.getLatitude()) {
                        diflatitude = goal.getLatitude() - latitude;
                    }
                    if (latitude > goal.getLatitude()) {
                        diflatitude = latitude - goal.getLatitude();
                    }

                    if (longitude > goal.getLongitude()) {
                        longitude = longitude - 0.001;
                    }
                    if (longitude < goal.getLongitude()) {
                        longitude = longitude + 0.001;
                    }
                    if (latitude > goal.getLatitude()) {
                        latitude = latitude - 0.001;
                    }
                    if (latitude < goal.getLatitude()) {
                        latitude = latitude + 0.001;
                    }
                    if (diflongitude < 0.001 && diflatitude < 0.001) {
                        for (MyWaypoint d : orders) {
                            if (d.getLabel().equals(id)) {
                                orders.remove(d);
                                waypointpainter.setWaypoints(orders);
                                for (Unit e : EmergencyUnits) {
                                    if (e.getNaam().equals(id)) {
                                        e.setLongitude(longitude);
                                        e.setLatidude(latitude);

                                    }
                                }
                                gmap.drawRoute();
                            }
                        }
                        //gmap.sendMessage(id + "Is op locatie gearriveerd", id, "Meldkamer");
                        timer.cancel();
                    }

                    GeoPosition bla = new GeoPosition(latitude, longitude);
                    List<GeoPosition> track = Arrays.asList(goal, bla);

                    gmap.routepainter = new RoutePainter(track);
                    gmap.painters.add(gmap.routepainter);
                    p.setPosition(bla);
                    gmap.drawRoute();
                    gmap.painters.remove(gmap.routepainter);
                }
            }
        }
    }
}

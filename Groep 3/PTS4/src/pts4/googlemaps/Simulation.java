/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.googlemaps;

import chat.EmergencyUnit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
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
public class Simulation implements Painter<JXMapViewer> {

    private Timer timer;
    private GeoPosition unit;
    private GeoPosition fire;
    private Set<MyWaypoint> units, orders;
    private String id;
    private Gmaps gmap;
    private JXMapViewer map;
    private double longitude, latitude, diflongitude, diflatitude;
    private WaypointPainter<MyWaypoint> waypointpainter;
    private boolean bla = false;
    private boolean antiAlias = true;
    private int lenght;
    private int height;
    private Color color;
    private Boolean arrived;

    public Simulation(GeoPosition fire, GeoPosition unit, String id, Set<MyWaypoint> orders, Set<MyWaypoint> units, Gmaps gmap, WaypointPainter<MyWaypoint> waypointpainter) {
        this.fire = fire;
        this.unit = unit;
        this.orders = orders;
        this.units = units;
        this.id = id;
        this.gmap = gmap;
        this.longitude = unit.getLongitude();
        this.latitude = unit.getLatitude();
        this.waypointpainter = waypointpainter;
        this.map = gmap.returnFrame();
        this.lenght = 10;
        this.height = 10;
        this.color = Color.red;
        this.arrived = false;
    }

    public void unitArrived() {
        this.color = Color.green;
        this.arrived = true;
    }

    @Override
    public void paint(Graphics2D gd, JXMapViewer t, int w, int h) {
        t = map;
        gd = (Graphics2D) gd.create();
        gd.setColor(this.color);
        Rectangle rect = map.getViewportBounds();
        gd.translate(-rect.x, -rect.y);
        // convert from viewport to world bitmap
        if (antiAlias) {
            gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            if (!arrived) {
                                lenght = (int) (lenght + 0.5);
                                height = (int) (height + 0.5);
                            } else {
                                lenght = (int) (lenght - 3);
                                height = (int) (height - 3);
                            }
                            if (lenght <= 0)
                            {                               
                                gmap.draw();
                                timer.cancel();
                            }
                            gmap.draw();
                        }
                    });
                }
            }, 2000, 2000);
        }
        if(!arrived)
        {
        int X;
        int Y;
        Point2D pt = map.getTileFactory().geoToPixel(fire, map.getZoom());
        X = (int) pt.getX();
        Y = (int) pt.getY();
        gd.fillOval(X - (lenght / 2), Y - (lenght / 2), lenght, height);
        }
        gd.dispose();
        
    }

}

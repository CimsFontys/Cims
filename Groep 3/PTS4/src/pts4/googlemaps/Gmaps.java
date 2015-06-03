/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.googlemaps;

import chat.ChatMessage;
import chat.EmergencyUnit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.list;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingNode;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MouseInputListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.LocalResponseCache;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import pts4.chatserver.Server;
import pts4.klassen.Administration;
import static pts4.klassen.Administration.incidents;
import pts4.klassen.Incident;
import pts4.gui.GUIController;
import static pts4.klassen.Administration.EmergencyUnits;
import pts4.klassen.Unit;

/**
 *
 * @author Sander
 */
public class Gmaps {

    private JList list = new JList();
    private JXMapViewer mapViewer = new JXMapViewer();
    private JFrame frame = new JFrame("Google Maps");
    private Set<Waypoint> waypoints;
    private Set<MyWaypoint> units;
    private Set<MyWaypoint> orders;
    private WaypointPainter<Waypoint> waypointPainter;
    private WaypointPainter<MyWaypoint> waypointPainter2, waypointPainter3;
    public RoutePainter routepainter;
    public Boolean unitAanmaak = false;
    public String id;
    public int type;
    private GUIController gui;
    private Timeline timeline;
    private AnimationTimer timer;
    private Integer i = 0;
    public List<Painter<JXMapViewer>> painters;
    private Server server;

    public Gmaps(GUIController gui, Server server) {
        this.gui = gui;
        this.server = server;
    }

    public void open() {
        // Create a TileFactoryInfo for Virtual Earth
        TileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        LocalResponseCache.installResponseCache(info.getBaseURL(), cacheDir, false);

        // Setup JXMapViewer
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        //Calimiteiten toevoegen.
        waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter2 = new WaypointPainter<MyWaypoint>();
        waypointPainter3 = new WaypointPainter<MyWaypoint>();
        waypoints = new HashSet<Waypoint>();
        units = new HashSet<MyWaypoint>();
        orders = new HashSet<MyWaypoint>();
        painters = new ArrayList<Painter<JXMapViewer>>();

        for (Incident object : incidents) {
            GeoPosition plek = new GeoPosition(Double.parseDouble(object.getLatitude()), Double.parseDouble(object.getLongitude()));
            waypoints.add(new DefaultWaypoint(plek));
            // Create a waypoint painter that takes all the waypoints
        }
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);
        for (Unit a : EmergencyUnits) {
            GeoPosition spot = new GeoPosition(a.getLatidude(), a.getLongitude());
            Color color = null;
            if (a.getType() == 1) {
                color = Color.cyan;
            }
            if (a.getType() == 2) {
                color = Color.YELLOW;
            }
            if (a.getType() == 3) {
                color = Color.RED;
            }
            units.add(new MyWaypoint(a.getNaam(), color, spot));
            // Create a waypoint painter that takes all the waypoints
            waypointPainter2.setWaypoints(units);
            waypointPainter2.setRenderer(new FancyWaypointRenderer());
            // Create a waypoint painter that takes all the waypoints
        }
        createStage();
        teken();

        /*// Create waypoints from the geo-positions
         Set<MyWaypoint> waypoints = new HashSet<MyWaypoint>(Arrays.asList(
         new MyWaypoint("F", Color.ORANGE, Utrecht)));

         // Create a waypoint painter that takes all the waypoints
         WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<MyWaypoint>();
         waypointPainter.setWaypoints(waypoints);
         waypointPainter.setRenderer(new FancyWaypointRenderer());

         mapViewer.setOverlayPainter(waypointPainter);*/
        // Display the viewer in a JFrame
    }

    /*public void addUnitOnMap(EmergencyUnit a) {
     GeoPosition spot = new GeoPosition(a.getLatidude(), a.getLongitude());
     Color color = null;
     if (a.getType() == 1) {
     color = Color.cyan;
     }
     if (a.getType() == 2) {
     color = Color.YELLOW;
     }
     if (a.getType() == 3) {
     color = Color.RED;
     }
     units.add(new MyWaypoint(a.getNaam(), color, spot));
     // Create a waypoint painter that takes all the waypoints
     waypointPainter2.setWaypoints(units);
     waypointPainter2.setRenderer(new FancyWaypointRenderer());
     teken();
     }*/
    public void addIncidentOnMap(Incident a) {
        GeoPosition plek = new GeoPosition(Double.parseDouble(a.getLatitude()), Double.parseDouble(a.getLongitude()));
        waypoints.add(new DefaultWaypoint(plek));
        waypointPainter.setWaypoints(waypoints);
        teken();
    }

    public void teken() {
        // Create a compound painter that uses both the route-painter and the waypoint-painter

        painters.add(waypointPainter2);
        painters.add(waypointPainter);
        painters.add(waypointPainter3);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
        
    }

    public void tekenRoute() {

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        painters.add(waypointPainter2);
        painters.add(waypointPainter);
        painters.add(waypointPainter3);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
 
    }

    public void createStage() {

        GeoPosition Utrecht = new GeoPosition(52.0907370, 5.1214200);
        /*// Create a waypoint painter that takes all the waypoints
         WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
         waypointPainter.setWaypoints(waypoints);*/
        // Set the focus
        mapViewer.setZoom(10);
        mapViewer.setAddressLocation(Utrecht);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        mapViewer.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if (unitAanmaak == true) {
                    Color kleur = null;
                    if (type == 1) {
                        kleur = Color.cyan;

                    }
                    if (type == 2) {
                        kleur = Color.YELLOW;

                    }
                    if (type == 3) {
                        kleur = Color.RED;

                    }

                    GeoPosition plek = mapViewer.convertPointToGeoPosition(me.getPoint());
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            gui.setUnit(plek.getLongitude(), plek.getLatitude());
                        }
                    });

                    for (MyWaypoint p : orders) {
                        if (p.getLabel().equals(id)) {
                            orders.remove(p);
                        }
                    }
                    orders.add(new MyWaypoint(id, kleur, plek));
                    for (Unit a : EmergencyUnits) {
                        if (a.getNaam().equals(id)) {
                            GeoPosition plek2 = new GeoPosition(a.getLatidude(), a.getLongitude());
                            ChatMessage chat = new ChatMessage(gui.getIncidentorder() + "\n" + gui.getUnitDescription(), id, "Meldkamer");
                            //server.sendMessage(chat);
                            new Animation(plek, plek2, id, orders, units, Gmaps.this, waypointPainter3);
                        }
                    }

                    unitAanmaak = false;
                    // Create a waypoint painter that takes all the waypoints
                    
                    waypointPainter3.setWaypoints(orders);
                    waypointPainter3.setRenderer(new FancyWaypointRenderer());
                    teken();

                    //GeoPosition frankfurt = new GeoPosition(mapViewer.convertPointToGeoPosition(me.getLocationOnScreen()));
                }
            }
        }); // end MouseAdapter

    }

    public void Combobox() {
        /*JPanel panel = new JPanel();
         panel.setLayout(new GridLayout());
         String[] labels = new String[2];
         labels[0] = "Units";
         labels[1] = "Calimiteiten";
         final JComboBox combo = new JComboBox(labels);
         combo.setSize(5, 20);
         panel.add(combo);
         frame.add(panel, BorderLayout.NORTH);*/
    }

    public JXMapViewer returnFrame() {
        return this.mapViewer;
    }

    public void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                open();
                swingNode.setContent(mapViewer);
            }
        });
    }

    public void goIncident(double longitude, double latitude) {
        mapViewer.setZoom(4);
        GeoPosition huidige = new GeoPosition(latitude, longitude);
        mapViewer.setAddressLocation(huidige);
    }

}

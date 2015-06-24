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
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.list;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.json.JSONException;

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
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxResult;
import se.mbaeumer.fxmessagebox.MessageBoxType;

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
    public Boolean createUnit = false;
    public Boolean simulation = false;
    public Boolean weather = false;
    public String id;
    private Incident incident;
    public String incidentstring;
    public int type;
    private GUIController gui;
    private Timeline timeline;
    private AnimationTimer timer;
    private Integer i = 0;
    public List<Painter<JXMapViewer>> painters;
    private Server server;
    public Simulation simulations;
    private SelectionAdapter sa;
    private SelectionPainter sp;

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
        sa = new SelectionAdapter(mapViewer);
        sp = new SelectionPainter(sa);
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
            units.add(new MyWaypoint(a.getName(), color, spot));
            // Create a waypoint painter that takes all the waypoints
            waypointPainter2.setWaypoints(units);
            waypointPainter2.setRenderer(new FancyWaypointRenderer());
            // Create a waypoint painter that takes all the waypoints
        }

        createStage();

        draw();

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

    public void drawUnits() {
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
            units.add(new MyWaypoint(a.getName(), color, spot));
            // Create a waypoint painter that takes all the waypoints
            waypointPainter2.setWaypoints(units);
            waypointPainter2.setRenderer(new FancyWaypointRenderer());
            // Create a waypoint painter that takes all the waypoints
        }
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

    public void DrawIncidents() {
        waypoints.clear();
        for (Incident object : incidents) {
            GeoPosition plek = new GeoPosition(Double.parseDouble(object.getLatitude()), Double.parseDouble(object.getLongitude()));

            waypoints.add(new DefaultWaypoint(plek));
            draw();

            // Create a waypoint painter that takes all the waypoints
        }
        waypointPainter.setWaypoints(waypoints);
    }

    public void addIncidentOnMap(Incident a) {
        GeoPosition plek = new GeoPosition(Double.parseDouble(a.getLatitude()), Double.parseDouble(a.getLongitude()));
        waypoints.add(new DefaultWaypoint(plek));
        waypointPainter.setWaypoints(waypoints);
        draw();

    }

    public void draw() {
        // Create a compound painter that uses both the route-painter and the waypoint-painter
        if (simulations != null) {
            painters.add(simulations);
        }
        painters.add(waypointPainter2);
        painters.add(waypointPainter);
        painters.add(waypointPainter3);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);

    }

    public void drawRoute() {

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

        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);
        mapViewer.setOverlayPainter(sp);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        //mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        mapViewer.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {

                //Rechter muisknop klik
                if (me.getButton() == MouseEvent.BUTTON3) {
                    GeoPosition mousepoint = mapViewer.convertPointToGeoPosition(me.getPoint());
                    Double lat1 = mousepoint.getLatitude();
                    Double lng1 = mousepoint.getLongitude();
                    Double lat2;
                    Double lng2;

                    for (MyWaypoint u : units) {
                        lat2 = u.getPosition().getLatitude();
                        lng2 = u.getPosition().getLongitude();

                        if (UnitControl.distFrom(lat1.floatValue(), lng1.floatValue(), lat2.floatValue(), lng2.floatValue()) < (mapViewer.getZoom() * mapViewer.getZoom() * mapViewer.getZoom() * 2)) {
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {

                                    MessageBox msg = new MessageBox("Send message to unit " + u.getLabel() + "?", MessageBoxType.YES_NO);
                                    msg.showAndWait();

                                    if (msg.getMessageBoxResult() == MessageBoxResult.YES) {

                                        try {
                                            if (gui.messageToUnit(u.getLabel()) == false) {
                                                MessageBox msg2 = new MessageBox("Error connecting to unit", MessageBoxType.OK_ONLY);
                                                msg2.show();

                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(Gmaps.class
                                                    .getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {
                                        //msg.close();
                                    }
                                }

                            });
                        }
                    }

                } else {
                    if (createUnit == true) {
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
                            if (a.getName().equals(id)) {
                                GeoPosition plek2 = new GeoPosition(a.getLatidude(), a.getLongitude());
                                ChatMessage chat = new ChatMessage(gui.getIncidentorder() + "\n" + gui.getUnitDescription(), "Meldkamer", id);
                                server.sendMessage(chat);
                                a.setIncident(incidentstring);
                                new Animation(plek, plek2, id, orders, units, Gmaps.this, waypointPainter3);
                            }
                        }
                        createUnit = false;
                        // Create a waypoint painter that takes all the waypoints
                        waypointPainter3.setWaypoints(orders);
                        waypointPainter3.setRenderer(new FancyWaypointRenderer());
                        draw();
                    }

                    if (simulation == true) {
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
                            if (a.getName().equals(id)) {
                                GeoPosition plek2 = new GeoPosition(a.getLatidude(), a.getLongitude());
                                ChatMessage chat = new ChatMessage(gui.getIncidentorder() + "\n" + gui.getUnitDescription(), "Meldkamer", id);
                                server.sendMessage(chat);
                                a.setIncident(incidentstring);
                                Point2D fire = mapViewer.getTileFactory().geoToPixel(plek, mapViewer.getZoom());
                                simulations = new Simulation(plek, plek2, id, orders, units, Gmaps.this, waypointPainter3);
                                new SimulationAnimation(simulations, plek, plek2, id, orders, units, Gmaps.this, waypointPainter3);

                            }
                        }
                        createUnit = false;
                        // Create a waypoint painter that takes all the waypoints
                        waypointPainter3.setWaypoints(orders);
                        waypointPainter3.setRenderer(new FancyWaypointRenderer());
                        /*
                         List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
                         painters.add(simulations);
                         CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
                                
                         mapViewer.setOverlayPainter(painter);*/
                        draw();
                        simulation = false;
                    }

                    if (weather == true) {
                        GeoPosition mousepoint = mapViewer.convertPointToGeoPosition(me.getPoint());
                        Double lat1 = mousepoint.getLatitude();
                        Double lng1 = mousepoint.getLongitude();
                        try {
                            new Weather(lng1, lat1);
                        } catch (IOException ex) {
                            Logger.getLogger(Gmaps.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(Gmaps.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        ); // end MouseAdapter

    }

    public void sendMessage(String message, String Sender, String Receiver) {
        ChatMessage chat = new ChatMessage(message, Sender, Receiver);
        server.sendMessage(chat);
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

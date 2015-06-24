/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.googlemaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import javafx.application.Platform;
import javax.json.*;
import org.json.JSONException;
import org.json.JSONObject;
import se.mbaeumer.fxmessagebox.MessageBox;
import se.mbaeumer.fxmessagebox.MessageBoxType;

/**
 *
 * @author Casvan
 */
public class Weather {

    WeerBerichtJSON json = new WeerBerichtJSON();
    String Tsecondes;
    String Turen;
    String tminuten;
    String tdagen;
    String tmaanden;
    static double temprature;
    static String summary;

    /**
     * @param args the command line arguments
     */
    public Weather(double longitude, double latitude) throws IOException, JSONException {

        JSONObject json = WeerBerichtJSON.readJsonFromUrl(latitude, longitude);
        JSONObject jsoneind = (JSONObject) json.get("currently");
        temprature = ((jsoneind.getDouble("temperature") - 32) / 1.8);
        System.out.print(Math.round(temprature));
        summary = jsoneind.getString("summary");
        Platform.runLater(new Runnable() {

                                @Override
                                public void run() {

                                    MessageBox msg = new MessageBox((int) temprature + "dgrees and " + summary, MessageBoxType.OK_ONLY);
                                    msg.showAndWait();
                                }
        });
                

    }

}

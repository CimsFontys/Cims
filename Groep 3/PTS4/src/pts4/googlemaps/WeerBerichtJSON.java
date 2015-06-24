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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Casvan
 */
public class WeerBerichtJSON {
    static String tsecondes;
    static String turen;
    static String tminuten;
    static String tdagen;
    static String tmaanden;
    
    public WeerBerichtJSON()
    {
                
    }
    private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(double latitude, double longtitude) throws IOException, JSONException {
    
    Calendar c = Calendar.getInstance();
                int seconds = c.get(Calendar.SECOND);
                if (seconds < 10) {
                    tsecondes = "0" + String.valueOf(seconds);
                } else {
                    tsecondes = String.valueOf(seconds);
                }
                int uur = c.get(Calendar.HOUR_OF_DAY);
                if (uur < 10) {
                    turen = "0" + String.valueOf(uur);
                } else {
                    turen = String.valueOf(uur);
                }
                int minuten = c.get(Calendar.MINUTE);
                if (minuten < 10) {
                    tminuten = "0" + String.valueOf(minuten);
                } else {
                    tminuten = String.valueOf(minuten);
                }
                int dag = c.get(Calendar.DAY_OF_MONTH);
                if (dag < 10) {
                    tdagen = "0" + String.valueOf(dag);
                } else {
                    tdagen = String.valueOf(dag);
                }
                int maand = c.get(Calendar.MONTH) + 1;
                if (maand < 10) {
                    tmaanden = "0" + String.valueOf(maand);
                } else {
                    tmaanden = String.valueOf(maand);
                }
                int jaar = c.get(Calendar.YEAR);
                final StringBuilder stringURL = new StringBuilder();
                stringURL.append("https://api.forecast.io/forecast/8dd1383c8ec57d47b44334ac6111d84e/" + latitude + "," + longtitude + "," + jaar + "-" + tmaanden + "-" + tdagen + "T" + turen + ":" + tminuten + ":" + tsecondes);
      InputStream is = new URL(stringURL.toString()).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
}

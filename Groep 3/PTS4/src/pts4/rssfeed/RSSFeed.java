package pts4.rssfeed;


import java.net.URL;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Systeembeheer
 */
public class RSSFeed
{
   private String title;
   private String description;
   private String geoLat;
   private String geoLong;
   private URL link;
   
   public RSSFeed(String Title, String Description, String GeoLat, String GeoLong, URL Link)
   {
       this.title = Title;
       this.description = Description;
       this.geoLat = GeoLat;
       this.geoLong = GeoLong;
       this.link = Link;
   }
   
   @Override
   public String toString()
   {
       return (title + " " + description + " " + geoLat + " " + geoLong);
   }
   
   public String getTitle() {
       return this.title;
   }
   
   public String getDescription() {
       return this.description;
   }
   
   public String getLat() {
       return this.geoLat;
   }
   
   public String getLong() {
       return this.geoLong;
   }
}

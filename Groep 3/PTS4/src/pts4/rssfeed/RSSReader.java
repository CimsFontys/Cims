package pts4.rssfeed;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pts4.klassen.Incident;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SpierbundelDeluxinator
 */
public class RSSReader {

    private static RSSReader instance = null;
    private URL rssURL;
    private String title;
    private String description;
    private String geoLat;
    private String geoLong;
    private URL link;
    private Boolean geo = false;
    private ArrayList<RSSFeed> rssFeeds;
    private ArrayList<RSSFeed> newRSSFeeds;
    private RSSContainer feedContainer;
    private RSSContainer newFeedContainer;
    private static String rssString;
    
    private RSSReader() 
    {
    }

    public static RSSReader getInstance() {
        if (instance == null) {
            instance = new RSSReader();
        }
        return instance;
    }

    public void setURL(URL url) {
        rssURL = url;
    }

    public void writeFeed() {
        try {
            rssFeeds = new ArrayList<RSSFeed>();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(rssURL.openStream());

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                title = getValue(item, "title");
                description = getValue(item, "description");
                try {
                    geoLat = getValue(item, "geo:lat").toString();
                    geoLong = getValue(item, "geo:long").toString();
                    geo = true;
                } catch (Exception ex) {
                    geo = false;
                }
                link = new URL(getValue(item, "link"));
                if (!geo) {
                    geoLat = "100";
                    geoLong = "100";
                }
                RSSFeed newFeed = new RSSFeed(title, description, geoLat, geoLong, link);
                rssFeeds.add(newFeed);
                        //System.out.println(newFeed.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compareFeed() {
        try {
            rssFeeds = new ArrayList<RSSFeed>();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(rssURL.openStream());

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                title = getValue(item, "title");
                description = getValue(item, "description");
                try {
                    geoLat = getValue(item, "geo:lat").toString();
                    geoLong = getValue(item, "geo:long").toString();
                    geo = true;
                } catch (Exception ex) {
                    geo = false;
                }
                link = new URL(getValue(item, "link"));
                if (!geo) {
                    geoLat = "100";
                    geoLong = "100";
                }
                RSSFeed newFeed = new RSSFeed(title, description, geoLat, geoLong, link);

                if (!rssFeeds.contains(newFeed)) {
                    newRSSFeeds.add(newFeed);
                } else {
                    rssFeeds.add(newFeed);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(rssFeeds.size());
        feedContainer = new RSSContainer(rssFeeds);
        newFeedContainer = new RSSContainer(newRSSFeeds);
    }

    public String getValue(Element parent, String nodeName) {
        return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
    }
    
    public String removeZero(String input)
    {
        return input.replaceAll("00000", "-");
    }
    
    private ArrayList<String> getEnumList()
    {
        ArrayList<String> enumInString = new ArrayList<String>();
        for (EnumProvincies prov : EnumProvincies.values()) 
        {
            if(prov.name().contains("00000"))
            {
                removeZero(prov.name());
            }
            enumInString.add(prov.name());
        }
        return enumInString;
    }
    
    public ArrayList<Incident> getIncidents() {
        ArrayList<Incident> incidents = new ArrayList<>();
        
        for (RSSFeed r : rssFeeds) {
            incidents.add(new Incident(r.getLong(), r.getLat(), r.getTitle(), r.getDescription()));
        }
        return incidents;
    }

//    public static void main(String[] args) 
//    {
//        Scanner scanner = new Scanner(System.in);
//        String provincie = scanner.nextLine().toLowerCase();
//        provincie = provincie.replaceAll("-", "00000");
//        provincie = provincie.replaceAll(" ", "");
//        
//        Boolean gevonden = false;
//        if (provincie == null || provincie.equals(""))
//        {
//            rssString = "http://rss.politie.nl/rss/algemeen/ab/algemeen.xml";
//        }
//        else
//        {
//            for (EnumProvincies prov : EnumProvincies.values()) 
//            {
//                if (prov.name().equals(provincie))
//                {
//                    gevonden = true;
//                    String zoekTerm = removeZero(provincie);
//                    rssString = "http://rss.politie.nl/rss/nb/provincies/" + zoekTerm +".xml";
//                }
//            }
//            if(!gevonden)
//            {
//                System.out.println("Deze provincie bestaat niet of deze is verkeerd getypt : " + provincie);
//                rssString = null;
//            }
//        }
//        
//        try {
//            if(rssString != null)
//            {
//                RSSReader reader = RSSReader.getInstance();
//                reader.setURL(new URL(rssString));
//                reader.writeFeed();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

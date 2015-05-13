package pts4.rssfeed;


import java.util.ArrayList;
import java.util.Observable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sander 
 */
public class RSSContainer extends Observable implements RSSFeedterface{
    private ArrayList<RSSFeed> rssList;
    
    public RSSContainer(ArrayList<RSSFeed> feeds)
    {
        this.rssList = feeds;
    }

    @Override
    public ArrayList<RSSFeed> getFeeds() {
    return this.rssList;
    }

    @Override
    public void addFeed(RSSFeed feed) {
        this.rssList.add(feed);
    }

    @Override
    public void removeFeed(RSSFeed feed) {
        this.rssList.remove(feed);
    }

    @Override
    public void addObserver(Observable o) {
        this.addObserver(o);
    }
   
}

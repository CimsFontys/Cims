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
public interface RSSFeedterface {
   
  /**
   * 
   * @return Arraylist van alle feeds
   */
  public ArrayList<RSSFeed> getFeeds();
  /**
   * 
   * @param feed Een item(nieuwsbericht) dat toegevoegd dient te worden 
   */
  public void addFeed(RSSFeed feed);
  /**
   * 
   * @param feed een item(nieuwsbericht) dat verwijdert dient te worden 
   */
  public void removeFeed(RSSFeed feed);
  /**
   * 
   * @param o object dat als observable wordt toegevoegd 
   */
  public void addObserver(Observable o);
  
    
}

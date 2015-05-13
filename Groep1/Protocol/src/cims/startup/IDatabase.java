/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cims.startup;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Michael & Merijn
 */
public interface IDatabase 
{
    
    /**
     * TESTED AND WORKING
     * @param username
     * @param password
     * @return 
     */
    public String loginPerson(String username, String password);
    
    public String getPersonInformation(int personId);
    
    /**
     * TESTED AND WORKING
     * @param regionId
     * @return 
     */
    public String getCalamityFromRegion(int regionId);
    
    public ArrayList<String> getCalamityFromRegionDetailed(int regionId);
   
    /**
     * TESTED AND WORKING
     * @param idpersonal_type
     * @param first_name
     * @param last_name
     * @param middle_name
     * @param username
     * @param password
     * @param SSN
     * @param email
     * @param Birthdate
     * @param phonenumber
     * @param Street
     * @param City
     * @param Postal
     * @param Region
     * @param configurator
     * @return 
     */
    public boolean insertPerson(int idpersonal_type, String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, Date Birthdate, String phonenumber, String Street, String City, String Postal, String Region, String configurator);
    
    /**
     * TESTED AND WORKING
     * @param geo_long
     * @param geo_lat
     * @param personid
     * @param name
     * @param description
     * @param timestamp
     * @param calamitydanger
     * @param region
     * @return 
     */
    public boolean insertCalamity(String geo_long, String geo_lat, int personid, String name, String description, Date timestamp, String calamitydanger, String region);
    
    /**
     * TESTED AND WORKING
     * @param personid
     * @return 
     */
    public String retrieveCalamityWithPersonID(int personid);
    
    /**
     * TESTED AND WORKING
     * @param id
     * @return 
     */
    public String retrieveCalamityWithID(int id);
    
    /**
     * TESTED AND WORKING
     * @return 
     */
    public String retrieveAllCalamities();
    
    public boolean insertLog(int id_emergency_service, String description);
    
    public ArrayList<String> retrieveLogs(int id_emergency_service);
    
    public boolean insertMessage(int sender_id, int receiver_id, String message, File file);
   
    public ArrayList<String> retrieveMessage(int sender_id, int receiver_id);
   
    public boolean insertInformation(int id_calamity, String type, String description, File file, int id_civilian, int id_emergency);
       
    public ArrayList<String> retrieveInformation(int id_calamity);
    
    public ArrayList<String> retrieveRegions();
    
    public File retrieveFileWithID(int fileid);
    
    public boolean insertLocation(String name, String geo_long, String geo_lat, int personal_type);
    
    public ArrayList<String> retrieveLocations();
}

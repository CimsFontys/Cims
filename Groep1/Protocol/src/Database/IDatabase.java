/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Michael & Merijn
 */
public interface IDatabase 
{
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param username
     * @param password
     * @return 
     */
    public String loginPerson(String username, String password);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param personId
     * @return 
     */
    public String getPersonInformation(int personId);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param regionId
     * @return 
     */
    public String getCalamityFromRegion(int regionId);
    
    public String getCalamityFromRegionDetailed(int regionId);
   
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
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
     * TESTED AND WORKING -> IN MESSAGEBUILDER
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
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param personid
     * @return 
     */
    public String retrieveCalamityWithPersonID(int personid);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param id
     * @return 
     */
    public String retrieveCalamityWithID(int id);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @return 
     */
    public String retrieveAllCalamities();
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param personid
     * @param description
     * @return 
     */
    public boolean insertLog(int personid, String description);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param personid
     * @return 
     */
    public String retrieveLogs(int personid);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param sender_id
     * @param receiver_id
     * @param message
     * @param file
     * @return 
     */
    public boolean insertMessage(int sender_id, int receiver_id, String message, File file);
   
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param sender_id
     * @param receiver_id
     * @return 
     */
    public String retrieveMessages(int sender_id, int receiver_id);
   
    public boolean insertInformation(int id_calamity, String type, String description, File file, int id_civilian, int id_emergency);
       
    public ArrayList<String> retrieveInformation(int id_calamity);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @return 
     */
    public String retrieveRegions();
    
    
    public File retrieveFileWithID(int fileid);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @param name
     * @param geo_long
     * @param geo_lat
     * @param locationtypeid -> 1 = firestation, 2 = policestation, 3 = hospital
     * @return 
     */
    public boolean insertLocation(String name, String geo_long, String geo_lat, int locationtypeid);
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @return 
     */
    public String retrieveLocations();
    
    /**
     * TESTED AND WORKING -> IN MESSAGEBUILDER
     * @return 
     */
    public String retrieveLocationTypes();
}

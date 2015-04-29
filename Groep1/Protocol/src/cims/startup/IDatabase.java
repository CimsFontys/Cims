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
     * METHOD FOR EMERGENCY PERSONAL LOGIN, USING UNIQUE PERSONAL ID
     * @param username  =   NOT NULL
     * @param password  =   NOT NULL
     * @param id
     * @return 
     */
    public int loginEmergencyService(String username, String password, int id);
    
    /**
     * 
     * @param first_name    -   MUST HAVE
     * @param last_name     -   MUST HAVE 
     * @param middle_name   -   OPTIONAL
     * @param username      -   MUST HAVE
     * @param password      -   MUST HAVE
     * @param SSN           -   MUST HAVE
     * @param email         -   MUST HAVE
     * @param Birthdate     -   MUST HAVE
     * @param phonenumber   -   MUST HAVE
     * @param Street        -   MUST HAVE
     * @param City          -   MUST HAVE
     * @param Postal        -   MUST HAVE
     * @param Region        -   MUST HAVE
     * @param idpersonal_type   - POLICE 1
     *                          - FIREDEP 2
     *                          - AMBULANCE 3
     * 
     * METHOD FOR INSERTING EMERGENCY PERSONAL INTO THE SYSTEM!
     * @return TRUE IF SUCCESFULL
     */
    public boolean insertEmergencyService(int idpersonal_type, String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, Date Birthdate, String phonenumber, String Street, String City, String Postal, String Region);
    
    /**
     * METHOD FOR CIVILIAN LOGIN
     * @param username  =   NOT NULL
     * @param password  =   NOT NULL
     * @return 
     */
    public boolean loginCivilian(String username, String password);
    
    /**
     * Method for inserting Civilian accounts into the database
     * @param first_name    -   First Name of the Civilian
     * @param last_name     -   Last Name of the Civilian   
     * @param middle_name   -   Middle Name of the Civilian (CAN BE NULL)
     * @param username      -   Username of the Civilian used to login the system
     * @param password      -   Password of the Civilian used to login the system
     * @param SSN           -   OPTIONAL
     * @param email         -   VERPLICHT
     * @param Birthdate     -   VERPLICHT
     * @param phonenumber   -   OPTIONAL
     * @param Street        -   OPTIONAL
     * @param City          -   OPTIONAL
     * @param Postal        -   OPTIONAL
     * @param Region        -   OPTIONAL
     * @return True if successfully added to the Database
     */
    public boolean insertCivilian(String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, Date Birthdate, String phonenumber, String Street, String City, String Postal, String Region);
        
    /**
     * METHOD FOR INSERTING CALAMITY INTO THE DATABASE
     * @param geo_long                  =   NOT NULL
     * @param geo_lat                   =   NOT NULL
     * @param id_emergency_service      =   NOT NULL
     * @param name                      =   NOT NULL
     * @param description               =   NOT NULL
     * @param timestamp                 =   NOT NULL
     * @return TRUE IF SUCCESFULL
     */
    public boolean insertCalamity(String geo_long, String geo_lat, int id_emergency_service, String name, String description, Date timestamp);
    
    /**
     * RETRIEVE AN ARRAYLIST<STRING> WITH ALL CALAMITIES WITH EMERGENCY_PERSONAL_ID
     * @param id
     * @return 
     */
    public ArrayList<String> retrieveCalamityWithIES(int id_emergency_service);
    
     /**
     * RETRIEVE AN ARRAYLIST<STRING> WITH A SINGLE CALAMITY WITH ID
     * @param id
     * @return 
     */
    public ArrayList<String> retrieveCalamityWithID(int id);
    
    public ArrayList<String> retrieveAllCalamities();
    
    /**
     * METHOD FOR PLACING A PERSONAL LOG IN THE DATABASE
     * USE THIS METHOD AFTER EVERY METHOD AND CLICK!
     * @param id_emergency_service
     * @param description
     * @return 
     */
    public boolean insertLog(int id_emergency_service, String description);
    
    /**
     * METHOD FOR RETRIEVING ARRAYLIST<STRING> WITH ALL THE LOGS BY A CERTAIN PERSONAL ID
     * @param id_emergency_service
     * @return 
     */
    public ArrayList<String> retrieveLogs(int id_emergency_service);
    
    /**
     * INSERTS A MESSAGE INTO THE DATABASE, THIS IS THE PRIME FORM OF COMMUNICATION
     * BETWEEN STOKHOLDERS. 
     * @param sender_id         =   NOT NULL
     * @param receiver_id       =   NOT NULL
     * @param message           =   NOT NULL
     * @param file              =   CAN BE NULL
     * @return 
     */
    public boolean insertMessage(int sender_id, int receiver_id, String message, File file);
    
    /**
     * RETRIEVES AN ARRAYLIST OF STRING WITH SENDER_ID AND RECEIVER_ID
     * THIS IS THE COMMUNICATION BETWEEN 2 STAKEHOLDERS
     * SUBSTRING THE STRINGS FROM THE ARRAYLIST FOR USABLE INFORMATION
     * @return 
     */
    public ArrayList<String> retrieveMessage(int sender_id, int receiver_id);
    
    /**
     * METHOD FOR PUSHING INFORMATION TO THE DATABASE
     * @param id_calamity   =   NOT NULL
     * @param type          =   NOT NULL
     * @param description   =   NOT NULL
     * @param file          =   CAN BE NULL
     * @param id_civilian   =   ID_CIVILIAN OR ID_EMERGENCY NOT BOTH!! 
     * @param id_emergency  =   ID_CIVILIAN OR ID_EMERGENCY NOT BOTH!!
     *                          INSERT 0 FOR THE ID THAT YOU'RE NOT USING!
     * 
     *                          EXAMPLE: ID_CIVILIAN IS THE ONE YOU INSERTED
     *                                   THEN INSERT 0 IN ID_EMERGECY
     * 
     * TYPE IS THE SPECIFIC TYPE OF INFORMATION WE WANT THIS IN THESE SYNTAXES!
     *      FIRE
     *      POISONOUS
     *      STORM
     *      
     *      ADD MORE IF YOU LIKE BUT UPDATE US WITH THAT INFORMATION
     * 
     * @return 
     */
    public boolean insertInformation(int id_calamity, String type, String description, File file, int id_civilian, int id_emergency);
       
    public ArrayList<String> retrieveInformation(int id_calamity);
    /**
     * METHOD FOR RETRIEVING REGIONS FOR ADDING PERSONAL
     * @return 
     */
    public ArrayList<String> retrieveRegions();
    
    /**
     * METHOD FOR RETRIEVING FILE
     * INSERT FILEID TO GET FILE OBJECT REFERENCE
     * @param fileid
     * @return 
     */
    public File retrieveFileWithID(int fileid);
    
     /**
     * @param idpersonal_type   - POLICE 1
     *                          - FIREDEP 2
     *                          - AMBULANCE 3
     * 
     * METHOD FOR INSERTING EMERGENCY PERSONAL INTO THE SYSTEM!
     * @return TRUE IF SUCCESFULL
     */
    public boolean insertLocation(String name, String geo_long, String geo_lat, int personal_type);
    
     /**
     * @param type              - POLICE 1
     *                          - FIREDEP 2
     *                          - AMBULANCE 3
     * 
     * METHOD FOR INSERTING EMERGENCY PERSONAL INTO THE SYSTEM!
     * @return TRUE IF SUCCESFULL
     */
    public ArrayList<String> retrieveLocations();
}

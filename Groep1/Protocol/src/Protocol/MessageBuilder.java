package Protocol;

import FileTransfer.FileManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Merijn
 */
public class MessageBuilder 
{     
    public static final String RetrieveCalamityInformation = "retrievecalamityinformation";  //<- NOG DOEN IN CIMS SERVER
    public static final String RetrieveCalamityInformationReply = "retrievecalamityinformationreply";  //<- NOG DOEN IN CIMS SERVER
    public static final String RetrieveAllCalamitiesDetailed = "retrieveallcalamitiesdetailed"; // 
    public static final String RetrieveAllCalamitiesDetailedReply = "retrieveallcalamitiesdetailedreply"; //
    public static final String Login = "login"; //
    public static final String LoginReply = "loginreply"; //
    public static final String RetrieveAllCalamities = "retrieveallcalamities"; //
    public static final String RetrieveAllCalamitiesReply = "retrieveallcalamitiesreply"; //
    public static final String RetrievePersonInformation = "retrievepersoninformation"; //
    public static final String RetrievePersonInformationReply = "retrievepersoninformationreply"; //
    public static final String InsertPerson = "insertperson"; //
    public static final String InsertCalamity = "insertcalamity";
    public static final String RetrieveAllLocations = "retrievealllocations"; //
    public static final String RetrieveAllLocationsReply = "retrievealllocationsreply"; //
    public static final String RetrieveAllLocationTypes = "retrievealllocationtypes"; //
    public static final String RetrieveAllLocationTypesReply = "retrievealllocationtypesreply"; //
    public static final String InsertLocation = "insertlocation"; //
    public static final String RetrieveAllRegions = "retrieveallregions"; //
    public static final String RetrieveAllRegionsReply = "retrieveallregionsreply"; //
    public static final String RetrieveCalamitiesFromRegion = "retrievecalamitiesfromregion";  //
    public static final String RetrieveCalamitiesFromRegionReply = "retrievecalamitiesfromregionreply"; //
    public static final String RetrieveCalamitiesFromPersonID = "retrievecalamitiesfrompersonid"; //
    public static final String RetrieveCalamitiesFromPersonIDReply = "retrievecalamitiesfrompersonidreply"; //
    public static final String RetrieveCalamityWithID = "retrievecalamitywithid"; //
    public static final String RetrieveCalamityWithIDReply = "retrievecalamitywithidreply"; //
    public static final String InsertLog = "insertlog"; //
    public static final String RetrieveLogs = "retrievelogs"; //
    public static final String RetrieveLogsReply = "retrievelogsreply"; //
    public static final String InsertMessage = "insertmessage"; //
    public static final String RetrieveMessages = "retrievemessages"; // 
    public static final String RetrieveMessagesReply = "retrievemessagesreply"; //
    public static final String RetrieveCalamityWithName = "retrievecalamitywithname";
    public static final String RetrieveCalamityWithNameReply = "retrievecalamitywithnamereply";
    public static final String RetrievePersonIdFromName = "retrievepersonidfromname";
    public static final String RetrievePersonIdFromNameReply = "'retrievepersonidfromnamereply";
    
    private static final String token = "-";
    
    private final boolean canHaveFile = false;
    
    /**
     * Consturctor of the the message builder
     * Takes a type.
     * Creates the needed info to create a message and 
     * validates is bassed on the type.
     * @param type Use like MessageBuilder.LOGIN enz.
     */
    public MessageBuilder()
    {
        
    }  
    
    public Message buildRetrievePersonIdFromName(String name)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("username", name);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrievePersonIdFromName);
        
        return message;
    }
    
    public Message buildRetrievePersonIdFromNameReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrievePersonIdFromNameReply);
        
        return message;
    }
    
    public Message buildRetrieveCalamityInformation(int calamityid)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("calamityid", calamityid);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrieveCalamityInformation);
        
        return message;
    }
    
    public Message buildRetrieveCalamityInformationReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveCalamityInformationReply);
        
        return message;
    }
    
    public Message buildRetrieveAllCalamitiesDetailed()
    {      
        Message message = new Message();
        message.setType(RetrieveAllCalamitiesDetailed);
        return message;
    }
    
    public Message buildRetrieveAllCalamitiesDetailedReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveAllCalamitiesDetailedReply);
        return message;
    }
    
    public Message buildRetrieveMessages(int senderid, int receiverid)
    {      
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("senderid", senderid);
        jb.add("receiverid", receiverid);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrieveMessages);
        
        return message;
    }
    
    public Message buildRetrieveMessagesReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveMessagesReply);
        
        return message;
    }

    public Message buildInsertMessage(int personid, int receiverid, String messageS, File file)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("personid", personid);
        jb.add("receiverid", receiverid);
        jb.add("message", messageS);
                       
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(InsertMessage);
        
//        byte[] bFile = new byte[(int) file.length()];
//        FileInputStream fis;
//        try {
//            fis = new FileInputStream(file);
//            fis.read(bFile);
//            fis.close();
//        }
//        catch(IOException ex)
//        {
//        }
//
//        message.setFile(bFile);
        
        return message;
    }
    
    public Message buildRetrieveLogs(int personid)
    {      
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("personid", personid);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrieveLogs);
        
        return message;
    }
    
    public Message buildRetrieveLogsReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveLogsReply);
        
        return message;
    }
    
    public Message buildInsertLog(int personid, String text)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("personid", personid);
        jb.add("logdescription", text);
                       
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(InsertLog);
        
        return message;
    }
    
    public Message buildRetrieveCalamityWithId(int calamityid)
    {      
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("calamityid", calamityid);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrieveCalamityWithID);
        
        return message;
    }
    
    public Message buildRetrieveCalamityWithIdReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveCalamityWithIDReply);
        return message;
    }
    
    public Message buildRetrieveCalamityWithName(String name)
    {      
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("calamityname", name);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrieveCalamityWithName);
        
        return message;
    }
    
    public Message buildRetrieveCalamityWithNameReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveCalamityWithNameReply);
        return message;
    }
    
    public Message buildRetrieveCalamitiesFromPersonID(int personid)
    {      
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("personid", personid);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrieveCalamitiesFromPersonID);
        
        return message;
    }
    
    public Message buildRetrieveCalamitiesFromPersonIDReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveCalamitiesFromPersonIDReply);
        return message;
    }
    
    public Message buildRetrieveCalamitiesFromRegion(int regionid)
    {      
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("regionid", regionid);
            
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrieveCalamitiesFromRegion);
        
        return message;
    }
    
    public Message buildRetrieveCalamitiesFromRegionReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveCalamitiesFromRegionReply);
        return message;
    }
    
    public Message buildRetrieveRegions()
    {      
        Message message = new Message();
        message.setType(RetrieveAllRegions);
        return message;
    }
    
    public Message buildRetrieveRegionsReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveAllRegionsReply);
        return message;
    }
    
    public Message buildInsertLocation(String name, String longtitude, String latitude, int locationtypeid)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("locationname", name);
        jb.add("locationlongtitude", longtitude);
        jb.add("locationlatitude", latitude);
        jb.add("locationtypeid", locationtypeid);
                       
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(InsertLocation);
        
        return message;
    }
     
    public Message buildRetrieveLocationTypes()
    {      
        Message message = new Message();
        message.setType(RetrieveAllLocationTypes);
        return message;
    }
    
    public Message buildRetrieveLocationTypesReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveAllLocationTypesReply);
        return message;
    }
  
    public Message buildRetrieveLocations()
    {      
        Message message = new Message();
        message.setType(RetrieveAllLocations);
        return message;
    }
    
    public Message buildRetrieveLocationsReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveAllLocationsReply);
        return message;
    }
    
    public Message buildLoginMessage(String username, String password)
    {      
        JsonArrayBuilder jb = Json.createArrayBuilder();
        JsonObjectBuilder jo = Json.createObjectBuilder();
        
        jo.add("username", username);
        jo.add("password", password);
                
        jb.add(jo);
        
        JsonArray array = jb.build();
        
        Message message = new Message();
        message.setText(array.toString());
        message.setType(Login);
        
        return message;
    }
    
    public Message buildLoginReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(LoginReply);
        return message;
    }
    
    public Message buildRetrieveAllCalamitiesMessage()
    {
        Message message = new Message();
        message.setType(RetrieveAllCalamities);
        return message;
    }
    
    public Message buildRetrieveAllCalamitiesReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrieveAllCalamitiesReply);
        return message;
    }
    
    public Message buildPersonInformation(int personid)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("personid", personid);
                
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(RetrievePersonInformation);
        
        return message;
    }
    
    public Message buildPersonInformationReply(String json)
    {
        Message message = new Message();
        message.setText(json);
        message.setType(RetrievePersonInformationReply);
        return message;
    }
    
    public Message buildInsertPerson(int persontypeid, String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, LocalDate Birthdate, String phonenumber, String Street, String City, String Postal, String Region, String configurator)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("persontype", persontypeid);
        jb.add("firstname", first_name);
        jb.add("middlename", middle_name);
        jb.add("lastname", last_name);
        jb.add("username", username);
        jb.add("password", password);
        jb.add("ssn", SSN);
        jb.add("email", email);
        jb.add("birthdate", Birthdate.toString());
        jb.add("phonenumber", phonenumber);
        jb.add("street", Street);
        jb.add("city", City);
        jb.add("postal", Postal);
        jb.add("region", Region);
        jb.add("configurator", configurator);
                       
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(InsertPerson);
        
        return message;
    }
    
    public Message buildInsertCalamity(String geo_long, String geo_lat, int personid, String name, String description, Date timestamp, String calamitydanger, String region)
    {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        
        jb.add("calamitylongtitude", geo_long);
        jb.add("calamitylatitude", geo_lat);
        jb.add("personid", personid);
        jb.add("calamityname", name);
        jb.add("calamitydescription", description);
        jb.add("calamitydate", timestamp.toString());
        jb.add("calamitydanger", calamitydanger);
        jb.add("region", region);
      
        JsonObject jo = jb.build();
        
        Message message = new Message();
        message.setText(jo.toString());
        message.setType(InsertCalamity);
        
        return message;
    }
}

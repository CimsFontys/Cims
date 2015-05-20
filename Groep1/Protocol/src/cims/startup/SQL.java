/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cims.startup;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
/**
 *
 * @author Michael & Merijn 
 */
public class SQL extends DatabaseConnector implements IDatabase
{    
    ArrayList<PreparedStatement> statements = new ArrayList<>();
    Boolean connectiedatabase1 = true;
    Boolean connectiedatabase2 = true;
    Boolean updateconndatabase1 = true;
    Boolean updateconndatabase2 = true;
    public SQL()
    {
        super();
    }

    /**
     * TESTED AND WORKING
     */
    @Override
    public String loginPerson(String username, String password) 
    {
        String query = "SELECT * FROM person WHERE personusername = ? AND personpassword = ?";
        
        try
        {
            if(connectiedatabase1)
            {
                connectiedatabase1 = super.connectToDatabase();
            }
            else
            {
                connectiedatabase2 = super.connectToDatabase2();
            }
        }
        catch (Exception e)
        {
            return "";
        }
        
        try
        {
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, username);
            prest.setString(2, password);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                int personId = res.getInt("personid");
                int persontypeid = res.getInt("persontypeid");
                String personconfigurator = res.getString("personconfigurator");

                JsonObjectBuilder jb = Json.createObjectBuilder();
                jb.add("personid" , personId);
                jb.add("persontypeid", persontypeid);
                
                JsonObject jo = jb.build();
                
                return jo.toString();
            }
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return "";
    }

    /**
     * TESTED AND WORKING
     */
    @Override
    public boolean insertPerson(int idpersonal_type, String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, Date Birthdate, String phonenumber, String Street, String City, String Postal, String Region, String configurator) 
    {
         if(this.checkRegion(Region) == false)
        {
            this.insertRegion(Region);
        }
        
        if(this.checkAdress(Street, City, Postal, Region) == false)
        {
            this.insertAddress(Street, City, Postal, Region);
        }
        
        try
        {
                super.connectToDatabase();
                super.connectToDatabase2();
        }
        catch(Exception e)
        {
            return false;
        }
        try
        {
            String query = "INSERT INTO person (personfirstname, personlastname, personmiddlename, personusername, personpassword, personssn, personemail, personbirthdate, addressid, personphone, persontypeid, personconfigurator)" 
                    + " values (?,?,?,?,?,?,?,?,(SELECT addressid FROM address WHERE addressstreet = ? AND addresscity = ? AND addresspostal = ? and regionid = (SELECT regionid FROM region WHERE regionname = ?)),?,?,?)";
            PreparedStatement prest = conn.prepareStatement(query);
            
            java.sql.Timestamp timestamp = new java.sql.Timestamp(Birthdate.getTime());
            
            prest.setString(1, first_name);          
            prest.setString(2, last_name);  
            prest.setString(3, middle_name);  
            prest.setString(4, username);
            prest.setString(5, password);
            prest.setString(6, SSN);
            prest.setString(7, email);
            prest.setTimestamp(8, timestamp);
            prest.setString(9, Street);
            prest.setString(10, City);
            prest.setString(11, Postal);
            prest.setString(12, Region);
            prest.setString(13, phonenumber);
            prest.setInt(14, idpersonal_type);
            prest.setString(15, configurator);
            
            prest.execute();
            if(connectiedatabase1 == false || connectiedatabase2 == false)
            {
                statements.add(prest);
            }
            
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }   

    /**
     * TESTED AND WORKING
     */
    @Override
    public boolean insertCalamity(String longtitude, String latitude, int personid, String name, String description, Date timestamp, String calamitydanger, String region) 
    {
        if(this.checkRegion(region) == false)
        {
            this.insertRegion(region);
        }
         
        try
        {
            super.connectToDatabase();
        }
        catch(Exception e)
        {
            return false;
        }
        try
        {
            String query = "INSERT INTO calamity (calamitylatitude, calamitylongtitude, calamityname, calamitydescription ,  calamitydate , personid, calamitydanger, regionid)" 
                    + " values (?,?,?,?,?,?, ?, (SELECT regionid FROM region WHERE regionname = ?))";
            PreparedStatement prest = conn.prepareStatement(query);
            
            Calendar calendar = new GregorianCalendar();

            java.sql.Timestamp Timestamp = new java.sql.Timestamp(timestamp.getTime());
            
            prest.setString(1, latitude); 
            prest.setString(2, longtitude); 
            prest.setString(3, name);
            prest.setString(4, description);
            prest.setTimestamp(5, Timestamp);
            prest.setInt(6, personid);
            prest.setString(7, calamitydanger);
            prest.setString(8, region);
            
            prest.execute();
            
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }

    @Override
    public String retrieveCalamityWithPersonID(int personid) 
    {        
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM calamity WHERE personid = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, personid);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int id_calamity = res.getInt("calamityid");
                String geo_long = res.getString("calamitylongtitude");
                String geo_lat = res.getString("calamitylatitude");
                String name = res.getString("calamityname");
                String description = res.getString("calamitydescription");
                Date date = res.getDate("calamitydate");
                String calamitydanger = res.getString("calamitydanger");
                int regionid = res.getInt("regionid");
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("calamityid" , id_calamity);
                jb2.add("calamitylongtitude" , geo_long);
                jb2.add("calamitylatitude" , geo_lat);
                jb2.add("calamityname" , name);
                jb2.add("calamitydescription" , description);
                jb2.add("calamitydate" , date.toString());
                jb2.add("calamitydanger" , calamitydanger);
                jb2.add("regionid", regionid);
                
                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public String retrieveCalamityWithID(int id)
    {        
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            JsonObjectBuilder jb = Json.createObjectBuilder();
            String query = "SELECT * FROM calamity WHERE calamityid = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int id_calamity = res.getInt("calamityid");
                String geo_long = res.getString("calamitylongtitude");
                String geo_lat = res.getString("calamitylatitude");
                String name = res.getString("calamityname");
                String description = res.getString("calamitydescription");
                Date date = res.getDate("calamitydate");
                String calamitydanger = res.getString("calamitydanger");
                String personid = res.getString("personid");
                int regionid = res.getInt("regionid");
                                
                jb.add("calamityid" , id_calamity);
                jb.add("calamitylongtitude" , geo_long);
                jb.add("calamitylatitude" , geo_lat);
                jb.add("calamityname" , name);
                jb.add("calamitydescription" , description);
                jb.add("calamitydate" , date.toString());
                jb.add("calamitydanger" , calamitydanger);
                jb.add("regionid", regionid);
                jb.add("personid", personid);
                
                JsonObject jo = jb.build();
                
                return jo.toString();
            }
            
        }
        catch(SQLException ee)
        {
            return "";
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return "";
    }
    
    @Override
    public String retrieveAllCalamities() 
    {        
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM calamity";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int id_calamity = res.getInt("calamityid");
                String geo_long = res.getString("calamitylongtitude");
                String geo_lat = res.getString("calamitylatitude");
                String name = res.getString("calamityname");
                String description = res.getString("calamitydescription");
                Date date = res.getDate("calamitydate");
                String calamitydanger = res.getString("calamitydanger");
                int regionid = res.getInt("regionid");
                int personid = res.getInt("personid");
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("calamityid" , id_calamity);
                jb2.add("calamitylongtitude" , geo_long);
                jb2.add("calamitylatitude" , geo_lat);
                jb2.add("calamityname" , name);
                jb2.add("calamitydescription" , description);
                jb2.add("calamitydate" , date.toString());
                jb2.add("calamitydanger" , calamitydanger);
                jb2.add("regionid", regionid);
                jb2.add("personid", personid);
                
                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }

    @Override
    public boolean insertLog(int personid, String description) 
    {
        try
        {
            super.connectToDatabase();
        }
        catch(Exception e)
        {
            return false;
        }
        try
        {
            String query = "INSERT INTO log (logdate, logdescription, personid)" 
                    + " values (?,?,?)";
            PreparedStatement prest = conn.prepareStatement(query);
            
            Calendar calendar = new GregorianCalendar();

            java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());
            
            prest.setTimestamp(1, timestamp);          
            prest.setString(2, description);
            prest.setInt(3, personid);
            
            prest.execute();
            
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }

    @Override
    public String retrieveLogs(int personid) 
    {
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM log WHERE personid = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, personid);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int logid = res.getInt("logid");
                Date logdate = res.getDate("logdate");
                String logdescription = res.getString("logdescription");
                int personid2 = res.getInt("personid");
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("logid" , logid);
                jb2.add("logdate" , logdate.toString());
                jb2.add("logdescription" , logdescription);
                jb2.add("personid" , personid2);
                
                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    private boolean insertFile(String name, String path, String extension)
    {
        int idfile_type = 0;
        
        if(!this.checkFileTypeID(extension))
        {
             this.insertFileType(extension);
        }
         
        idfile_type = this.retrieveFileTypeID(extension);
        
        if(name != "" || path != "")
        {
            try
            {
                super.connectToDatabase();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "INSERT INTO file (idfile_type, name, path)" 
                        + " values (?,?,?)";
                PreparedStatement prest = conn.prepareStatement(query);

                prest.setInt(1, idfile_type);
                prest.setString(2, name);
                prest.setString(3, path);

                prest.execute();

                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                super.disconnectFromDatabase();
            }
            
         }
         else
         {
             return false;
         }
    }
    
    /**
     * RETURN 0 WHEN FAILS
     * @param extension
     * @return 
     */
    private int retrieveFileTypeID(String extension)
    {
        String filetype = this.getFileType(extension);
        int filetypeid = 0;
        
        if(filetype != "ERROR")
        {
            try
            {
                super.connectToDatabase();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "SELECT idfile_type FROM file_type WHERE extension = ? AND type = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setString(1, extension);
                prest.setString(2, filetype);

                prest.execute();

                ResultSet res = prest.getResultSet();

                while(res.next())
                {                
                    filetypeid = res.getInt("idfile_type"); 
                }
                
                return filetypeid;
            }
            catch(SQLException ee)
            {
                return 0;
            }   
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * RETURN 0 WHEN FAILS
     * @param filepath
     * @param name
     * @return 
     */
    private int retrieveFileID(String filepath, String name, int idfile_type)
    {
        int fileid = 0;
        
        if(filepath != "" || name != "" || idfile_type != 0)
        {
            try
            {
                super.connectToDatabase();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "SELECT idfile FROM file WHERE path = ? AND name = ? AND idfile_type = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setString(1, filepath);
                prest.setString(2, name);
                prest.setInt(3, idfile_type);

                prest.execute();

                ResultSet res = prest.getResultSet();

                while(res.next())
                {                
                    fileid = res.getInt("idfile"); 
                }
                
                return fileid;
            }
            catch(SQLException ee)
            {
                return 0;
            }   
        }
        else
        {
            return 0;
        }
    }
    
    private boolean checkFileTypeID(String extension)
    {
        if(extension != "")
        {
            try
            {
                super.connectToDatabase();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "SELECT * FROM file_type WHERE extension = ? AND type = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setString(1, extension);
                prest.setString(2, this.getFileType(extension));

                prest.execute();

                ResultSet res = prest.getResultSet();

                while(res.next())
                {                
                    return true;
                }
               
            }
            catch(SQLException ee)
            {
                return false;
            }   
        }
        else
        {
            return false;
        }
        
        return false;
    }
    
    private boolean checkFile(String filepath, String name, String extension)
    {
        int idfile_type = 0;
        
        if(this.checkFileTypeID(extension))
        {
            idfile_type = this.retrieveFileTypeID(extension);
        }
        else
        {
            return false;
        }
        
        if(filepath != "" || name != "" || extension != "")
        {
            try
            {
                super.connectToDatabase();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "SELECT * FROM file WHERE path = ? AND name = ? AND idfile_type = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setString(1, filepath);
                prest.setString(2, name);
                prest.setInt(3, idfile_type);

                prest.execute();

                ResultSet res = prest.getResultSet();

                while(res.next())
                {                
                    return true;
                }
                
                return false;
            }
            catch(SQLException ee)
            {
                return false;
            }   
        }
        else
        {
            return false;
        }
    }
    
    private boolean insertFileType(String extension)
    {
        String filetype = this.getFileType(extension);
        
        if(filetype != "ERROR")
        {
            try
            {
                super.connectToDatabase();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "INSERT INTO file_type (type, extension)" 
                        + " values (?,?)";
                PreparedStatement prest = conn.prepareStatement(query);

                prest.setString(1, filetype);
                prest.setString(2, extension);

                prest.execute();

                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                super.disconnectFromDatabase();
            }
        }
        else
        {
            return false;
        }
    }
    
    private String getFileType(String extension)
    {
        switch(extension)
        {
            case ".jpg":
                return "Picture";
            case ".bmp":
                return "Picture";
            case ".png":
                return "Picture";
            case ".mp3":
                return "Audio";
            case ".wav":
                return "Audio";
        }
        
        return "ERROR";
    }

    @Override
    public boolean insertMessage(int sender_id, int receiver_id, String message, File file) 
    {
        if(file != null)
        {
            String absolutefilepath = file.getAbsolutePath();
            String extension = absolutefilepath.substring(absolutefilepath.lastIndexOf('.'));
            String name = absolutefilepath.substring(absolutefilepath.lastIndexOf('/') + 1);
            name = name.substring(0, name.length()-extension.length());
            String path = absolutefilepath.substring(0, absolutefilepath.length() - name.length() - extension.length());
        
            int idfile = 0;

            if(!this.checkFile(path, name, extension))
            {
                this.insertFile(name, path, extension);
            }

            idfile = this.retrieveFileID(path, name, this.retrieveFileTypeID(extension));

            try
            {
                super.connectToDatabase();
            }

            catch(Exception e)
            {
                return false;
            }
            try
            {
                String query = "INSERT INTO message (messagesender, messagereceiver, messagestring, messagefileid, messagedate)" 
                        + " values (?,?,?,?, ?)";
                PreparedStatement prest = conn.prepareStatement(query);
                
                 Calendar calendar = new GregorianCalendar();

                java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

                prest.setInt(1, sender_id);
                prest.setInt(2, receiver_id);
                prest.setString(3, message);
                prest.setInt(4, idfile);
                prest.setTimestamp(5, timestamp); 

                prest.execute();

                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                super.disconnectFromDatabase();
            }
        }
        else if(file == null)
        {      
            try
            {
                super.connectToDatabase();
            }

            catch(Exception e)
            {

            }
            try
            {
                String query = "INSERT INTO message (messagesender, messagereceiver, messagestring, messagedate)" 
                        + " values (?,?,?,?)";
                PreparedStatement prest = conn.prepareStatement(query);
                
                 
                 Calendar calendar = new GregorianCalendar();

                java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

                prest.setInt(1, sender_id);
                prest.setInt(2, receiver_id);
                prest.setString(3, message);
                prest.setTimestamp(4, timestamp);

                prest.execute();

                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                super.disconnectFromDatabase();
            }
        }
        
        return false;
    }

    @Override
    public String retrieveMessages(int sender_id, int receiver_id) 
    {       
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM message WHERE messagereceiver = ? and messagesender = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.setInt(2, sender_id);
                prest.setInt(1, receiver_id);
                
            prest.execute();
            
            ResultSet res = prest.getResultSet();
                
            while(res.next())
            {                
                int messageid = res.getInt("messageid");
                int messagereceiver = res.getInt("messagereceiver");
                int messagesender = res.getInt("messagesender");
                int messagefileid = res.getInt("messagefileid");
                String messagestring = res.getString("messagestring");
                Date messagedate = res.getDate("messagedate");
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("messageid", messageid);
                jb2.add("messagereceiver", messagereceiver);
                jb2.add("messagesender", messagesender);
                jb2.add("messagefileid", messagefileid);
                jb2.add("messagestring", messagestring);
                jb2.add("messagedate", messagedate.toString());

                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public File retrieveFileWithID(int fileid)
    {
        File retrievedfile = null;
        
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            String query = "SELECT * from FILE f, FILE_TYPE fi WHERE idfile = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, fileid);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                String path = res.getString("path");
                String extension = res.getString("extension");
                String name = res.getString("name");
                
                retrievedfile = new File(path + name + extension);
            }
            
            return retrievedfile;
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }   
    }

    private boolean checkInformationType(String type)
    {
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return false;      
        }
        
        try
        {
            String query = "SELECT * FROM information_type WHERE type = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, type);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                return true;
            }
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return false;
    }
    
    private boolean insertInformationType(String type)
    {
        try
        {
            super.connectToDatabase();
        }
        catch(Exception e)
        {
            return false;
        }
        try
        {
            String query = "INSERT INTO information_type (type)" 
                    + " values (?)";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.setString(1, type);  
            
            prest.execute();
            
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public boolean insertInformation(int id_calamity, String type, String description, File file, int id_civilian, int id_emergency) 
    {
        String absolutefilepath = file.getAbsolutePath();
        String extension = absolutefilepath.substring(absolutefilepath.lastIndexOf('.'));
        String name = absolutefilepath.substring(absolutefilepath.lastIndexOf('/') + 1);
        name = name.substring(0, name.length()-extension.length());
        String path = absolutefilepath.substring(0, absolutefilepath.length() - name.length() - extension.length());
        
        int idfile = 0;

        
        if(id_emergency != 0)
        {
            if(!this.checkFile(path, name, extension))
            {
                this.insertFile(name, path, extension);
            }

            idfile = this.retrieveFileID(path, name, this.retrieveFileTypeID(extension));
            
            if(!this.checkInformationType(type))
            {
                this.insertInformationType(type);
            }
            
            try
            {
                super.connectToDatabase();
            }

            catch(Exception e)
            {
                   return false;
            }
            try
            {
                String query = "INSERT INTO information (idcalamity, idinformation_type, description, idfile, idemergency_service)" 
                        + " values (?,(SELECT idinformation_type FROM information_type WHERE type = ?),?,?,?)";
                PreparedStatement prest = conn.prepareStatement(query);

                prest.setInt(1, id_calamity);
                prest.setString(2, type);
                prest.setString(3, description);
                prest.setInt(4, idfile);
                prest.setInt(5, id_emergency);

                prest.execute();

                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                super.disconnectFromDatabase();
            }
        }
        else if(id_civilian != 0)
        {
            if(!this.checkFile(path, name, extension))
            {
                this.insertFile(name, path, extension);
            }

            idfile = this.retrieveFileID(path, name, this.retrieveFileTypeID(extension));
            
            if(!this.checkInformationType(type))
            {
                this.insertInformationType(type);
            }
            
            try
            {
                super.connectToDatabase();
            }

            catch(Exception e)
            {
                return false;
            }
            try
            {
                String query = "INSERT INTO information (idcalamity, idinformation_type, description, idfile, idcivilian)" 
                        + " values (?,(SELECT idinformation_type FROM information_type WHERE type = ?),?,?,?)";
                PreparedStatement prest = conn.prepareStatement(query);

                prest.setInt(1, id_calamity);
                prest.setString(2, type);
                prest.setString(3, description);
                prest.setInt(4, idfile);
                prest.setInt(5, id_civilian);

                prest.execute();

                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                super.disconnectFromDatabase();
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public ArrayList<String> retrieveInformation(int id_calamity)
    {
        ArrayList<String> information = new ArrayList<String>();
        
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            String query = "SELECT * FROM INFORMATION I, INFORMATION_TYPE II WHERE idcalamity = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id_calamity);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                String description = res.getString("description");
                int idfile = res.getInt("idfile");
                String type = res.getString("type");
                
                String result = "-CALAMITY_ID:" + id_calamity + "-DESCRIPTION:" + description + "-TYPE:" + type + "-FILEID:" + idfile;
                
                information.add(result);
            }
            
            return information;
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }

    /**
     * INSERT A REGION IF IT DOESN'T EXIST
     * @param region 
     */
    private void insertRegion(String region) 
    {
        try
        {
            super.connectToDatabase();
        }
        catch(Exception e)
        {
            
        }
        try
        {
            String query = "INSERT INTO region (regionname)" 
                    + " values (?)";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.setString(1, region);          
            
            prest.execute();
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    /**
     * CHECK FOR REGION, IF EXISTS RETURN TRUE ELSE FALSE
     * @param region
     * @return 
     */
    private boolean checkRegion(String region)
    {
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return false;      
        }
        
        try
        {
            String query = "SELECT * FROM region WHERE regionname = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, region);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                return true;
            }
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return false;
    }
    
    /**
     * CHECK FOR REGION, IF EXISTS RETURN TRUE ELSE FALSE
     * @param region
     * @return 
     */
    private boolean checkAdress(String Street, String City, String Postal, String Region)
    {
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return false;      
        }
        
        try
        {
            String query = "SELECT * FROM address WHERE addressstreet = ? AND addresscity = ? AND addresspostal = ? AND regionid = (SELECT regionid from region where regionname = ?)";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, Street);
            prest.setString(2, City);
            prest.setString(3, Postal);
            prest.setString(4, Region);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                return true;
            }
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return false;
    }

    /**
     * INSERT A REGION IF IT DOESN'T EXIST
     * @param region 
     */
    private void insertAddress(String Street, String City, String Postal, String Region) 
    {
        try
        {
            super.connectToDatabase();
        }
        catch(Exception e)
        {
            
        }
        try
        {
            String query = "INSERT INTO address (addressstreet, addresscity, addresspostal, regionid)" 
                    + " values (?,?,?, (SELECT regionid from region where regionname = ?))";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.setString(1, Street);  
            prest.setString(2, City); 
            prest.setString(3, Postal); 
            prest.setString(4, Region); 
            
            prest.execute();
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public String retrieveRegions() 
    {        
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return "";    
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM region";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int regionid = res.getInt("regionid");
                String regionname = res.getString("regionname");
                
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("regionid" , regionid);    
                jb2.add("regionname", regionname);
                
                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public boolean insertLocation(String name, String geo_long, String geo_lat, int locationtypeid)
    {
        try
        {
            super.connectToDatabase();
        }
        catch(Exception e)
        {
            
        }
        try
        {
            String query = "INSERT INTO servicelocation (servicelocationlatitude, servicelocationlongtitude, servicelocationname, servicelocationtypeid)" 
                    + " values (?,?,?,?)";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.setString(3, name);  
            prest.setString(2, geo_long); 
            prest.setString(1, geo_lat); 
            prest.setInt(4, locationtypeid);
            
            prest.execute();
            
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public String retrieveLocations()
    {
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM servicelocation s, servicelocationtype sl WHERE s.servicelocationtypeid = sl.servicelocationtypeid";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
                
            while(res.next())
            {                
                int servicelocationid = res.getInt("servicelocationid");
                String servicelocationlatitude = res.getString("servicelocationlatitude");
                String servicelocationlongtitude = res.getString("servicelocationlongtitude");
                String servicelocationname = res.getString("servicelocationname");
                int servicelocationtypeid = res.getInt("servicelocationtypeid");
                String servicelocationtype = res.getString("servicelocationtype");
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("servicelocationid" , servicelocationid);   
                jb2.add("servicelocationlatitude", servicelocationlatitude);
                jb2.add("servicelocationlongtitude", servicelocationlongtitude);
                jb2.add("servicelocationname", servicelocationname);
                jb2.add("servicelocationtypeid", servicelocationtypeid);
                jb2.add("servicelocationtype", servicelocationtype);

                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }

    @Override
    public String getPersonInformation(int personId) 
    {
        String query = "SELECT * FROM person p, address a, region r, persontype pt WHERE p.addressid = a.addressid AND a.regionid = r.regionid AND p.persontypeid = pt.persontypeid AND personid = ?";
        
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;
        }
        
        try
        {
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, personId);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                String firstname = res.getString("personfirstname");
                String lastname = res.getString("personlastname");
                String middlename = res.getString("personmiddlename");
                String username = res.getString("personusername");
                String password = res.getString("personpassword");
                String ssn = res.getString("personssn");
                String email = res.getString("personemail");
                java.sql.Date birthdayDate = res.getDate("personbirthdate");
                String birthday = birthdayDate.toString();
                String date = birthday;
                String phone = res.getString("personphone");
                String subscribed = res.getString("personsubscribed");
                String configurator = res.getString("personconfigurator");
                String addressstreet = res.getString("addressstreet");
                String addresscity = res.getString("addresscity");
                String addresspostal = res.getString("addresspostal");
                String regionname = res.getString("regionname");
                String persontype = res.getString("persontype");

                JsonObjectBuilder jb = Json.createObjectBuilder();
                jb.add("personid" , personId);
                jb.add("firstname", firstname);
                jb.add("lastname", lastname);
                jb.add("middlename", middlename);
                jb.add("username", username);
                jb.add("password", password);
                jb.add("ssn", ssn);
                jb.add("email", email);
                jb.add("date", birthday);
                jb.add("phone", phone);
                jb.add("subscribed", subscribed);
                jb.add("configurator", configurator);
                jb.add("addressstreet", addressstreet);
                jb.add("addresscity", addresscity);
                jb.add("addresspostal", addresspostal);
                jb.add("regionname", regionname);
                jb.add("persontype", persontype);
                
                JsonObject jo = jb.build();
                
                return jo.toString();
            }
            
            
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return null;
    }

    @Override
    public String getCalamityFromRegion(int regionId) 
    {       
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM calamity where regionid = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.setInt(1, regionId);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
                       
            while(res.next())
            {                
                int id_calamity = res.getInt("calamityid");
                String geo_long = res.getString("calamitylongtitude");
                String geo_lat = res.getString("calamitylatitude");
                String name = res.getString("calamityname");
                String description = res.getString("calamitydescription");
                Date date = res.getDate("calamitydate");
                String calamitydanger = res.getString("calamitydanger");
                int regionid = res.getInt("regionid");
                int personid = res.getInt("personid");
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("calamityid" , id_calamity);
                jb2.add("calamitylongtitude" , geo_long);
                jb2.add("calamitylatitude" , geo_lat);
                jb2.add("calamityname" , name);
                jb2.add("calamitydescription" , description);
                jb2.add("calamitydate" , date.toString());
                jb2.add("calamitydanger" , calamitydanger);
                jb2.add("regionid", regionid);
                jb2.add("personid", personid);
                
                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
    }

    @Override
    public String getCalamityFromRegionDetailed(int regionId) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String retrieveLocationTypes() {
        try
        {
            super.connectToDatabase();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM servicelocationtype";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
                
            while(res.next())
            {                
                int servicelocationtypeid = res.getInt("servicelocationtypeid");
                String servicelocationtype = res.getString("servicelocationtype");
                
                JsonObjectBuilder jb2 = Json.createObjectBuilder();
                jb2.add("servicelocationtypeid", servicelocationtypeid);
                jb2.add("servicelocationtype", servicelocationtype);

                jb.add(jb2);
                count++;
            }
            
            JsonArray jo = jb.build(); 
            return jo.toString();
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }    }
    
    private void insertstatements(PreparedStatement e)
    {
        statements.add(e);
    }
    
    public Boolean connectingInsert()
    {
        connectiedatabase1 = super.connectToDatabase();
        connectiedatabase2 = super.connectToDatabase2();
        if (connectiedatabase1 == false || connectiedatabase2 == false) {
            updateconndatabase1 = connectiedatabase1;
            updateconndatabase2 = connectiedatabase2;
        }  
        updateStateDatabase();
        if(connectiedatabase1 && connectiedatabase2)
        {
            return true;
        }
        return false;
        
                
    }
    
    private void connectingSelect()
    {
        

        connectiedatabase1 = super.connectToDatabase();
        if(connectiedatabase1 == false)
        {   
            connectiedatabase2 = super.connectToDatabase2();
            updateconndatabase1 = connectiedatabase1;
        }
        updateStateDatabase();
    }
    
    private void updateStateDatabase()
    {
        if (updateconndatabase1 == false && connectiedatabase1 == true && statements.size() > 0) {
            try {
                updateconndatabase1 = true;
                super.connectToDatabase();
                for(PreparedStatement p : statements)
                {
                    PreparedStatement prest = p;
                    prest.execute();
                }
                statements.clear();
            } catch (Exception e) {

            }
        }
        if (updateconndatabase2 == false && connectiedatabase2 == true && statements.size() > 0) {
            try {
                updateconndatabase2 = true;
                super.connectToDatabase2();
                 for(PreparedStatement p : statements)
                {
                    PreparedStatement prest = p;
                    prest.execute();
                }
                statements.clear();
            } catch (Exception e) {

            }
        }
    }
}

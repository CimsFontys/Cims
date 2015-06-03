/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
/**
 *
 * @author Michael & Merijn 
 */
public class SQL extends DatabaseConnector implements IDatabase
{    
    ArrayList<String> statements = new ArrayList<>();
    Boolean updateconndatabase1 = true;
    Boolean updateconndatabase2 = true;
    Boolean allreadyupdated = false;
    
    int counter = 0;
    
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
            this.connectingSelect();
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

                JsonArrayBuilder ja = Json.createArrayBuilder();
                JsonObjectBuilder jb = Json.createObjectBuilder();
                jb.add("personid" , personId);
                jb.add("persontypeid", persontypeid);
                jb.add("personconfigurator", personconfigurator);
                
                ja.add(jb);
                JsonArray array = ja.build();
                
                return array.toString();
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
                this.connectingInsert();
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
           if(!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated)
            {
                String save = "INSERT INTO person (personfirstname, personlastname, personmiddlename, personusername, personpassword, personssn, personemail, personbirthdate, addressid, personphone, persontypeid, personconfigurator)" 
                    + " values ('" + first_name + "','" + last_name + "','"+ middle_name + "','" + username + "','" + password + "',' " + SSN + "',' " + email + " ',' " + timestamp + "',(SELECT addressid FROM address WHERE addressstreet = '" + Street + "' AND addresscity = '" + City + "'  AND addresspostal =  '" + Postal + "'  and regionid = (SELECT regionid FROM region WHERE regionname = '"+ Region + "')),'" + phonenumber + "',"+ idpersonal_type + " ,'" + configurator + "')";
                 statements.add(save);
                allreadyupdated = true;
            }
            else if(allreadyupdated)
            {
                allreadyupdated = false;
            }
            
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            if(counter == 1)
            {
                insertPerson(idpersonal_type,first_name,last_name,middle_name,username,password,SSN,email,Birthdate,phonenumber,Street,City,Postal,Region,configurator);
            }
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
            this.connectingInsert();
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
            if(!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated)
            {
                String save = "INSERT INTO calamity (calamitylatitude, calamitylongtitude, calamityname, calamitydescription ,  calamitydate , personid, calamitydanger, regionid)" 
                    + " values ('" + latitude + "','" + longtitude + "','" + name + "','" + description + "','" + Timestamp + "'," + personid
                    + ",'" + calamitydanger + "',(SELECT regionid FROM region WHERE regionname = '"+ region + "' ))";
                statements.add(save);
                allreadyupdated = true;
            }
            else if(allreadyupdated)
            {
                allreadyupdated = false;
            }
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            if(counter == 1)
            {
                insertCalamity(longtitude,latitude,personid,name,description,timestamp,calamitydanger,region);
            }
            super.disconnectFromDatabase();
        }
    }

    @Override
    public String retrieveCalamityWithPersonID(int personid) 
    {        
        try
        {
            this.connectingSelect();
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
            this.connectingSelect();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            JsonObjectBuilder jb = Json.createObjectBuilder();
            JsonArrayBuilder ja = Json.createArrayBuilder();
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
                
                ja.add(jb);
                JsonArray array = ja.build();
                
                return array.toString();
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
            this.connectingSelect();
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
    public String retrieveAllPersons() 
    {        
        try
        {
            this.connectingSelect();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            String query = "SELECT * FROM person p, region r , address a where p.addressid = a.addressid AND a.regionid = r.regionid";
            PreparedStatement prest = conn.prepareStatement(query);
            
            JsonArrayBuilder ja = Json.createArrayBuilder();
                
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {         
                JsonObjectBuilder jb = Json.createObjectBuilder();
            
                int personId = res.getInt("personid");
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
                
                ja.add(jb);     
            }
            
            JsonArray array = ja.build();
            return array.toString();
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
    public String retrieveAllCalamitiesDetailed() 
    {        
        try
        {
            this.connectingSelect();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "SELECT * FROM region r, calamity c, calamityinformation ci where c.calamityid = ci.calamityid AND r.regionid = c.regionid";
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
                String calamityinformationtype = res.getString("calamityinformationtype");
                String calamityinformationdescription = res.getString("calamityinformationdescription");
                String regionname = res.getString("regionname");
                
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
                jb2.add("calamityinformationtype", calamityinformationtype);
                jb2.add("calamityinformationdescription", calamityinformationdescription);
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
    public boolean insertLog(int personid, String description) 
    {
        try
        {
            this.connectingInsert();
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
            if(!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated)
            {
                String save = "INSERT INTO log (logdate, logdescription, personid)" 
                    + " values ('" + timestamp + "' , '" + description + "' ," + personid +")";
                statements.add(save);
                allreadyupdated = true;
            }
            else if(allreadyupdated)
            {
                allreadyupdated = false;
            }
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            if(counter == 1)
            {
                insertLog(personid,description) ;
            }
            super.disconnectFromDatabase();
        }
    }

    @Override
    public String retrieveLogs(int personid) 
    {
        try
        {
            this.connectingSelect();
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
        
        if(name != "" || path != "")
        {
            try
            {
                this.connectingInsert();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "INSERT INTO file (filename, filepath, fileextension)" 
                        + " values (?,?,?)";
                PreparedStatement prest = conn.prepareStatement(query);

                prest.setString(1, name);
                prest.setString(2, path);
                prest.setString(3, extension);

                prest.execute();
                if(!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated)
                {
                    String save = "INSERT INTO file (filename, filepath, fileextension)" 
                        + " values ('"+ name + "','" + path + "','" + extension + "')";
                    statements.add(save);
                    allreadyupdated = true;
                }
                else if(allreadyupdated)
                {
                    allreadyupdated = false;
                }

                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                if(counter == 1)
                {
                insertFile(name,path,extension) ;
                }
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
     * @param filepath
     * @param name
     * @return 
     */
    private int retrieveFileID(String filepath, String name)
    {
        int fileid = 0;
        
        if(filepath != "" || name != "")
        {
            try
            {
                this.connectingSelect();
            }
            catch(Exception e)
            {
            
            }
            try
            {
                String query = "SELECT fileid FROM file WHERE filepath = ? AND filename = ?";
                PreparedStatement prest = conn.prepareStatement(query);
                prest.setString(1, filepath);
                prest.setString(2, name);

                prest.execute();

                ResultSet res = prest.getResultSet();

                while(res.next())
                {                
                    fileid = res.getInt("fileid"); 
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
    
    private boolean checkFile(String filepath, String name, String extension)
    {                
        if(filepath != "" || name != "" || extension != "")
        {
            try
            {
                this.connectingSelect();
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
                prest.setString(3, extension);

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

            idfile = this.retrieveFileID(path, name);

            try
            {
              this.connectingInsert();
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
                 if (!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated) {
                    String save = "INSERT INTO message (messagesender, messagereceiver, messagestring, messagefileid, messagedate)"
                        + " values ("+ sender_id +"," + receiver_id + ",'" + message + "'," + idfile + ",'" + timestamp + "' )";
                    statements.add(save);
                    allreadyupdated = true;
                } else if (allreadyupdated) {
                    allreadyupdated = false;
                }
                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                if(counter == 1)
                {
                    insertMessage(sender_id,receiver_id,message,file); 
                }
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
                if (!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated) {
                    String save = "INSERT INTO message (messagesender, messagereceiver, messagestring, messagedate)"
                        + " values (" + sender_id +"," + receiver_id + ",'" + message + "','" + timestamp +"')";
                    statements.add(save);
                    allreadyupdated = true;
                } else if (allreadyupdated) {
                    allreadyupdated = false;
                }
                return true;
            }
            catch(SQLException ee)
            {
                return false;
            }
            finally
            {
                if(counter == 1)
                {
                    insertMessage(sender_id,receiver_id,message,file); 
                }
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
            this.connectingSelect();
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
            this.connectingSelect();
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

    
    @Override
    public boolean insertInformation(int id_calamity, String calamitytype, String calamitydescription, String filepath, String filename, String fileextension, int personid) {
        int fileid = 0;
        boolean fileIsNull = false;
        
        if (filepath.equals("") || filename.equals("") || fileextension.equals("")) 
        {
            fileIsNull = true;
        } 
        else {
            if (this.insertFile(filename, filepath, fileextension)) {
                fileid = this.retrieveFileID(filepath, filename);
            } else {
                return false;
            }
        }

        try {
            this.connectingInsert();
        } catch (Exception e) {
            return false;
        }
        try {
            String query = "INSERT INTO calamityinformation (calamityid, calamityinformationtype, calamityinformationdescription, fileid, personid)"
                    + " values (?,?,?,?,?)";
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setInt(1, id_calamity);
            prest.setString(2, calamitytype);
            prest.setString(3, calamitydescription);
            
            if(fileIsNull)
            {
                prest.setNull(4, Types.INTEGER);
            }
            else
            {
                prest.setInt(4, fileid);
            }   
            prest.setInt(5, personid);

            prest.execute();
           if (!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated) {
               String save;
               if(fileIsNull)
               {
                save = "INSERT INTO information (idcalamity, idinformation_type, description, idfile, idemergency_service)"
                        + " values (" + id_calamity + ",'" + calamitytype + "','" + calamitydescription + "'," + fileid + "," + personid + ")";
               }
               else
               {
                save = "INSERT INTO information (idcalamity, idinformation_type, description, idfile, idemergency_service)"
                        + " values (" + id_calamity + ",'" + calamitytype + "','" + calamitydescription + "'," + 0 + "," + personid + ")";
                   
               }
               statements.add(save);
                    allreadyupdated = true;
                } else if (allreadyupdated) {
                    allreadyupdated = false;
                }
            return true;
        } catch (SQLException ee) {
            return false;
        } finally {
            if(counter == 1)
                {
                    insertInformation(id_calamity,calamitytype,calamitydescription,filepath,filename,fileextension,personid)  ;
                }
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public String retrieveInformation(int id_calamity)
    {        
        try
        {
            this.connectingSelect();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            String query = "select * from region r, calamity c, calamityinformation i where c.calamityid = i.calamityid AND c.regionid = r.regionid AND c.calamityid = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id_calamity);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int calamityid = res.getInt("calamityid");
                String calamitylatitude = res.getString("calamitylatitude");
                String calamitylongtitude = res.getString("calamitylongtitude");
                String calamityname = res.getString("calamityname");
                String calamitydescription = res.getString("calamitydescription");
                java.sql.Date birthdayDate = res.getDate("calamitydate");
                String birthday = birthdayDate.toString();
                String calamitydate = birthday;
                int personid = res.getInt("personid");
                String calamitydanger = res.getString("calamitydanger");
                String calamityinformationtype = res.getString("calamityinformationtype");
                String calamityinformationdescription = res.getString("calamityinformationdescription");
                String regionname = res.getString("regionname");

                JsonArrayBuilder ja = Json.createArrayBuilder();
                JsonObjectBuilder jb = Json.createObjectBuilder();
                jb.add("calamityid" , calamityid);
                jb.add("calamitylatitude", calamitylatitude);
                jb.add("calamitylongtitude", calamitylongtitude);
                jb.add("calamityname", calamityname);
                jb.add("calamitydescription", calamitydescription);
                jb.add("calamitydate", calamitydate);
                jb.add("personid", personid);
                jb.add("calamitydanger", calamitydanger);
                jb.add("calamityinformationtype", calamityinformationtype);
                jb.add("calamityinformationdescription", calamityinformationdescription);
                jb.add("regionname", regionname);
                
                ja.add(jb);
                JsonArray array = ja.build();
                
                return array.toString();
            }
           
        }
        catch(SQLException ee)
        {
            return null;
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return "";
    }

    /**
     * INSERT A REGION IF IT DOESN'T EXIST
     * @param region 
     */
    private void insertRegion(String region) 
    {
        try
        {
            this.connectingInsert();
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
            if(!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated)
            {
                String save = "INSERT INTO region (regionname) values ('" + region + " ')";
                statements.add(save);
                allreadyupdated = true;
            }
            else if(allreadyupdated)
            {
                allreadyupdated = false;
            }
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            if(counter == 1)
            {
                insertRegion(region);
            }
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
            this.connectingSelect();
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
            this.connectingSelect();
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
            this.connectingInsert();
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
            if(!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated)
            {
                String save = "INSERT INTO address (addressstreet, addresscity, addresspostal, regionid)" 
                    + " values ('" + Street + "','" + City +  "','" + Postal + "',(SELECT regionid from region where regionname = '" + Region + "'))";
                statements.add(save);
                allreadyupdated = true;
            }
            else if(allreadyupdated)
            {
                allreadyupdated = false;
            }
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            if(counter == 1)
            {
                insertAddress(Street,City,Postal, Region) ;
            }
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public String retrieveRegions() 
    {        
        try
        {
            this.connectingSelect();
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
            this.connectingInsert();
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
            if(!updateconndatabase1 && !allreadyupdated || !updateconndatabase2 && !allreadyupdated)
            {
                 String save = "INSERT INTO servicelocation (servicelocationlatitude, servicelocationlongtitude, servicelocationname, servicelocationtypeid)" 
                    + " values ('" + name + "', '" + geo_long + "', '" + geo_lat + "',"+ locationtypeid+")";
                statements.add(save);
                allreadyupdated = true;
            }
            else if(allreadyupdated)
            {
                allreadyupdated = false;
            }
            return true;
        }
        catch(SQLException ee)
        {
            return false;
        }
        finally
        {
            if(counter == 1)
            {
                insertLocation(name,geo_long,geo_lat,locationtypeid);
            }
            super.disconnectFromDatabase();
        }
    }
    
    @Override
    public String retrieveLocations()
    {
        try
        {
            this.connectingSelect();
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
            this.connectingSelect();
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

                JsonArrayBuilder ja = Json.createArrayBuilder();
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
                
                ja.add(jb);
                JsonArray array = ja.build();
                
                return array.toString();
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
            this.connectingSelect();
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
        try
        {
            this.connectingSelect();
        }
        catch (Exception e)
        {
            return null;     
        }
        
        try
        {
            int count = 0;
            JsonArrayBuilder jb = Json.createArrayBuilder();
            String query = "select * from calamity c, calamityinformation ci, region r where c.calamityid = ci.calamityid AND c.regionid = r.regionid AND c.regionid = ?";
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
                String calamityinformationtype = res.getString("calamityinformationtype");
                String calamityinformationdescription = res.getString("calamityinformationdescription");
                
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
                jb2.add("calamityinformationtype", calamityinformationtype);
                jb2.add("calamityinformationdescription", calamityinformationdescription);
                
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
    public String retrieveLocationTypes() {
        try
        {
            this.connectingSelect();
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
    
    private void insertstatements(String e)
    {
        statements.add(e);
    }
    
public Boolean connectingInsert()
    {
        if(counter == 1)
        {
            updateconndatabase2 = super.connectToDatabase2();
           counter = 0;
        }
        else{
            updateconndatabase1 = super.connectToDatabase();
            counter++;
        }
        updateStateDatabase();
        return true;
        
                
    }
    
    private void connectingSelect()
    {
        if(!super.connectToDatabase() || counter == 1)
        {   
            updateconndatabase2 = super.connectToDatabase2();
            updateconndatabase1 = false;
        }
        updateStateDatabase();
    }
    
    private void updateStateDatabase()
    {
        if (updateconndatabase1 == false && super.connectToDatabase() == true && statements.size() > 0) {
            try {
                updateconndatabase1 = true;
                super.connectToDatabase();
                for(String p : statements)
                {
                    PreparedStatement prest = conn.prepareStatement(p);
                    prest.execute();
                }
                statements.clear();
            } catch (Exception e) {

            }
        }
        if (updateconndatabase2 == false && super.connectToDatabase2() == true && statements.size() > 0) {
            try {
                updateconndatabase2 = true;
                super.connectToDatabase2();
                for(String p : statements)
                {
                    PreparedStatement prest = conn.prepareStatement(p);
                    prest.execute();
                }
                statements.clear();
            } catch (Exception e) {

            }
        }
        
        
    }
}

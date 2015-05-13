/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.database;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 *
 * @author Michael & Merijn 
 */
public class SQL extends DatabaseConnector implements IDatabase
{
    public SQL()
    {
        super();
    }

    @Override
    public boolean loginEmergencyService(String username, String password, int id) 
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
            String query = "SELECT * FROM emergency_service WHERE USERNAME = ? AND PASSWORD = ? AND IDPERSONAL_TYPE = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, username);
            prest.setString(2, password);
            prest.setInt(3, id);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                String usernameResult = res.getString("USERNAME");
                String passwordResult = res.getString("PASSWORD");
                int idResult = res.getInt("IDPERSONAL_TYPE");
                
                if(passwordResult.equals(""))
                {
                    return false;
                }
                if(usernameResult.equals(""))
                {
                    return false;
                }
                if(id != idResult)
                {
                    return false;
                }
                if(password.equals(passwordResult) && username.equals(usernameResult) && id == idResult)
                {
                    return true;
                }
            }
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return false;
    }

    @Override
    public boolean insertEmergencyService(int idpersonal_type, String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, Date Birthdate, String phonenumber, String Street, String City, String Postal, String Region) 
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
        }
        catch(Exception e)
        {
            return false;
        }
        try
        {
            String query = "INSERT INTO emergency_service (first_name, last_name, middle_name, username, password, ssn, email, birthdate, idaddress, phone, idpersonal_type)" 
                    + " values (?,?,?,?,?,?,?,?,(SELECT idaddress FROM address WHERE street = ? AND city = ? AND postal = ? and idregion = (SELECT idregion FROM region WHERE region = ?)),?,?)";
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
    public boolean loginCivilian(String username, String password) 
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
            String query = "SELECT * FROM civilian WHERE USERNAME = ? AND PASSWORD = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, username);
            prest.setString(2, password);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                String usernameResult = res.getString("USERNAME");
                String passwordResult = res.getString("PASSWORD");
                
                if(passwordResult.equals(""))
                {
                    return false;
                }
                if(usernameResult.equals(""))
                {
                    return false;
                }
                if(password.equals(passwordResult) && username.equals(usernameResult))
                {
                    return true;
                }
            }
        }
        catch(SQLException ee)
        {
            
        }
        finally
        {
            super.disconnectFromDatabase();
        }
        
        return false;
    }

    @Override
    public boolean insertCivilian(String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, Date Birthdate, String phonenumber, String Street, String City, String Postal, String Region) 
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
        }
        catch(Exception e)
        {
            return false;
        }
        try
        {
            String query = "INSERT INTO civilian (first_name, last_name, middle_name, username, password, ssn, email, birthdate, idaddress, phone)" 
                    + " values (?,?,?,?,?,?,?,?,(SELECT idaddress FROM address WHERE street = ? AND city = ? AND postal = ? and idregion = (SELECT idregion FROM region WHERE region = ?)),?)";
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
    public boolean insertCalamity(String geo_long, String geo_lat, int id_emergency_service, String name, String description, Date timestamp) 
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
            String query = "INSERT INTO calamity (goe_long, goe_lat, idemergency_service, name, description , date)" 
                    + " values (?,?,?,?,?,?)";
            PreparedStatement prest = conn.prepareStatement(query);
            
            Calendar calendar = new GregorianCalendar();

            java.sql.Timestamp Timestamp = new java.sql.Timestamp(timestamp.getTime());
            
            prest.setString(1, geo_long); 
            prest.setString(2, geo_lat); 
            prest.setInt(3, id_emergency_service);
            prest.setString(4, name);
            prest.setString(5, description);
            prest.setTimestamp(6, Timestamp);
            
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
    public ArrayList<String> retrieveCalamityWithIES(int id_emergency_service) 
    {
        ArrayList<String> calamities = new ArrayList<String>();
        
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
            String query = "SELECT * FROM calamity WHERE idemergency_service = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id_emergency_service);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int id_calamity = res.getInt("idcalamity");
                String geo_long = res.getString("goe_long");
                String geo_lat = res.getString("goe_lat");
                String name = res.getString("name");
                String description = res.getString("description");
                Date date = res.getDate("date");
                
                String calamity = "-ID_CALAMITY:" + id_calamity + "-GEO_LONG:" + geo_long + "-GEO_LAT:" + geo_lat + "-NAME:" + name + "-DESCRIPTION:" + description + "-DATE:" + date;
                System.out.println("-ID_CALAMITY:" + id_calamity + "-GEO_LONG:" + geo_long + "-GEO_LAT:" + geo_lat + "-NAME:" + name + "-DESCRIPTION:" + description + "-DATE:" + date);
                
                calamities.add(calamity);
            }
            
            return calamities;
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
    public ArrayList<String> retrieveAllCalamities() 
    {
        ArrayList<String> calamities = new ArrayList<String>();
        
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
            String query = "SELECT * FROM calamity";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int id_calamity = res.getInt("idcalamity");
                String geo_long = res.getString("goe_long");
                String geo_lat = res.getString("goe_lat");
                String name = res.getString("name");
                String description = res.getString("description");
                Date date = res.getDate("date");
                
                String calamity = "-ID_CALAMITY:" + id_calamity + "-GEO_LONG:" + geo_long + "-GEO_LAT:" + geo_lat + "-NAME:" + name + "-DESCRIPTION:" + description + "-DATE:" + date;
                System.out.println("-ID_CALAMITY:" + id_calamity + "-GEO_LONG:" + geo_long + "-GEO_LAT:" + geo_lat + "-NAME:" + name + "-DESCRIPTION:" + description + "-DATE:" + date);
                
                calamities.add(calamity);
            }
            
            return calamities;
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
    public boolean insertLog(int id_emergency_service, String description) 
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
            String query = "INSERT INTO log (time, description, idemergency_service)" 
                    + " values (?,?,?)";
            PreparedStatement prest = conn.prepareStatement(query);
            
            Calendar calendar = new GregorianCalendar();

            java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());
            
            prest.setTimestamp(1, timestamp);          
            prest.setString(2, description);
            prest.setInt(3, id_emergency_service);
            
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
    public ArrayList<String> retrieveLogs(int id_emergency_service) 
    {
        ArrayList<String> logs = new ArrayList<String>();
        
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
            String query = "SELECT * FROM log WHERE idemergency_service = ?";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, id_emergency_service);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                int idlog = res.getInt("idlog");
                Date time = res.getTimestamp("time");
                String description = res.getString("description");
                int id_emergency_service2 = res.getInt("idemergency_service");
                
                String result = "ID: " + idlog + " Time: " + time.toString() + " Description: " + description + " By: " + id_emergency_service2;
                System.out.println( "ID: " + idlog + " Time: " + time.toString() + " Description: " + description + " By: " + id_emergency_service2);
                
                logs.add(result); 
            }
            
            return logs;
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
            
        }
        try
        {
            String query = "INSERT INTO message (sender_id, receiver_id, message, idfile)" 
                    + " values (?,?,?,?)";
            PreparedStatement prest = conn.prepareStatement(query);
                        
            prest.setInt(1, sender_id);
            prest.setInt(2, receiver_id);
            prest.setString(3, message);
            prest.setInt(4, idfile);
            
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
    public ArrayList<String> retrieveMessage(int sender_id, int receiver_id) 
    {
        ArrayList<String> messages = new ArrayList<String>();
        
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
            String query = "SELECT * FROM message WHERE sender_id = ? AND receiver_id = ? ";
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setInt(1, sender_id);
            prest.setInt(2, receiver_id);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {                
                String message = res.getString("message");
                int fileid = res.getInt("idfile");
                
                String result = "-SENDER_ID:" + sender_id + "-RECEIVER_ID:" + receiver_id + "-MESSAGE:" + message + "-FILEID:" + fileid;
                
                messages.add(result);
            }
            
            return messages;
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
            String query = "INSERT INTO region (region)" 
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
            String query = "SELECT * FROM region WHERE region = ?";
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
            String query = "SELECT * FROM address WHERE street = ? AND city = ? AND postal = ? AND idregion = (SELECT idregion from region where region = ?)";
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
            String query = "INSERT INTO address (street, city, postal, idregion)" 
                    + " values (?,?,?, (SELECT idregion from region where region = ?))";
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
    public ArrayList<String> retrieveRegions() 
    {
        ArrayList<String> regions = new ArrayList<String>();
        
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
            String query = "SELECT * FROM region";
            PreparedStatement prest = conn.prepareStatement(query);
            
            prest.execute();
            
            ResultSet res = prest.getResultSet();
            
            while(res.next())
            {
                String region = res.getString("region");
                
                regions.add(region);
            }
            
            return regions;
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cims.startup;


import java.sql.*;
import java.util.logging.*;

/**
 *
 * @author Michael
 */
public class DatabaseConnector
{
    /**
     * Connection Variable
     */
    protected Connection conn;
    /**
     * DatabaseConnctor constructor
     * testing initial connection with
     * database
     */
    public DatabaseConnector() 
    {
        this.connectToDatabase();
    }
       /**
        * Connecting to the MYSQL Database
        */
    protected boolean connectToDatabase()
    {
        //IP ADRES FROM DATABASE
        //String url = "jdbc:mysql://localhost:3306/";
        String url = "jdbc:mysql://localhost:3306/";
        //DATABASE NAME
        String dbName = "mydb";
        //String dbName = "collectit";
        //DRIVER & LIBRARY MYSQL DRIVER
        String driver = "com.mysql.jdbc.Driver";
        //CIMS USERNAME DATABASE ACCESS
        String userName = "cims"; 
        //CIMS PASSWORD DATABASE ACCESS
        String password = "cims";
        
        try 
        {
            try 
            {
                Class.forName(driver);
            }
            catch(Exception e)
            {
                return false;
            }
        } 
        catch(Exception e)
        {
            return false;
        }
        try 
        {
            conn = DriverManager.getConnection(url+dbName,userName,password);
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    protected boolean connectToDatabase1()
    {
        //IP ADRES FROM DATABASE
        //String url = "jdbc:mysql://localhost:3306/";
        String url = "jdbc:mysql://localhost:3306/";
        //DATABASE NAME
        String dbName = "mydb";
        //String dbName = "collectit";
        //DRIVER & LIBRARY MYSQL DRIVER
        String driver = "com.mysql.jdbc.Driver";
        //CIMS USERNAME DATABASE ACCESS
        String userName = "cims"; 
        //CIMS PASSWORD DATABASE ACCESS
        String password = "cims";
        
        try 
        {
            try 
            {
                Class.forName(driver);
            }
            catch(Exception e)
            {
                return false;
            }
        } 
        catch(Exception e)
        {
            return false;
        }
        try 
        {
            conn = DriverManager.getConnection(url+dbName,userName,password);
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    protected boolean connectToDatabase2()
    {
        //IP ADRES FROM DATABASE
        //String url = "jdbc:mysql://localhost:3306/";
        String url = "jdbc:mysql://localhost:3306/";
        //DATABASE NAME
        String dbName = "mydb";
        //String dbName = "collectit";
        //DRIVER & LIBRARY MYSQL DRIVER
        String driver = "com.mysql.jdbc.Driver";
        //CIMS USERNAME DATABASE ACCESS
        String userName = "cims"; 
        //CIMS PASSWORD DATABASE ACCESS
        String password = "cims";
        
        try 
        {
            try 
            {
                Class.forName(driver);
            }
            catch(Exception e)
            {
                return false;
            }
        } 
        catch(Exception e)
        {
            return false;
        }
        try 
        {
            conn = DriverManager.getConnection(url+dbName,userName,password);
            return true;
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    /**
     * Used to disconnect safely from the database
     */
    protected void disconnectFromDatabase() 
    {
        try 
        {
            conn.close();
            conn = null;
        } 
        catch (SQLException ex) 
        {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Used for closing the DatabaseConnection
     */
    public void close()
    {
        this.disconnectFromDatabase();
    }

    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.database;


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
    protected void connectToDatabase()
    {
        //IP ADRES FROM DATABASE
        String url = "jdbc:mysql://145.144.240.224:3306/";
        //DATABASE NAME
        String dbName = "cimsdb";
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
                Class.forName(driver).newInstance();
            } 
            catch (InstantiationException ex) 
            {
                Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IllegalAccessException ex) 
            {
                Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        try 
        {
            conn = DriverManager.getConnection(url+dbName,userName,password);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (conn != null) 
        {
                System.out.println("Verbinding met database bewerkstelligd op IP: " + url + " - " + dbName);
	}
        else 
        {
		System.out.println("Failed to make connection to the Database on: " + url + " - " + dbName);
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

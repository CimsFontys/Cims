/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Database.SQL;
import java.io.File;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael
 */
public class DatabaseTests {
    
    public DatabaseTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddPerson()
    {
//        SQL sql = new SQL();
//        Date test = new Date();
//        
//        boolean testB = sql.insertPerson(1, "test2", "test2" ,"test2", "test2", "test2", "test2", "test2", test, "test2", "test2", "test2", "test2", "test2", "YES"); 
//        assertTrue(testB);
    }
    
    @Test
    public void testLoginPerson()
    {
        SQL sql = new SQL();
        String test = sql.loginPerson("michaelvaneck", "cims");
        System.out.println(test);
    }
    
    @Test
    public void testAddCalmity()
    {
//        SQL sql = new SQL();
//        Date test = new Date();
//        
//        boolean testB = sql.insertCalamity("50", "50", 2, "test2", "test2", test, "High", "test");
//        assertTrue(testB);
    }
    
    @Test
    public void testGetCalamityWithPersonID()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveCalamityWithPersonID(2));
    }
    
    @Test
    public void testGetCalamityWithID()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveCalamityWithID(1));
    }
    
    @Test
    public void testGetAllCalamities()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveAllCalamities());
    }
    
    @Test
    public void testGetCalamitiesFromRegion()
    {
        SQL sql = new SQL();
        System.out.println(sql.getCalamityFromRegion(13));
    }
    
    @Test
    public void testAddLog()
    {
        SQL sql = new SQL();
        boolean test = sql.insertLog(2, "test");
        assertTrue(test);
    }
    
    @Test
    public void testGetAllRegions()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveRegions());
    }
    
    @Test
    public void testInsertMessage()
    {
//        SQL sql = new SQL();
//        File file = null;
//        boolean test = sql.insertMessage(2, 4, "TEST MESSAGE", file);
//        assertTrue(test);
    }
    
    @Test
    public void testGetPersonInformation()
    {
        SQL sql = new SQL();
        System.out.println(sql.getPersonInformation(4));
    }
    
    @Test
    public void testGetLogs()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveLogs(2));
    }
    
    @Test
    public void testGetAllLocations()
    {   
        SQL sql = new SQL();
        System.out.println(sql.retrieveLocations());
    }
    
    @Test
    public void testInsertLocation()
    {
//        SQL sql = new SQL();
//        boolean test = sql.insertLocation("test", "50", "50", 2);
//        assertTrue(test);
    }
    
    @Test
    public void testGetLocationTypes()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveLocationTypes());
    }
    
    @Test
    public void testGetMessages()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveMessages(2, 4));
    }
    
    @Test
    public void testGetDetailedCalamity()
    {
        SQL sql = new SQL();
        System.out.println(sql.retrieveInformation(1));
    }
    
    @Test
    public void testGetCalamitiesDetailed()
    {
        SQL sql = new SQL();
        System.out.println("GET CALAMITIES DETAILED");
        System.out.println(sql.retrieveAllCalamitiesDetailed());
    }
}

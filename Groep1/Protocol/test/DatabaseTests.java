/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cims.startup.SQL;
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
//        boolean testB = sql.insertPerson(1, "test", "test" ,"test", "test", "test", "test", "test", test, "test", "test", "test", "test", "test", "YES"); 
//        assertTrue(testB);
    }
    
    @Test
    public void testLoginPerson()
    {
        SQL sql = new SQL();
        String test = sql.loginPerson("test", "test");
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
}

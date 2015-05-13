/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import FileTransfer.FileManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Merijn
 */
public class FileTest {
    
    public FileTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void saveAndLoadFile()
    {
        FileManager fm = FileManager.getInstance();
        String path = "";
        byte[] bFile = new byte[] {74,69,32,77,79,69,68,69,82};
        try {
            path = fm.saveFile(bFile, "test.txt");
        } catch (IOException ex) {
            Logger.getLogger(FileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        File file = new File(path);
        assertTrue(file.exists());
        byte[] bFileLoad = null;
        try {
            bFileLoad = fm.loadFile(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(bFile[0], bFileLoad[0]);
        assertEquals(bFile[1], bFileLoad[1]);
        assertEquals(bFile[2], bFileLoad[2]);
        assertEquals(bFile[3], bFileLoad[3]);
        assertEquals(bFile[4], bFileLoad[4]);
        assertEquals(bFile[5], bFileLoad[5]);
        assertEquals(bFile[6], bFileLoad[6]);
        assertEquals(bFile[7], bFileLoad[7]);
        assertEquals(bFile[8], bFileLoad[8]);
    }
    
}

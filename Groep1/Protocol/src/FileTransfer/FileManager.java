/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Merijn
 */
public class FileManager {
    
    private static final String LOC = "C:\\CIMSFiles\\";
    
    private static FileManager INSTANCE;
    
    private FileManager()
    {
        INSTANCE = this;
    }
    
    public static FileManager getInstance()
    {
        new FileManager();
        return INSTANCE; 
    }
    
    /**
     * saves the file on the server
     * @param file as byte[]
     * @param fileName String of filename + extension Example("test.txt") 
     * @return string with the file paht on the server 
     */
    public String saveFile(byte[] file, String fileName)
    {
        try {
            FileOutputStream fos = new FileOutputStream(LOC + fileName);
            try {
                fos.write(file);
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return LOC + fileName;
    }
    
    /**
     * loads the file form the specified file path and converts to byte[]
     * @param fullPath
     * @return the file as byte[]
     */
    public byte[] loadFile(String fullPath) throws FileNotFoundException
    {
        File file = new File(fullPath);
        byte[] bFile = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        try {
            fis.read(bFile);
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bFile;
    }
}

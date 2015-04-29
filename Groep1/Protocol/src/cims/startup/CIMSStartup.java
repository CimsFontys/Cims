/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cims.startup;

/**
 *
 * @author Michael
 */
public class CIMSStartup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        DatabaseConnector test = new DatabaseConnector();
        test.connectToDatabase();
    }
    
}

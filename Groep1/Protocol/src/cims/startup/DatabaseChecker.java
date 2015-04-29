/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cims.startup;

import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class DatabaseChecker
{
    private String ipdatabase1; //IP ADRESS VAN DATABASE 1
    private String ipdatabase2; //IP ADRESS VAN DATABASE 2
    private boolean isOnlinedb1;
    private boolean isOnlinedb2;
    
    private DatabaseConnector connector;
    
    private ArrayList<String> methodHistorydb = new ArrayList<>();
    
    private static DatabaseChecker INSTANCE;
    
    private DatabaseChecker()
    {
        INSTANCE = this;
        
        connector = new DatabaseConnector();
    }
    
    public void checkDBOnline1()
    {
        if(connector.connectToDatabase1())
        {
            isOnlinedb1 = true;
        }
    }
    
    public void checkDBOnline2()
    {
        if(connector.connectToDatabase2())
        {
            isOnlinedb2 = true;
        }
    }
    
    public static DatabaseChecker getInstance()
    {
        if(INSTANCE == null)
        {
            new DatabaseChecker();
        }
        return INSTANCE;
    }
       
    public boolean isIsOnlinedb1() {
        return isOnlinedb1;
    }

    public void setIsOnlinedb1(boolean isOnlinedb1) {
        this.isOnlinedb1 = isOnlinedb1;
    }
    
        public boolean isIsOnlinedb2() {
        return isOnlinedb2;
    }

    public void setIsOnlinedb2(boolean isOnlinedb2) {
        this.isOnlinedb2 = isOnlinedb2;
    }
    
    public ArrayList<String> getMethodHistorydb() {
        return methodHistorydb;
    }

    public void setMethodHistorydb(ArrayList<String> methodHistorydb) {
        this.methodHistorydb = methodHistorydb;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socketserver;

import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class ThreadAdministration 
{
    private static ThreadAdministration INSTANCE;
    private ArrayList<CIMSServer> threadList;
    
    private ThreadAdministration()
    {
        INSTANCE = this;
        threadList = new ArrayList<CIMSServer>();
    }
    
    public void addClient(CIMSServer client)
    {
        threadList.add(client);
    }
    
    public void removeClient(CIMSServer client)
    {
        threadList.remove(client);
    }
    
    public static ThreadAdministration getInstance()
    {
        if(INSTANCE == null)
        {
            new ThreadAdministration();
        }
        
        return INSTANCE;
    }
}

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
    private ArrayList<CIMSThread> currentThreads;
    
    private ThreadAdministration() 
    {
        currentThreads = new ArrayList<CIMSThread>();
    }
    
    public static ThreadAdministration getInstance() 
    {
        return ThreadAdministrationHolder.INSTANCE;
    }
    
    private static class ThreadAdministrationHolder {

        private static final ThreadAdministration INSTANCE = new ThreadAdministration();
    }
    
    public void addClient(CIMSThread server)
    {
        currentThreads.add(server);
        System.out.println("-------------------------------------------------------");
        System.out.println("CIMSServer: CIMSThread added to threadpool, current active Threads: " + currentThreads.size());
    }
    
    public void removeClient(CIMSThread server)
    {
        currentThreads.remove(server);
        System.out.println("-------------------------------------------------------");
        System.out.println("CIMSServer: CIMSThread removed from threadpool, current active Threads: " + currentThreads.size());
    }
    
    public CIMSThread findClient(int personid)
    {
        System.out.println("-------------------------------------------------------");
        System.out.println("CIMSServer: looking for requested Thread");
        for(CIMSThread c : currentThreads)
        {
            if(c.receiverID == personid)
            {
                System.out.println("-------------------------------------------------------");
                System.out.println("CIMSServer: thread located with unique id: " + c.receiverID);
                return c;
            }
        }
        
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socketserver;

import java.util.ArrayList;

/**
 *
 * @author Michael & Merijn
 */
public class ThreadAdministration 
{  
    /**
     * LIST OF ALL THE CURRENTLY CONNECTED THREADS ON THE CIMS SERVER
     */
    private ArrayList<CIMSThread> currentThreads;
    
    /**
     * SINGLETON CLASS FOR ADMINISTRATION
     */
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
    
    /**
     * ADDS A NEW THREAD TO THE CLIENT POOL
     * @param server 
     */
    public void addClient(CIMSThread server)
    {
        currentThreads.add(server);
        System.out.println("-------------------------------------------------------");
        System.out.println("CIMSServer: CIMSThread added to threadpool, current active Threads: " + currentThreads.size());
    }
    
    /**
     * REMOVES A LEAVING THREAD FROM THE CLIENT POOL
     * @param server 
     */
    public void removeClient(CIMSThread server)
    {
        currentThreads.remove(server);
        System.out.println("-------------------------------------------------------");
        System.out.println("CIMSServer: CIMSThread removed from threadpool, current active Threads: " + currentThreads.size());
    }
    
    /**
     * THIS METHOD IS USED TO FIND A LOGGED IN PERSON HIS THREAD IN THE CURRENT THREAD POOL 
     * THIS IS USED FOR CHATTING, MESSAGING AND DELIVERING OTHER INFROMATION TO THIS PERSON
     * @param personid
     * @return 
     */
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

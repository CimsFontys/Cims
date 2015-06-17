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
    private ArrayList<CIMSServer> currentThreads;
    
    private ThreadAdministration() 
    {
        currentThreads = new ArrayList<CIMSServer>();
    }
    
    public static ThreadAdministration getInstance() 
    {
        return ThreadAdministrationHolder.INSTANCE;
    }
    
    private static class ThreadAdministrationHolder {

        private static final ThreadAdministration INSTANCE = new ThreadAdministration();
    }
    
    public void addClient(CIMSServer server)
    {
        currentThreads.add(server);
    }
    
    public void removeClient(CIMSServer server)
    {
        currentThreads.remove(server);
    }
    
    public CIMSServer findClient(int personid)
    {
        for(CIMSServer c : currentThreads)
        {
            if(c.receiverID == personid)
            {
                return c;
            }
        }
        
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socketserver;

/**
 *
 * @author Michael
 */
public class ThreadAdministration {
   
    private ThreadAdministration() 
    {
        
    }
    
    public static ThreadAdministration getInstance() {
        return ThreadAdministrationHolder.INSTANCE;
    }
    
    private static class ThreadAdministrationHolder {

        private static final ThreadAdministration INSTANCE = new ThreadAdministration();
    }
    
    public void addClient(CIMSServer server)
    {
        
    }
}

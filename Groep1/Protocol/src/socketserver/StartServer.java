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
public class StartServer 
{
    public static void main(String args[])
            throws Exception 
    {
        CIMSServer server = new CIMSServer(9000);
        new Thread(server).start();
        System.out.println("Booting up CIMS Server, Creating CIMS Platform");
    }
}

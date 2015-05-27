/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pts4.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leo
 */
public class ServerThread implements Runnable
{
    
    private Server server;
    private ArrayList<String> clients;
    
    public ServerThread(Server server)
    {
        this.server = server;
    }
    
    @Override
    public void run() 
    {
        try
        {
            ServerSocket s = new ServerSocket(8189);
            while(true)
            {
                // establish server socket
                // wait for client connection         
                Socket incoming = s.accept();
                System.out.println("connected");
                Client client = new Client(incoming, server);
                Thread t = new Thread(client);
                t.start();
                server.putClient(client);
            }
        }
        catch (IOException e)
        {  
           e.printStackTrace();
        } 
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 *
 * @author Michael
 */

public class CIMSServer implements Runnable {

    protected int serverPort = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    /**
     * CREATES NEW CIMS SERVER WITH PORT
     * @param port 
     */
    public CIMSServer(int port) {
        this.serverPort = port;
    }

    /**
     * RUN METHOD OF THE CIMS SERVER, CHECKING AND ACCEPTING INCOMING SOCKETS
     */
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(
                    new CIMSThread(
                            clientSocket, "Multithreaded Server")
            ).start();
        }
        System.out.println("Server Stopped.");
    }

    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * STOPS THE CIMS SERVER AND ALL IT'S CONNECTIONS
     */
    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
    
    /**
     * OPENS A SERVER SOCKET
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

}

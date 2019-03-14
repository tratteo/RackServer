/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.io.PrintWriter;
import rackserver.Runnables.*;

/**
 *
 * @author Matteo
 */
public class DevicesManager
{
    private final Server server;
    private boolean connectedToP1 = false, connectedToP2 = false, connectedToArduino = false;
    
    public synchronized boolean isConnectedToP1() {return connectedToP1;}
    public synchronized boolean isConnectedToP2() {return connectedToP2;}
    public synchronized boolean isConnectedToArduino() {return connectedToArduino;}
    
    public synchronized void setConnectedToP1(boolean state) {connectedToP1 = state;}
    public synchronized void setConnectedToP2(boolean state) {connectedToP2 = state;}
    public synchronized void setConnectedToArduino(boolean state) {connectedToArduino = state;}
    
    public PrintWriter outToP1, outToP2, outToArduino;
    
    public DevicesManager(Server server)
    {
        this.server = server;
    }
    
    
    public void WriteToArduino(String message)
    {
        if(connectedToArduino)
        {
            outToArduino.print(message);
            outToArduino.flush();
        }
    }
   
    public void WriteToP1(String message)
    {
        if(connectedToP1)
        {
            outToP1.print(message);
            outToP1.flush();
        }
    }
    
    public void WriteToP2(String message)
    {
        if(connectedToP2)
        {
            outToP2.print(message);
            outToP2.flush();
        }
    }
    
    public void StartPiConnectionThread()
    {
        if(!connectedToP1 || !connectedToP2)
            new Thread(new ConnectPisRunnable(server)).start();
    }
    
    public boolean ConnectArduino()
    {
        if(!connectedToArduino)
            return UtilitiesClass.getInstance().ConnectArduino(server);
        else
            return true;
    }
}

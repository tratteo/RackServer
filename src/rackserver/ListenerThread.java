/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.io.*;
import java.net.*;

public class ListenerThread implements Runnable
{
    String pi = null;
    String piResponse;
    static boolean connectedToPi = false;
    static BufferedReader inFromPi = null;
	
    ListenerThread(Socket _socket, String _pi)
    {
        pi = _pi;
        try{ inFromPi = new BufferedReader(new InputStreamReader(_socket.getInputStream())); } catch (Exception e) {}
    }
	
    @Override
    public void run()
    {
        if(pi.equals("p1"))
        {
            while(RackServer.connectedToP1)
            {
                try
                {
                    if(inFromPi != null)
                        piResponse = inFromPi.readLine();						           
                    if(piResponse.equals("p1-disconnecting"))
                    {
                        RackServer.DisconnectPi("p1");
                        RackServer.commandLineText.append("P1 has interrupted connection\n");
                    }
                    
                    
                } catch (Exception e) {}
            }
        }
        else if(pi.equals("p2"))
        {
            while(RackServer.connectedToP2)
            {
                try
                {
                    if(inFromPi != null)
                        piResponse = inFromPi.readLine();				
                    if(piResponse.equals("p2-disconnecting"))
                    {
                        RackServer.DisconnectPi("p2");
                        RackServer.commandLineText.append("P2 has interrupted connection\n");
                    }
                   
                    
                } catch (Exception e) {}
            }
        }
    }
}
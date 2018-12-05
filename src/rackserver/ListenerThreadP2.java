/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.io.*;
import java.net.*;

public class ListenerThreadP2 implements Runnable
{
    String piResponse;
    static BufferedReader inFromPi = null;
	
    ListenerThreadP2(Socket _socket)
    {
        try{ inFromPi = new BufferedReader(new InputStreamReader(_socket.getInputStream())); } catch (Exception e) {}
    }
	
    @Override
    public void run()
    {
        
         while(RackServer.connectedToP2)
        {
            try
            {
                piResponse = inFromPi.readLine();
                if(piResponse != null)
                {
                    if(piResponse.equals("disconnecting"))
                    {
                        UtilitiesClass.DisconnectPi("p2");
                        RackServer.commandLineText.append("P2 has interrupted connection\n");
                        UtilitiesClass.WriteToAndroidClient("p2-interrupt");

                    }
                    else if(piResponse.equals("rainbowrunning"))
                    {
                        UtilitiesClass.WriteToAndroidClient("rainbowrunning");
                    }                          
                }                    
                else
                    RackServer.commandLineText.append("Null response\n");
            } 
            catch (Exception e) 
            {
                RackServer.commandLineText.append(e.toString() + "\n");
            }
        }
    }
}

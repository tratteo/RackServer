/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.Runnables;

import java.io.*;
import java.net.*;
import rackserver.Server;
import rackserver.UtilitiesClass;

public class ListenerP2Runnable implements Runnable
{
    Server context;
    BufferedReader inFromPi = null;
    String piResponse;
	
    public ListenerP2Runnable(Socket _socket, Server context)
    {
        this.context = context;
        try{ inFromPi = new BufferedReader(new InputStreamReader(_socket.getInputStream())); } catch (Exception e) {System.out.println(e);}
    }
	
    @Override
    public void run()
    { 
         while(context.getDevicesManager().isConnectedToP2())
        {
            try {piResponse = inFromPi.readLine();} catch(Exception e) {System.out.println(e);}
            if(piResponse != null)
            {
                if(piResponse.equals("disconnecting"))
                {
                    UtilitiesClass.getInstance().DisconnectPi("p2", context);
                    context.frame.commandLineText.append("P2 has interrupted connection\n");
                    context.WriteToAllClients("p2-interrupt");
                }                
            }                    
            else
                context.frame.commandLineText.append("Null response\n");
        }
    }
}

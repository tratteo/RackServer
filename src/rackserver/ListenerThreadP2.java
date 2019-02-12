/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ListenerThreadP2 implements Runnable
{
    Application context;
    //BufferedReader inFromPi = null;
    Scanner inFromPi = null;
    String piResponse;
	
    ListenerThreadP2(Socket _socket, Application context)
    {
        this.context = context;
        try{ inFromPi = new Scanner(new InputStreamReader(_socket.getInputStream())); } catch (Exception e) {System.out.println(e);}
    }
	
    @Override
    public void run()
    { 
         while(context.connectedToP2)
        {
            try {piResponse = inFromPi.nextLine();} catch(Exception e) {System.out.println(e);}
            if(piResponse != null)
            {
                if(piResponse.equals("disconnecting"))
                {
                    UtilitiesClass.getInstance().DisconnectPi("p2", context);
                    context.frame.commandLineText.append("P2 has interrupted connection\n");
                    UtilitiesClass.getInstance().WriteToAndroidClient("p2-interrupt", context);

                }
                else if(piResponse.equals("rainbowrunning"))
                {
                    UtilitiesClass.getInstance().WriteToAndroidClient("rainbowrunning", context);
                }                     
            }                    
            else
                context.frame.commandLineText.append("Null response\n");
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.Runnables;

import java.awt.Color;
import rackserver.UI.Overlay;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import rackserver.Server;
import rackserver.UtilitiesClass;

/**
 *
 * @author Matteo
 */
public class ListenerP1Runnable implements Runnable
{

    Server context;
    BufferedReader inFromPi = null;
    String piResponse;
	
    public ListenerP1Runnable(Socket _socket, Server context)
    {
        this.context = context;
        try{ inFromPi = new BufferedReader(new InputStreamReader(_socket.getInputStream())); } catch (Exception e) {}
    }
	
    @Override
    public void run()
    { 
        while(context.getDevicesManager().isConnectedToP1())
        {
            try {piResponse = inFromPi.readLine();} catch(Exception e) {System.out.println(e);}
            if(piResponse != null)
            {
                if(piResponse.equals("disconnecting"))
                {
                    UtilitiesClass.getInstance().DisconnectPi("p1", context);
                    context.frame.commandLineText.append("P1 has interrupted connection\n");
                    context.WriteToAllClients("p1-interrupt");
                }
                else if(UtilitiesClass.getInstance().isStringFloat(piResponse))
                {
                    context.currentTemperature = piResponse;
                    context.frame.temperatureLabel.setText(piResponse + "°");
                    int[] rgbValues = UtilitiesClass.getInstance().getRGBValuesFromTemperature(Float.parseFloat(piResponse));
                    context.frame.temperatureLabel.setForeground(new Color(rgbValues[0], rgbValues[1], rgbValues[2]));
                    
                    context.WriteToAllClients(piResponse);
                    
                }
            }
            else
                context.frame.commandLineText.append("Null response\n");
        }
    }
}

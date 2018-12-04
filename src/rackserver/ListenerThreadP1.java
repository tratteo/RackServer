/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Matteo
 */
public class ListenerThreadP1 implements Runnable
{
    String piResponse;
    static BufferedReader inFromPi = null;
	
    ListenerThreadP1(Socket _socket)
    {
        try{ inFromPi = new BufferedReader(new InputStreamReader(_socket.getInputStream())); } catch (Exception e) {}
    }
	
    @Override
    public void run()
    {
        
        while(RackServer.connectedToP1)
        {
            try
            {
                piResponse = inFromPi.readLine();

                if(piResponse != null)
                {
                    if(piResponse.equals("disconnecting"))
                    {
                        UtilitiesClass.DisconnectPi("p1");
                        RackServer.commandLineText.append("P1 has interrupted connection\n");
                        UtilitiesClass.WriteToAndroidClient("p1-interrupt");
                    }
                    else
                        RackServer.temperatureLabel.setText(piResponse + "°");
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

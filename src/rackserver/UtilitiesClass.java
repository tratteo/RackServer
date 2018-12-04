/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.awt.Color;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Matteo
 */
public class UtilitiesClass 
{
    public static void WriteToAndroidClient(String message)
    {
        if(RackServer.outToAndroidClient != null)
        {
            RackServer.outToAndroidClient.println(message);
            RackServer.outToAndroidClient.flush();
        }
    }
    
    public static boolean ConnectPi(String pi)
    {
        if(pi.equals("p1"))
        {
            try 
            {
                RackServer.p1Socket = new Socket("192.168.1.100", 5555);
                RackServer.outToP1 = new PrintWriter(RackServer.p1Socket.getOutputStream());
                Thread p1Listener = new Thread(new ListenerThreadP1(RackServer.p1Socket));
                RackServer.tratPiLabel.setForeground(new Color(80, 255, 70));
                RackServer.commandLineText.append("Pi 1 connected\n");
                RackServer.connectedToP1 = true;
                UtilitiesClass.WriteToAndroidClient("p1-connected");
                p1Listener.start();
                return true;

            } 
            catch (Exception e) 
            {
                RackServer.commandLineText.append("Unable to connect to Pi 1\n");
                UtilitiesClass.WriteToAndroidClient("p1-unable");
                RackServer.tratPiLabel.setForeground(Color.RED);
                RackServer.connectedToP1 = false;
                return false;
            }
        }

        else if(pi.equals("p2"))
        {
            try 
            {
                RackServer.p2Socket = new Socket("192.168.1.187", 6666);
                RackServer.outToP2 = new PrintWriter(RackServer.p2Socket.getOutputStream());
                Thread p2Listener = new Thread(new ListenerThreadP2(RackServer.p2Socket));
                p2Listener.start();
                RackServer.guizPiLabel.setForeground(new Color(80, 255, 70));
                RackServer.commandLineText.append("Pi 2 connected\n");
                RackServer.connectedToP2 = true;
                UtilitiesClass.WriteToAndroidClient("p2-connected");
                return true;

            } catch (Exception e)
            {
                RackServer.connectedToP2 = false;
                RackServer.commandLineText.append("Unable to connect to Pi 2\n");
                UtilitiesClass.WriteToAndroidClient("p2-unable");
                RackServer.guizPiLabel.setForeground(Color.RED);
                return false;
            }
        }
        return false;
    }
    
    public static void DisconnectPi(String pi)
    {
        if(pi.equals("p1"))
        {
            try 
            {
                RackServer.p1Socket.close();
                RackServer.tratPiLabel.setForeground(Color.RED);
                RackServer.connectedToP1 = false;
                RackServer.p1Socket = null;
            } catch (Exception e) {}
        }

        else if(pi.equals("p2"))
        {
            try 
            {
                RackServer.p2Socket.close();
                RackServer.guizPiLabel.setForeground(Color.RED);
                RackServer.connectedToP2 = false;
                RackServer.p2Socket = null;
            } catch (Exception e) {}
        }
    }
    
    public static void ClosingService()
    {   
        try
        {
            RackServer.commandLineText.append("Sending close message to client...\n");
            RackServer.outToAndroidClient.println("serverdown");
            RackServer.outToAndroidClient.flush();
        }
        catch (Exception e) 
        {
            RackServer.commandLineText.append("No clients connected\n");
        }

        try
        {
            RackServer.commandLineText.append("Sending close message to Pi1...\n");
            RackServer.outToP1.print("disconnecting");
            RackServer.outToP1.flush();
            RackServer.p1Socket.close();

        } catch( Exception e) 
        {
            RackServer.commandLineText.append("Pi1 not connected\n");
        }
        try
        {
            RackServer.commandLineText.append("Sending close message to Pi2...\n");
            RackServer.outToP2.print("disconnecting");
            RackServer.outToP2.flush();
            RackServer.p2Socket.close();
        } 
        catch( Exception e) 
        {
            RackServer.commandLineText.append("Pi2 not connected\n");
        }
        RackServer.commandLineText.append("Closing App\n");}
}

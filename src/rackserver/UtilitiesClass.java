/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import rackserver.RunnableUtils.ListenerThreadP2;
import rackserver.RunnableUtils.ListenerThreadP1;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.net.*;

/**
 *
 * @author Matteo
 */
public final class UtilitiesClass 
{
    private static UtilitiesClass instance = null;
    private UtilitiesClass() {}
    public static UtilitiesClass getInstance() 
    {
        if(instance == null)
            instance = new UtilitiesClass();
        
        return instance;
    }
    
    
    public void WriteToAndroidClient(String message, Application context)
    {
        if(context.outToAndroidClient != null)
        {
            context.outToAndroidClient.println(message);
            context.outToAndroidClient.flush();
        }
    }
    
    public boolean ConnectPi(String pi, Application context)
    {
        if(pi.equals("p1") && !context.connectedToP1)
        {
            try 
            { 
                context.p1Socket = new Socket();
                context.p1Socket.connect(new InetSocketAddress("192.168.1.100", 5555), 1000);
                context.outToP1 = new PrintWriter(context.p1Socket.getOutputStream());
                Thread p1Listener = new Thread(new ListenerThreadP1(context.p1Socket, context));
                context.frame.tratPiLabel.setForeground(new Color(80, 255, 70));
                context.frame.commandLineText.append("Pi 1 connected\n");
                context.connectedToP1 = true;
                WriteToAndroidClient("p1-connected", context);
                p1Listener.start();
                return true;

            } 
            catch (Exception e) 
            {
                WriteToAndroidClient("p1-unable", context);
                context.frame.tratPiLabel.setForeground(Color.RED);
                context.connectedToP1 = false;
                return false;
            }
        }

        else if(pi.equals("p2") && !context.connectedToP2)
        {
            try 
            {
                context.p2Socket = new Socket();
                context.p2Socket.connect(new InetSocketAddress("192.168.1.187", 6666), 1000);
                context.outToP2 = new PrintWriter(context.p2Socket.getOutputStream());
                Thread p2Listener = new Thread(new ListenerThreadP2(context.p2Socket, context));
                p2Listener.start();
                context.frame.guizPiLabel.setForeground(new Color(80, 255, 70));
                context.frame.commandLineText.append("Pi 2 connected\n");
                context.connectedToP2 = true;
                WriteToAndroidClient("p2-connected", context);
                return true;

            } catch (Exception e)
            {
                context.connectedToP2 = false;
                WriteToAndroidClient("p2-unable", context);
                context.frame.guizPiLabel.setForeground(Color.RED);
                return false;
            }
        }
        return false;
    }
    
    public void DisconnectPi(String pi, Application context)
    {
        if(pi.equals("p1"))
        {
            try 
            {
                context.p1Socket.close();
                context.frame.tratPiLabel.setForeground(Color.RED);
                context.connectedToP1 = false;
                context.p1Socket = null;
            } catch (Exception e) {}
        }

        else if(pi.equals("p2"))
        {
            try 
            {
                context.p2Socket.close();
                context.frame.guizPiLabel.setForeground(Color.RED);
                context.connectedToP2 = false;
                context.p2Socket = null;
            } catch (Exception e) {}
        }
    }
    
    public void CloseService(Application context)
    {   
        try
        {
            context.frame.commandLineText.append("Sending close message to client...\n");
            context.outToAndroidClient.println("serverdown");
            context.outToAndroidClient.flush();
        }
        catch (Exception e) 
        {
            context.frame.commandLineText.append("No clients connected\n");
        }

        try
        {
            context.frame.commandLineText.append("Sending close message to Pi1...");
            context.outToP1.print("disconnecting");
            context.outToP1.flush();
            context.p1Socket.close();
            context.frame.commandLineText.append("\n");

        } catch( Exception e) 
        {
            context.frame.commandLineText.append("Pi1 not connected\n");
        }
        try
        {
            context.frame.commandLineText.append("Sending close message to Pi2...");
            context.outToP2.print("disconnecting");
            context.outToP2.flush();
            context.p2Socket.close();
            context.frame.commandLineText.append("\n");
        } 
        catch( Exception e) 
        {
            context.frame.commandLineText.append("Pi2 not connected\n");
        }
        context.frame.commandLineText.append("Closing App\n");}

    public boolean ToPi1(String command)
    {
    
        return (command.substring(0,3).equals("p1-") && command.length() > 3);
    }

    public boolean ToPi2(String command)
    {
    
        return (command.substring(0,3).equals("p2-") && command.length() > 3);
    }
    
    public void SetFullScreen()
    {
        try 
            {                                                    
                Robot robot = new Robot();
                robot.delay(10000);
                robot.keyPress(KeyEvent.VK_F11);
                robot.keyRelease(KeyEvent.VK_F11);
            } catch (Exception ex) { }
    }
    
    public int[] getRGBValuesFromTemperature(float temperature)
    {            
        int[] rgbValues = new int[3];
        float red = 0, green = 0, blue = 0;

        if(temperature < 10f)
        {
            red = 0;
            green = 0;
            blue = 255f;
        }
        else if( temperature >= 10 && temperature < 16f)
        {
            red = 0;
            green = 42.5f * (temperature - 10f);
            blue = 255f;
        }
        else if(temperature >= 16f && temperature < 20f)
        {
            red = 0;
            green = 255f;
            blue = -63.75f * (temperature - 20f);
        }
        else if(temperature >= 20f && temperature < 25f)
        {
            red = 51f * (temperature - 20f);
            green = 255f;
            blue = 0;
        }
        else if(temperature >= 25f && temperature <= 36f)
        {
            red = 255f;
            green = -23.15f * (temperature - 36f);
            blue = 0;
        }     
        else if(temperature > 36f)
        {
            red = 255f;
            green = 0;
            blue = 0;
        }
        
        rgbValues[0] = (int)red;
        rgbValues[1] = (int)green;
        rgbValues[2] = (int)blue;
        return rgbValues;
    }
    
    public boolean isStringFloat(String numberString)
    {
        try{ Float.parseFloat(numberString); return true;} catch(Exception e) {return false;}
    }
}

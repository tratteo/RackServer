/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import com.fazecast.jSerialComm.SerialPort;
import rackserver.Runnables.ListenerP2Runnable;
import rackserver.Runnables.ListenerP1Runnable;
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
    
    
    public synchronized boolean ConnectArduino(Server context)
    {
        try
        {
            context.serialPort =  SerialPort.getCommPort("/dev/ttyACM0");
            context.serialPort.setComPortParameters(9600, 8, 1, 0);
            context.serialPort.openPort();
            context.getDevicesManager().outToArduino = new PrintWriter(context.serialPort.getOutputStream());
            context.frame.commandLineText.append("Arduino connected\n");
            context.frame.arduinoStatusLabel.setText("Connected");
            context.frame.arduinoStatusLabel.setForeground(Color.GREEN);
            context.getDevicesManager().setConnectedToArduino(true);
            return true;
        }
        catch (Exception ex)
        {
            context.frame.commandLineText.append("Unable to connect to Arduino on port ttyACM0... I'm trying on ttyACM1 \n");
            context.getDevicesManager().setConnectedToArduino(false);
            context.frame.arduinoStatusLabel.setText("Disconnected");
            context.frame.arduinoStatusLabel.setForeground(Color.RED);
            //return false;
        }

        try
        {
            context.serialPort =  SerialPort.getCommPort("/dev/ttyACM1");
            context.serialPort.setComPortParameters(9600, 8, 1, 0);
            context.serialPort.openPort();
            context.getDevicesManager().outToArduino = new PrintWriter(context.serialPort.getOutputStream());
            context.frame.commandLineText.append("Arduino connected\n");
            context.frame.arduinoStatusLabel.setText("Connected");
            context.frame.arduinoStatusLabel.setForeground(Color.GREEN);
            context.getDevicesManager().setConnectedToArduino(true);
        }
        catch (Exception ex)
        {
            context.frame.commandLineText.append("Unable to connect to Arduino\n");
            context.getDevicesManager().setConnectedToArduino(false);
            context.frame.arduinoStatusLabel.setText("Disconnected");
            context.frame.arduinoStatusLabel.setForeground(Color.RED);
            return false;
        }
        return true;
    }
    
    public synchronized boolean ConnectPi(String pi, Server context)
    {
        if(pi.equals("p1") && !context.getDevicesManager().isConnectedToP1())
        {
            try 
            { 
                context.p1Socket = new Socket();
                context.p1Socket.connect(new InetSocketAddress("192.168.1.100", 5555), 1500);
                context.getDevicesManager().outToP1 = new PrintWriter(context.p1Socket.getOutputStream());
                Thread p1Listener = new Thread(new ListenerP1Runnable(context.p1Socket, context));
                context.frame.tratPiLabel.setForeground(new Color(80, 255, 70));
                context.frame.commandLineText.append("Pi 1 connected\n");
                context.getDevicesManager().setConnectedToP1(true);
                context.WriteToAllClients("p1-connected");
                p1Listener.start();
                return true;

            } 
            catch (Exception e) 
            {
                context.frame.tratPiLabel.setForeground(Color.RED);
                context.getDevicesManager().setConnectedToP1(false);
                return false;
            }
        }

        else if(pi.equals("p2") && !context.getDevicesManager().isConnectedToP2())
        {
            try 
            {
                context.p2Socket = new Socket();
                context.p2Socket.connect(new InetSocketAddress("192.168.1.187", 6666), 1500);
                context.getDevicesManager().outToP2 = new PrintWriter(context.p2Socket.getOutputStream());
                Thread p2Listener = new Thread(new ListenerP2Runnable(context.p2Socket, context));
                p2Listener.start();
                context.frame.guizPiLabel.setForeground(new Color(80, 255, 70));
                context.frame.commandLineText.append("Pi 2 connected\n");
                context.getDevicesManager().setConnectedToP2(true);
                context.WriteToAllClients("p2-connected");
                return true;

            } catch (Exception e)
            {
                context.getDevicesManager().setConnectedToP2(false);
                context.frame.guizPiLabel.setForeground(Color.RED);
                return false;
            }
        }
        return false;
    }
    
    public void DisconnectPi(String pi, Server context)
    {
        if(pi.equals("p1"))
        {
            try 
            {
                context.p1Socket.close();
                context.frame.tratPiLabel.setForeground(Color.RED);
                context.getDevicesManager().setConnectedToP1(false);
                context.p1Socket = null;
            } catch (Exception e) {}
        }

        else if(pi.equals("p2"))
        {
            try 
            {
                context.p2Socket.close();
                context.frame.guizPiLabel.setForeground(Color.RED);
                context.getDevicesManager().setConnectedToP2(false);
                context.p2Socket = null;
            } catch (Exception e) {}
        }
    }
    
    public void CloseService(Server context)
    {
        try
        {
            context.frame.commandLineText.append("Sending close message to client...\n");
            context.WriteToAllClients("serverdown");
        }
        catch (Exception e) 
        {
            context.frame.commandLineText.append("No clients connected\n");
        }

        try
        {
            context.frame.commandLineText.append("Sending close message to Pi1...");
            context.getDevicesManager().WriteToP1("disconnecting");
            context.p1Socket.close();
            context.frame.commandLineText.append("\n");

        } catch( Exception e) 
        {
            context.frame.commandLineText.append("Pi1 not connected\n");
        }
        try
        {
            context.frame.commandLineText.append("Sending close message to Pi2...");
            context.getDevicesManager().WriteToP2("disconnecting");
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
    
    public void SetFullScreen(int delay)
    {
        try 
            {                                                    
                Robot robot = new Robot();
                robot.delay(delay);
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

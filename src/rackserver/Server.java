/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import rackserver.Runnables.*;
import com.fazecast.jSerialComm.SerialPort;
import rackserver.UI.RackServerFrame;
import rackserver.UI.Overlay;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rackserver.Runnables.ClientRunnable;
/**
 *
 * @author Matteo
 */
public class Server implements Runnable
{   
    private DevicesManager devicesManager = null;
    public DevicesManager getDevicesManager() {return devicesManager;}
    
    private Map<String,ClientRunnable> clientsList;
    public int getNumberOfConnectedClients() {return clientsList.size();}

    public RackServerFrame frame;
    private boolean firefoxRunning = false;
    public synchronized boolean isFirefoxRunning() {return firefoxRunning;}
    public synchronized void setFirefoxRunning(boolean state) {firefoxRunning = state;}
    
    //Connection variables
    private ServerSocket serverSocket = null;
    public Socket p1Socket = null, p2Socket = null;
    private Socket androidSocket;
    private final int portNumber=7777;

    
    private Overlay overlay;
    public String currentTemperature = null;
    private String sentence; 
    
    private final Server instance;
    
    //Arduino
    public SerialPort serialPort;
    
    public Server(RackServerFrame frame) 
    {
        this.frame = frame; instance = this;
        clientsList = new HashMap<>();
    }
    
    
    @Override
    public void run()
    {   
                
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent event)
            {
                UtilitiesClass.getInstance().CloseService(instance);
                System.exit(0);
            }
        });
        
        //new Thread(new ScreenSaverRunnable(this)).start();
        new Thread(new DigitalClockRunnable(frame.clockLabel)).start();
        
        do
        {
            frame.commandLineText.append("Starting Rack-Server v3.0.0\n");            
            try
            {
                serverSocket = new ServerSocket(portNumber);
                frame.commandLineText.append("Server socket created on port: "+ Integer.toString(portNumber) + "\n");
            }
            catch(Exception e) 
            {
                frame.commandLineText.append("Unable to create sockets.\nException: " + e.toString() + "\n");
            }
            
            devicesManager = new DevicesManager(this);
           
            devicesManager.ConnectArduino();
            devicesManager.StartPiConnectionThread();
            
            try
            {
                do
                {
                    //commandLineText.append("Waiting for clients connection...\n");
                    androidSocket = serverSocket.accept();
                    String ipString = androidSocket.getInetAddress().toString().substring(1, androidSocket.getInetAddress().toString().length());
                    if(clientsList.containsKey(ipString))
                    {
                        ClientRunnable clientRunnable = new ClientRunnable(androidSocket, this);
                        clientRunnable.WriteToClient("serverdown");
                        androidSocket.close();
                    }
                    else
                    {
                        frame.connectedClientText.append(ipString + "\n");                    
                        ClientRunnable clientRunnable = new ClientRunnable(androidSocket, this);
                        new Thread(clientRunnable).start();                    
                        clientsList.put(ipString, clientRunnable);
                    }
                   
                }while(true);
            }
            catch (Exception e) 
            {
                UtilitiesClass.getInstance().CloseService(this);
                try 
                {
                    serverSocket.close();
                } catch (Exception ex) 
                {
                    Logger.getLogger(RackServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.commandLineText.append("Bug:"+ e.toString() + "\n");
                frame.commandLineText.append("SERVER REBOOT\n");
                frame.connectedClientText.setText("");
            }   
        }while(true);
    }
    
    public void RemoveClientFromList(String ip)
    {
        clientsList.remove(ip);
    }
    
    public void WriteToAllClients(String message)
    {
        Iterator iterator = clientsList.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)iterator.next(); 
            ClientRunnable clientRunnable = (ClientRunnable)entry.getValue();
            if(clientRunnable.isClientConnected())
                clientRunnable.WriteToClient(message);
        }
    }
}


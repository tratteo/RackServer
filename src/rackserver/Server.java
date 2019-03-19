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
                    //TODO remove
                    //connectedToAndroid = true;
                    
                    String ipString = androidSocket.getInetAddress().toString().substring(1, androidSocket.getInetAddress().toString().length());
                    frame.connectedClientText.append(ipString + "\n");
                    
                    ClientRunnable clientRunnable = new ClientRunnable(androidSocket, this);
                    new Thread(clientRunnable).start();
                    
                    clientsList.put(ipString, clientRunnable);
                    
                    //outToAndroidClient = new PrintWriter(androidSocket.getOutputStream());
                    //inFromAndroidClient =  new BufferedReader(new InputStreamReader(androidSocket.getInputStream()));	

                    
                    //TODO implement in client runnable
                    

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
    
    public void CheckCommandToExecuteOnRack(String command)
    {
        String spotifyPrefix = "dbus-send --print-reply --dest=org.mpris.MediaPlayer2.spotify /org/mpris/MediaPlayer2 org.mpris.MediaPlayer2.Player.";
        //run one
        switch (command)
        {
            case "spotify":
               new Thread(new ExecuteRackCommandRunnable("/runBatches/run_spotify.sh", this)).start(); 
               break;
            case "spotifytoggle":
                new Thread(new ExecuteRackCommandRunnable(spotifyPrefix + "PlayPause", this)).start();
                break;
            case "spotifynext":
                new Thread(new ExecuteRackCommandRunnable(spotifyPrefix + "Next", this)).start();
                break;
            case "spotifyprevious":
                new Thread(new ExecuteRackCommandRunnable(spotifyPrefix + "Previous", this)).start();
                break;
            case "close firefox":
               new Thread(new ExecuteRackCommandRunnable("wmctrl -c firefox", this)).start(); 
               firefoxRunning = false;
               break;
            case "close spotify":
               new Thread(new ExecuteRackCommandRunnable("pkill spotify", this)).start(); 
               break;
            case "close server":
                UtilitiesClass.getInstance().CloseService(this);
                System.exit(0);
                break;
                    
            default:
                if(command.length()>=7 && command.substring(0,7).equals("firefox"))
                {
                    firefoxRunning = true;
                    new Thread(new ExecuteRackCommandRunnable(command, this)).start();
                    //UtilitiesClass.getInstance().SetFullScreen();
                    //overlay= new Overlay(this);       
                }
                else
                {
                    new Thread(new ExecuteRackCommandRunnable(command, this)).start();
                }
                break;
        }
        //frame.commandLineText.append(command + "\n");
        //frame.commandLineText.append(command.substring(0,12) + "\n");
        
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


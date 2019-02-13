/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import rackserver.RunnableUtils.ExecuteRackCommand;
import rackserver.RunnableUtils.DigitalClock;
import rackserver.UI.RackServerFrame;
import rackserver.UI.Overlay;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import rackserver.RunnableUtils.ScreenSaver;
/**
 *
 * @author Matteo
 */
public class Application implements Runnable
{   
    public RackServerFrame frame;
    public Application(RackServerFrame frame) {this.frame = frame;}
    
    public boolean connectedToP1 = false, connectedToP2 = false, connectedToAndroid = false, firefoxRunning=false;
    
    ServerSocket serverSocket = null;
    Socket p1Socket = null, p2Socket = null;
    Socket androidSocket;
    
    PrintWriter outToAndroidClient = null, outToP1 = null, outToP2 = null;
    BufferedReader inFromAndroidClient=null;
    final int portNumber=7777;

    Overlay overlay;
    public String currentTemperature = null;
    String sentence; 
            
    @Override
    public void run()
    {   
        new Thread(new ScreenSaver(this)).start();
        new Thread(new DigitalClock(frame.clockLabel)).start();
        
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent event)
            {
                try
                {
                    frame.commandLineText.append("Sending close message to client...\n");
                    outToAndroidClient.println("serverdown");
                    outToAndroidClient.flush();
                }
                catch (Exception e) 
                {
                    frame.commandLineText.append("No clients connected\n");
                }

                try
                {
                    frame.commandLineText.append("Sending close message to Pi1...\n");
                    outToP1.print("disconnecting");
                    outToP1.flush();
                    p1Socket.close();
                    connectedToP1 = false;

                } catch( Exception e) 
                {
                    frame.commandLineText.append("Pi1 not connected\n");
                }
                try
                {
                    frame.commandLineText.append("Sending close message to Pi2...\n");
                    outToP2.print("disconnecting");
                    outToP2.flush();
                    p2Socket.close();
                    connectedToP2 = false;
                } 
                catch( Exception e) 
                {
                    frame.commandLineText.append("Pi2 not connected\n");
                }
                frame.commandLineText.append("Closing App\n");
                System.exit(0);
            }
        });
        
        do
        {
            frame.commandLineText.append("Starting Rack server v2.0.0\n");            
            try
            {
                serverSocket = new ServerSocket(portNumber);
                frame.commandLineText.append("Server socket created on port: "+ Integer.toString(portNumber) + "\n");
            }
            catch(Exception e) 
            {
                frame.commandLineText.append("Unable to create sockets.\nException: " + e.toString() + "\n");
            }
            
            frame.commandLineText.append("Connecting available Raspberrys...\n");
            UtilitiesClass.getInstance().ConnectPi("p1", this);
            UtilitiesClass.getInstance().ConnectPi("p2", this);
            try
            {
                do
                {
                    //commandLineText.append("Waiting for clients connection...\n");
                    androidSocket = serverSocket.accept();
                    connectedToAndroid = true;
                    String ipString = androidSocket.getInetAddress().toString().substring(1, androidSocket.getInetAddress().toString().length());
                    frame.ClientConnectedText.append(ipString + "\n");
                    outToAndroidClient = new PrintWriter(androidSocket.getOutputStream());
                    inFromAndroidClient =  new BufferedReader(new InputStreamReader(androidSocket.getInputStream()));	

                    do
                    {
                        sentence = inFromAndroidClient.readLine();   

                        if(!sentence.equals("disconnecting"))
                            frame.commandLineText.append("Command received: " + sentence + "\n");
                        if(sentence != null)
                        {
                            //comandi da eseguire sul rack
                            if(sentence.length()>=5  && sentence.substring(0,5).equals("rack-"))
                            {
                                CheckCommandToExecuteOnRack(sentence.substring(5));
                            }
                            else if(UtilitiesClass.getInstance().ToPi1(sentence))
                            {
                                String command = sentence.substring(3, sentence.length());
                                
                                if(command.equals("connect") && !connectedToP1)
                                    UtilitiesClass.getInstance().ConnectPi("p1", this);
                                
                                else if(!command.equals("connect") && connectedToP1)
                                {
                                    frame.commandLineText.append("Sending " + command +" to Pi1\n");
                                    outToP1.print(command);
                                    outToP1.flush();
                                }      
                            }
                            else if(UtilitiesClass.getInstance().ToPi2(sentence))
                            {
                                String command = sentence.substring(3, sentence.length());
                                
                                if(command.equals("connect") && !connectedToP2)
                                    UtilitiesClass.getInstance().ConnectPi("p2", this);
                                else if(!command.equals("connect") && connectedToP2)
                                {
                                    frame.commandLineText.append("Sending " + command +" to Pi2\n");
                                    outToP2.print(command);
                                    outToP2.flush();
                                }
                            }
                        }
                    } while(!sentence.equals("disconnecting"));

                    connectedToAndroid = false;
                    inFromAndroidClient.close();
                    outToAndroidClient.close();

                    int endPoint = frame.ClientConnectedText.getLineEndOffset(0);
                    frame.ClientConnectedText.replaceRange("", 0, endPoint);

                }while(true);
            }
            catch (Exception e) 
            {
                UtilitiesClass.getInstance().CloseService(this);
                try 
                {
                    serverSocket.close();
                    inFromAndroidClient.close();
                } catch (Exception ex) 
                {
                    Logger.getLogger(RackServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                frame.commandLineText.append("Bug:"+ e.toString() + "\n");
                frame.commandLineText.append("SERVER REBOOT\n");
                frame.ClientConnectedText.setText("");
            }
                
        }while(true);
    }
    
    public void CheckCommandToExecuteOnRack(String command)
    {
        if(command.length()>=7 && command.substring(0,7).equals("firefox"))
        {
            firefoxRunning = true;
            new Thread(new ExecuteRackCommand(command, this)).start();
            UtilitiesClass.getInstance().SetFullScreen();
            overlay= new Overlay(this);       
        }
        else if(command.equals("close firefox"))
        {
            new Thread(new ExecuteRackCommand("pkill firefox", this)).start();
            firefoxRunning = false;
            overlay.Destroy();
        }
        else if(command.equals("close server"))
        {
            UtilitiesClass.getInstance().CloseService(this);
            System.exit(0);
        }
        else
        {
            new Thread(new ExecuteRackCommand(command, this)).start();
        }
    }
}


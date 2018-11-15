/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author Matteo
 */
public class RackServer extends javax.swing.JFrame
{
    static boolean connectedToP1 = false, connectedToP2 = false, connectedToAndroid = false;
    static PrintWriter outToAndroidClient, outToP1, outToP2;
    static Socket p1Socket = null, p2Socket = null;
    static ServerSocket serverSocket;
    static int porta=7777;
    
    static RackCommandThread rackCmdThread=null;
    
    
    public RackServer() 
    {
        initComponents();  
        tratPiLabel.setForeground(Color.RED);
        guizPiLabel.setForeground(Color.RED);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Rectangle maxBoundSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        this.setSize(maxBoundSize.getSize());
        this.setVisible(true);   
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent event)
            {
                try
                {
                    commandLineText.append("Sending close message to client...\n");
                    outToAndroidClient.println("serverdown");
                    outToAndroidClient.flush();
                }
                catch (Exception e) 
                {
                    commandLineText.append("No clients connected\n");
                }

                try
                {
                    commandLineText.append("Sending close message to Pi1...\n");
                    outToP1.print("disconnecting");
                    outToP1.flush();
                    p1Socket.close();

                } catch( Exception e) 
                {
                    commandLineText.append("Pi1 not connected\n");
                }
                try
                {
                    commandLineText.append("Sending close message to Pi2...\n");
                    outToP2.print("disconnecting");
                    outToP2.flush();
                    p2Socket.close();
                } 
                catch( Exception e) 
                {
                    commandLineText.append("Pi2 not connected\n");
                }
                commandLineText.append("Closing App\n");
                System.exit(0);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        commandLineScroll = new javax.swing.JScrollPane();
        commandLineText = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        guizPiLabel = new javax.swing.JLabel();
        tratPiLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rack Server");
        setIconImages(null);
        setResizable(false);
        setSize(new java.awt.Dimension(1360, 500));

        commandLineScroll.setToolTipText("");

        commandLineText.setEditable(false);
        commandLineText.setBackground(new java.awt.Color(255, 233, 204));
        commandLineText.setColumns(20);
        commandLineText.setFont(new Font("Droid Sans Mono", Font.PLAIN, 20));
        commandLineText.setRows(5);
        commandLineText.setMargin(new java.awt.Insets(10, 10, 10, 10));
        commandLineScroll.setViewportView(commandLineText);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Connnected Pi:");

        guizPiLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        guizPiLabel.setForeground(new java.awt.Color(255, 0, 0));
        guizPiLabel.setText("GuizPi");

        tratPiLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        tratPiLabel.setForeground(new java.awt.Color(255, 0, 0));
        tratPiLabel.setText("TratPi");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(commandLineScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(guizPiLabel)
                        .addGap(18, 18, 18)
                        .addComponent(tratPiLabel)))
                .addContainerGap(566, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guizPiLabel)
                            .addComponent(tratPiLabel)))
                    .addComponent(commandLineScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1382, 824);
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) 
    {                 
        RackServer rackServerFrame = new RackServer();
        Socket androidSocket;
        BufferedReader inFromAndroidClient;
        String sentence;
        DefaultCaret caret = (DefaultCaret)commandLineText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        do
        {
            commandLineText.append("Starting Rack server v2.0.0\n");            

            int numeroPorta;
            try 
            {
                numeroPorta = Integer.parseInt(args[0]);
            } 
            catch (Exception e) 
            {
                numeroPorta=porta;
            }
            try
            {
                serverSocket = new ServerSocket(numeroPorta);
                commandLineText.append("Server socket created on port: "+ Integer.toString(numeroPorta) + "\n");
            }
            catch(IOException e) 
            {
                commandLineText.append("Unable to create sockets. Exception: " + e.toString() + "\n");
            }
            
            commandLineText.append("Connecting available Raspberrys...\n");
            ConnectPi("p1");
            ConnectPi("p2");
                try
                {
                    do
                    {
                        commandLineText.append("Waiting for clients connection...\n");
                        androidSocket = serverSocket.accept();
                        connectedToAndroid = true;
                        String ipString = androidSocket.getInetAddress().toString().substring(1, androidSocket.getInetAddress().toString().length());
                        commandLineText.append("Connected to: " + ipString + "\n");

                        do
                        {
                            outToAndroidClient = new PrintWriter(androidSocket.getOutputStream());
                            inFromAndroidClient =  new BufferedReader(new InputStreamReader(androidSocket.getInputStream()));	
                            
                            sentence = inFromAndroidClient.readLine();   

                            commandLineText.append("Command received: " + sentence + "\n");

                            //comandi da eseguire sul rack
                            if(sentence.length()>=5  && sentence.substring(0,5).equals("rack-"))
                            {
                                CheckCommandToExecuteOnRack(sentence.substring(5));
                            }

                            else if(ToPi1(sentence))
                            {
                                String command = sentence.substring(3, sentence.length());
                                if(command.equals("connect") && !connectedToP1)
                                    ConnectPi("p1");
                                
                                else if(command.equals("disconnecting"))
                                {
                                    connectedToP1 = false;
                                    p1Socket.close();
                                    p1Socket = null;
                                    tratPiLabel.setForeground(Color.RED);
                                }
                                else if(!command.equals("connect") && connectedToP1)
                                {
                                    commandLineText.append("Sending " + command +" to Pi1\n");
                                    outToP1.print(command);
                                    outToP1.flush();
                                }      
                            }
                            else if(ToPi2(sentence))
                            {
                                String command = sentence.substring(3, sentence.length());
                                if(command.equals("connect") && !connectedToP2)
                                    ConnectPi("p2");
                                
                                else if(command.equals("disconnecting"))
                                {
                                    connectedToP2 = false;
                                    p2Socket.close();
                                    p2Socket = null;
                                    guizPiLabel.setForeground(Color.RED);
                                }
                                else if(!command.equals("connect") && connectedToP2)
                                {
                                    commandLineText.append("Sending " + command +" to Pi2\n");
                                    outToP2.print(command);
                                    outToP2.flush();
                                }
                            }    
                            
                        } while(!sentence.equals("disconnecting"));

                        connectedToAndroid = false;
                        commandLineText.append("Client: " + ipString + " disconnected\n");
                        
                    }while(true);
                }
                catch (IOException e) 
                {
                    try 
                    {
                        serverSocket.close();
                        if(p1Socket != null)
                                p1Socket.close();
                        if(p2Socket != null)
                                p2Socket.close();
                    } catch (IOException e1) {}

                    commandLineText.append("Bug:"+ e.toString() + "\n");
                    commandLineText.append("SERVER REBOOT\n");
                }
                
        }while(true);
    }

    public static  boolean ConnectPi(String pi)
    {
        if(pi.equals("p1"))
        {
            try 
            {
                p1Socket = new Socket("192.168.1.100", 5555);
                outToP1 = new PrintWriter(p1Socket.getOutputStream());
                connectedToP1 = true;
                Thread p1Listener = new Thread(new ListenerThread(p1Socket, "p1"));
                p1Listener.start();
                tratPiLabel.setForeground(new Color(80, 255, 70));
                commandLineText.append("Pi 1 connected\n");
                return true;

            } 
            catch (Exception e) 
            {
                connectedToP1 = false;
                commandLineText.append("Unable to connect to Pi 1\n");
                if(connectedToAndroid)
                {
                    outToAndroidClient.println("p1unable");
                    outToAndroidClient.flush();
                } 
                tratPiLabel.setForeground(Color.RED);
                return false;
            }
        }

        else if(pi.equals("p2"))
        {
            try 
            {
                p2Socket = new Socket("192.168.1.187", 6666);
                outToP2 = new PrintWriter(p2Socket.getOutputStream());
                connectedToP2 = true;
                Thread p2Listener = new Thread(new ListenerThread(p2Socket, "p2"));
                p2Listener.start();
                guizPiLabel.setForeground(new Color(80, 255, 70));
                commandLineText.append("Pi 2 connected\n");
                return true;

            } catch (Exception e)
            {
                connectedToP2 = false;
                commandLineText.append("Unable to connect to Pi 2\n");
                if(connectedToAndroid)
                {
                    outToAndroidClient.println("p2unable");
                    outToAndroidClient.flush();
                }
                guizPiLabel.setForeground(Color.RED);
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
                p1Socket.close();
                tratPiLabel.setForeground(Color.RED);
                connectedToP1 = false;
                p1Socket = null;
            } catch (IOException e) {}
        }

        else if(pi.equals("p2"))
        {
            try 
            {
                p2Socket.close();
                guizPiLabel.setForeground(Color.RED);
                connectedToP2 = false;
                p2Socket = null;
            } catch (IOException e) {}
        }
    }

    public static boolean ToPi1(String command)
    {
    
        return (command.substring(0,3).equals("p1-") && command.length() > 3);
    }

    public static boolean ToPi2(String command)
    {
    
        return (command.substring(0,3).equals("p2-") && command.length() > 3);
    }
    
    public static void CheckCommandToExecuteOnRack(String command)
    {
        if(command.length()>=7 && command.substring(0,7).equals("firefox"))
        {
            rackCmdThread = new RackCommandThread(command);
            rackCmdThread.start(); 
            try 
            {                                                    
                Robot robot = new Robot();
                robot.delay(10000);
                commandLineText.append("Setting fullscreen\n");
                robot.keyPress(KeyEvent.VK_F11);
                robot.keyRelease(KeyEvent.VK_F11);
            } catch (Exception ex) { }
        }
        else if(command.equals("close"))
        {
            rackCmdThread.stopExecute();
        }
    }
    
    public static class RackCommandThread extends Thread
    {
        String command;
        Runtime run;
        Process pr;
        
        RackCommandThread(String cmd)
        {
            command = cmd;
        }
        @Override
        public void run()
        {
            try 
            {
                commandLineText.append("Executing command on rack:" + command + "\n");
                run = Runtime.getRuntime();
                Process pr = run.exec(command);
                pr.waitFor();
                BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = "";
                while ((line=buf.readLine())!=null)
                {
                    commandLineText.append(line + "\n");
                }
            } catch (InterruptedException ex) {} 
            catch (IOException ex) {Logger.getLogger(RackServer.class.getName()).log(Level.SEVERE, null, ex);}
        }
        
        public void stopExecute()
        {
            pr.destroy();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane commandLineScroll;
    public static javax.swing.JTextArea commandLineText;
    private static javax.swing.JLabel guizPiLabel;
    private javax.swing.JLabel jLabel1;
    private static javax.swing.JLabel tratPiLabel;
    // End of variables declaration//GEN-END:variables
}

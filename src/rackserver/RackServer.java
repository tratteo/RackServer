/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;


import java.awt.*;
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

/*
 *
 * @author Matteo
 * 
 */

public class RackServer extends javax.swing.JFrame
{
    static boolean connectedToP1 = false, connectedToP2 = false, connectedToAndroid = false;
    static PrintWriter outToAndroidClient = null, outToP1, outToP2;
    static BufferedReader inFromAndroidClient=null;
    static Socket p1Socket = null, p2Socket = null;
    static ServerSocket serverSocket;
    static int porta=7777;
    
    static RackCommandThread rackCmdThread = null;
    static boolean firefoxRunning=false;
    
    static Background back;
    
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
        clockLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ClientConnectedText = new javax.swing.JTextArea();
        temperatureLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rack Server");
        setIconImages(null);
        setResizable(false);
        setSize(new java.awt.Dimension(1360, 500));

        commandLineScroll.setToolTipText("");

        commandLineText.setEditable(false);
        commandLineText.setBackground(new java.awt.Color(255, 244, 229));
        commandLineText.setColumns(20);
        commandLineText.setFont(new Font("Droid Sans Mono", Font.PLAIN, 20));
        commandLineText.setRows(5);
        commandLineText.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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

        clockLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("Inside Temperature:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("Client Connected:");

        jScrollPane1.setBackground(new java.awt.Color(102, 255, 255));
        jScrollPane1.setAutoscrolls(true);

        ClientConnectedText.setBackground(new java.awt.Color(204, 255, 204));
        ClientConnectedText.setColumns(20);
        ClientConnectedText.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        ClientConnectedText.setRows(5);
        ClientConnectedText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(ClientConnectedText);

        temperatureLabel.setFont(new java.awt.Font("Dialog", 0, 40)); // NOI18N
        temperatureLabel.setText("0°");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(commandLineScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(tratPiLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(guizPiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addComponent(temperatureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(commandLineScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(guizPiLabel)
                        .addGap(18, 18, 18)
                        .addComponent(tratPiLabel)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(temperatureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1458, 824);
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) 
    {                 
        RackServer rackServerFrame = new RackServer();
        Socket androidSocket;
        String sentence;
        DefaultCaret caret = (DefaultCaret)commandLineText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        TimeOutOperation screenSaver= new TimeOutOperation();
        screenSaver.start();
        
        Thread clock = new Thread(new DigitalClock(clockLabel));
        
        clock.start();
        
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
            catch(Exception e) 
            {
                commandLineText.append("Unable to create sockets.\nException: " + e.toString() + "\n");
            }
            
            commandLineText.append("Connecting available Raspberrys...\n");
            UtilitiesClass.ConnectPi("p1");
            UtilitiesClass.ConnectPi("p2");
                try
                {
                    do
                    {
                        commandLineText.append("Waiting for clients connection...\n");
                        androidSocket = serverSocket.accept();
                        connectedToAndroid = true;
                        String ipString = androidSocket.getInetAddress().toString().substring(1, androidSocket.getInetAddress().toString().length());
                        commandLineText.append("Connected to: " + ipString + "\n");
                        ClientConnectedText.append(ipString+"\n");
                        outToAndroidClient = new PrintWriter(androidSocket.getOutputStream());
                        inFromAndroidClient =  new BufferedReader(new InputStreamReader(androidSocket.getInputStream()));	
                            
                        do
                        {
                            
                            sentence = inFromAndroidClient.readLine();   

                            commandLineText.append("Command received: " + sentence + "\n");
                            if(sentence!=null)
                            {
                                //comandi da eseguire sul rack
                                if(sentence.length()>=5  && sentence.substring(0,5).equals("rack-"))
                                {
                                    CheckCommandToExecuteOnRack(sentence.substring(5));
                                }
                                else if(ToPi1(sentence))
                                {
                                    String command = sentence.substring(3, sentence.length());
                                    if(command.equals("connect") && !connectedToP1)
                                        UtilitiesClass.ConnectPi("p1");

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
                                        UtilitiesClass.ConnectPi("p2");

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
                            }
                        } while(!sentence.equals("disconnecting"));// || !sentence.equals("suspend"));

                        connectedToAndroid = false;
                        inFromAndroidClient.close();
                        outToAndroidClient.close();
                        //serverSocket.close();
                        
                        commandLineText.append("Client: " + ipString + " disconnected\n");
                        
                        int end = ClientConnectedText.getLineEndOffset(0);
                        ClientConnectedText.replaceRange("", 0, end);
                        
                    }while(true);
                }
                catch (Exception e) 
                {
                    UtilitiesClass.ClosingService();
                    try 
                    {
                        serverSocket.close();
                        inFromAndroidClient.close();
                    } catch (Exception ex) 
                    {
                        Logger.getLogger(RackServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    commandLineText.append("Bug:"+ e.toString() + "\n");
                    commandLineText.append("SERVER REBOOT\n");
                    ClientConnectedText.setText("");
                }
                
        }while(true);
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
            firefoxRunning=true;
            rackCmdThread = new RackCommandThread(command);
            rackCmdThread.start(); 
            SetFullScreen();
            back= new Background();
            
        }
        else if(command.equals("close firefox"))
        {
            rackCmdThread.stopExecute();
            firefoxRunning=false;
            back.close();
        }
        else
        {
            rackCmdThread = new RackCommandThread(command);
            rackCmdThread.start(); 
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
            rackCmdThread = new RackCommandThread("pkill firefox");
            rackCmdThread.start(); 
        }
    }
    
    public static class TimeOutOperation extends Thread
    {
        boolean running = false;
        int time=600;
        RackCommandThread operationThread;
        
        
        @Override
        public void run()
        {
            while(true)
            {   
                //System.err.println("while true");
                try 
                {
                    Thread.sleep(500);
                } catch (Exception ex) {}
                
                try
                {
                    if(!firefoxRunning)
                    {
                        int t=0;
                        for(t = 0; t<time && !connectedToAndroid ; t++)
                        {
                            try 
                            {
                                Thread.sleep(1000);
                                //System.out.println(t);
                            } catch (Exception ex) 
                            {
                                System.out.println(ex);
                            }
                        }

                        //System.err.println(connectedToAndroid);

                        if (t==time && !running)
                        {
                            System.out.println("avvio firefox");
                            operationThread= new RackCommandThread("firefox https://www.youtube.com/tv#/watch?v=RDfjXj5EGqI");
                            operationThread.start();
                            SetFullScreen();
                            running = true;
                            back= new Background();
                        }
                        else if(t<time && running)
                        {
                            operationThread = new RackCommandThread("pkill firefox");
                            operationThread.start();
                            System.out.println("chiudo firefox");
                            back.close();
                            running = false;
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }
    }
    
    public static void SetFullScreen()
    {
        try 
            {                                                    
                Robot robot = new Robot();
                robot.delay(10000);
                commandLineText.append("Setting fullscreen\n");
                robot.keyPress(KeyEvent.VK_F11);
                robot.keyRelease(KeyEvent.VK_F11);
            } catch (Exception ex) { }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextArea ClientConnectedText;
    public static javax.swing.JLabel clockLabel;
    private javax.swing.JScrollPane commandLineScroll;
    public static javax.swing.JTextArea commandLineText;
    public static javax.swing.JLabel guizPiLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JLabel temperatureLabel;
    public static javax.swing.JLabel tratPiLabel;
    // End of variables declaration//GEN-END:variables
}

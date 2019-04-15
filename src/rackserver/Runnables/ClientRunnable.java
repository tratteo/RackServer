package rackserver.Runnables;

import java.io.*;
import java.net.Socket;
import rackserver.Server;
import rackserver.UtilitiesClass;

/**
 * @author Matteo
 */
public class ClientRunnable implements Runnable
{
    //Context
    private Socket socket;
    private Server server;
    
    //Communication
    private PrintWriter outToClient;
    private BufferedReader inFromClient;
    
    //Local variables
    private boolean connectedToClient;
    public synchronized boolean isClientConnected() {return connectedToClient;}
    
    public ClientRunnable(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
        connectedToClient = true;
        try
        {
            outToClient = new PrintWriter(socket.getOutputStream());
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(Exception e) {}
    }
    
    @Override
    public void run()
    {
        String sentence;
        try
        {
            do
            {
                sentence = inFromClient.readLine();  
                if(!sentence.equals("disconnecting"))
                    server.frame.commandLineText.append("Command received: " + sentence + "\n");
                
                //Rack command
                if(sentence.length()>=5  && sentence.substring(0,5).equals("rack-"))
                {
                    new Thread(new ExecuteRackCommandRunnable(sentence.substring(5), server)).start();
                }
                //Sentence for Pi1
                else if(UtilitiesClass.getInstance().ToPi1(sentence))
                {
                    String command = sentence.substring(3, sentence.length());

                    if(command.equals("connect") && !server.getDevicesManager().isConnectedToP1())
                        UtilitiesClass.getInstance().ConnectPi("p1", server);

                    else if(!command.equals("connect") && server.getDevicesManager().isConnectedToP1())
                    {
                        server.frame.commandLineText.append("Sending " + command +" to Pi1\n");
                        server.getDevicesManager().WriteToP1(command);
                    }      
                }
                //Sentence for Pi2
                else if(UtilitiesClass.getInstance().ToPi2(sentence))
                {
                    String command = sentence.substring(3, sentence.length());

                    if(command.equals("connect") && !server.getDevicesManager().isConnectedToP2())
                        UtilitiesClass.getInstance().ConnectPi("p2", server);
                    
                    else if(!command.equals("connect") && server.getDevicesManager().isConnectedToP2())
                    {
                        server.frame.commandLineText.append("Sending " + command +" to Pi2\n");
                        server.getDevicesManager().WriteToP2(command);
                    }
                }
                
                //Sentence for Arduino
                else if(sentence.substring(0,3).equals("ar-"))
                {
                    if(!server.getDevicesManager().isConnectedToArduino())
                    {
                        UtilitiesClass.getInstance().ConnectArduino(server);
                    }
                    else
                    {
                        String command = sentence.substring(3, sentence.length())+"\n";
                        server.getDevicesManager().WriteToArduino(command);
                    }
                }
                
            } while(!sentence.equals("disconnecting"));
            CloseConnection();
        }
        catch(Exception e)
        {
            server.frame.commandLineText.append("Client thread crashed\n");
            CloseConnection();
        }       
    }
    
    private void CloseConnection()
    {
        String ipString = socket.getInetAddress().toString().substring(1, socket.getInetAddress().toString().length());
        server.RemoveClientFromList(ipString);
        try 
        {
            socket.close(); 
            outToClient.close();
            inFromClient.close();
            int endPoint = server.frame.connectedClientText.getLineEndOffset(0);
            int startPoint = server.frame.connectedClientText.getText().indexOf(ipString);
            server.frame.connectedClientText.replaceRange("", startPoint, endPoint);
        } 
        catch (Exception ignored)
        {}   
    }
    
    public void WriteToClient(String message)
    {
        try
        {
            outToClient.println(message);
            outToClient.flush();
        }catch(Exception e) {}
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.Runnables;

import rackserver.UI.RackServerFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import rackserver.Server;
import rackserver.UtilitiesClass;

/**
 *
 * @author Matteo
 */
public class ExecuteRackCommandRunnable implements Runnable
{
    Server server;
    String message;
    Runtime run;
    Process pr;
    BufferedReader buf;
    String line = "";
                
    public ExecuteRackCommandRunnable(String message, Server server)
    {
        this.server = server;
        this.message = message;
    }
    
    @Override
    public void run()
    {
        try
        {
            run = Runtime.getRuntime();
            String spotifyPrefix = "dbus-send --print-reply --dest=org.mpris.MediaPlayer2.spotify /org/mpris/MediaPlayer2 org.mpris.MediaPlayer2.Player.";
            
            switch (message)
            {
                case "spotify":
                    executeProcess("/run_batches/run_spotify.sh");
                    break;
                    
                case "spotifytoggle":
                    executeProcess(spotifyPrefix + "PlayPause");
                    break;
                    
                case "spotifynext":
                    executeProcess(spotifyPrefix + "Next");
                    break;
                    
                case "spotifyprevious":
                    executeProcess(spotifyPrefix + "Previous");
                    break;
                    
                case "close firefox":
                    executeProcess("wmctrl -c firefox");
                    server.setFirefoxRunning(false);
                    break;
                    
                case "close spotify":
                    executeProcess("pkill spotify");
                    break;
                    
                case "close server":
                    UtilitiesClass.getInstance().CloseService(server);
                    System.exit(0);
                    break;

                default:
                    if(message.length() >= 6 && message.substring(0,6).equals("volume"))
                    {
                        int volume = Integer.parseInt(message.substring(6));
                        executeProcess("amixer -D pulse sset Master " + volume + "%");
                    }
                    else if(message.length()>=7 && message.substring(0,7).equals("firefox"))
                    {
                        server.setFirefoxRunning(true);  
                        executeProcess(message);
                    }
                    else
                    {
                        executeProcess(message);
                        buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                        while((line = buf.readLine()) != null)
                        {
                            server.frame.commandLineText.append(line + "\n");
                        }
                    }       
                    break;
            } 
        }
        catch(Exception e){server.frame.commandLineText.append("Rack runnable catch: " + e.toString());}
    }
    
    public void executeProcess(String command) throws Exception
    {
        pr = run.exec(command);
        pr.waitFor();
    }
}

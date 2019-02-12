/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import rackserver.UI.RackServerFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class ExecuteRackCommand implements Runnable
{
    Application context;
    String command;
    Runtime run;
    Process pr;
        
    public ExecuteRackCommand(String command, Application context)
    {
        this.context = context;
        this.command = command;
    }
    
    @Override
    public void run()
    {
        try 
        {
            context.frame.commandLineText.append("Executing command on rack:" + command + "\n");
            run = Runtime.getRuntime();
            Process pr = run.exec(command);
            pr.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            while ((line=buf.readLine())!=null)
            {
                context.frame.commandLineText.append(line + "\n");
            }
        } catch (InterruptedException ex) {} 
        catch (IOException ex) {Logger.getLogger(RackServerFrame.class.getName()).log(Level.SEVERE, null, ex);}
    }

    public void stopExecute()
    {
        new Thread(new ExecuteRackCommand("pkill firefox", context)).start();
    }
}

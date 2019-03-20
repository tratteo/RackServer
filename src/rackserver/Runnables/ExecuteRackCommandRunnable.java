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

/**
 *
 * @author Matteo
 */
public class ExecuteRackCommandRunnable implements Runnable
{
    Server context;
    String command;
    Runtime run;
    Process pr;
    boolean printResult = false;
        
    public ExecuteRackCommandRunnable(String command, Server context, boolean printResult)
    {
        this.context = context;
        this.command = command;
        this.printResult = printResult;
    }
    
    @Override
    public void run()
    {
        try 
        {
            //context.frame.commandLineText.append("Executing command on rack:" + command + "\n");
            run = Runtime.getRuntime();
            Process pr = run.exec(command);
            pr.waitFor();
            if(printResult)
            {
                BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = "";
                while ((line=buf.readLine())!=null)
                {
                    context.frame.commandLineText.append(line + "\n");
                }
            }
        } catch (InterruptedException ex) {} 
        catch (IOException ex) {Logger.getLogger(RackServerFrame.class.getName()).log(Level.SEVERE, null, ex);}
    }
}

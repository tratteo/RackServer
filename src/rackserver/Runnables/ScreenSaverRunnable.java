/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.Runnables;

import rackserver.Server;
import rackserver.UI.Overlay;
import rackserver.UtilitiesClass;

/**
 *
 * @author Matteo
 */
public class ScreenSaverRunnable implements Runnable
{
    boolean running = false;
    int timeoutTime = 1800;
    
    Overlay overlay;
    Server context;
    
    public ScreenSaverRunnable(Server context) {this.context = context;}
    
    @Override
    public void run()
    {
        while(true)
        {   
            try {Thread.sleep(500);} catch (Exception ex) {}
            try
            {
                if(!context.isFirefoxRunning())
                {
                    int time = 0;
                    for(time = 0; time < timeoutTime && context.getNumberOfConnectedClients() == 0 ; time++)
                        try {Thread.sleep(1000);} catch (Exception ex) {System.out.println(ex);}

                    if (time == timeoutTime && !running)
                    {
                        new Thread(new ExecuteRackCommandRunnable("firefox https://www.youtube.com/tv#/watch/video/control?v=sH05pHZuptA", context, false)).start();
                        UtilitiesClass.getInstance().SetFullScreen(10000);
                        running = true;
                        overlay = new Overlay(context);
                    }
                    else if(time < timeoutTime && running)
                    {
                        new Thread(new ExecuteRackCommandRunnable("wmctrl -c firefox", context, false)).start();
                        overlay.Destroy();
                        running = false;
                    }
                }
            }
            catch(Exception e) {System.out.println(e);}
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.RunnableUtils;

import rackserver.Application;
import rackserver.UI.Overlay;
import rackserver.UtilitiesClass;

/**
 *
 * @author Matteo
 */
public class ScreenSaver implements Runnable
{
    boolean running = false;
    int timeoutTime = 300;
    
    Overlay overlay;
    Application context;
    
    public ScreenSaver(Application context) {this.context = context;}
    
    @Override
    public void run()
    {
        while(true)
        {   
            try {Thread.sleep(500);} catch (Exception ex) {}
            try
            {
                if(!context.firefoxRunning)
                {
                    int time = 0;
                    for(time = 0; time < timeoutTime && !context.connectedToAndroid ; time++)
                        try {Thread.sleep(1000);} catch (Exception ex) {System.out.println(ex);}

                    if (time == timeoutTime && !running)
                    {
                        new Thread(new ExecuteRackCommand("firefox https://www.youtube.com/tv#/watch/video/control?v=sH05pHZuptA", context)).start();
                        UtilitiesClass.getInstance().SetFullScreen();
                        running = true;
                        overlay = new Overlay(context);
                    }
                    else if(time < timeoutTime && running)
                    {
                        new Thread(new ExecuteRackCommand("pkill firefox", context)).start();
                        overlay.Destroy();
                        running = false;
                    }
                }
            }
            catch(Exception e) {System.out.println(e);}
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.RunnableUtils;

import rackserver.Application;
import rackserver.ExecuteRackCommand;
import rackserver.UI.Overlay;
import rackserver.UtilitiesClass;

/**
 *
 * @author Matteo
 */
public class ScreenSaver implements Runnable
{
    boolean running = false;
    int time=600;
    
    Overlay overlay;
    Application context;
    ExecuteRackCommand rackCommand;
    
    public ScreenSaver(Application context) {this.context = context;}
    
    @Override
    public void run()
    {
        while(true)
        {   
            try 
            {
                Thread.sleep(500);
            } catch (Exception ex) {}

            try
            {
                if(!context.firefoxRunning)
                {
                    int t=0;
                    for(t = 0; t<time && !context.connectedToAndroid ; t++)
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
                        rackCommand= new ExecuteRackCommand("firefox https://www.youtube.com/tv#/watch?v=RDfjXj5EGqI", context);
                        UtilitiesClass.getInstance().SetFullScreen();
                        running = true;
                        overlay = new Overlay();
                    }
                    else if(t<time && running)
                    {
                        rackCommand = new ExecuteRackCommand("pkill firefox", context);
                        overlay.close();
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

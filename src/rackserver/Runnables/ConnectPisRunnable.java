/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.Runnables;

import rackserver.*;

/**
 *
 * @author Matteo
 */
public class ConnectPisRunnable implements Runnable
{
    private boolean p1Connected = false, p2Connected = false;
    private Server context;
    
    public ConnectPisRunnable(Server context) {this.context = context;}
    
    @Override 
    public void run()
    {
        while(!p1Connected && !p2Connected)
        {
            //System.err.println("Connecting");
            p1Connected = UtilitiesClass.getInstance().ConnectPi("p1", context);
            p2Connected = UtilitiesClass.getInstance().ConnectPi("p2", context);
            try{Thread.sleep(60000);}catch(InterruptedException ignored){}
        }
    }
    
}

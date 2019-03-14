/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.Runnables;

import java.time.LocalTime;
import javax.swing.JLabel;

/**
 * @author aguiz
 * Create digital clock behaviour and print it on a label
*/
public class DigitalClockRunnable implements Runnable
{
    int hour;
    int minute;
    //static int second;
    String strHour;
    String strMinute;
    //static String strSecond;
    LocalTime time;
    String minPrec="";
    JLabel clock;
    
    
    public DigitalClockRunnable(JLabel clock)
    {
        this.clock = clock;
        time = LocalTime.now();
        hour = time.getHour();
        minute = time.getMinute();
        //second=time.getSecond();
        //strSecond=Integer.toString(second);
        strMinute = Integer.toString(minute);
        strHour = Integer.toString(hour);
        
        this.clock.setText(strHour + ":" + strMinute);
    }
    
    @Override
    public void run() 
    {
        while(true)
        {
            time = LocalTime.now();
            hour = time.getHour();
            minute = time.getMinute();
            
            //strSecond=Integer.toString(second);
            strMinute = Integer.toString(minute);
            strHour = Integer.toString(hour);
            
            if(minute < 10)
            {
                strMinute = "0" + Integer.toString(minute);
            }
            else if(hour < 10)
            {
                strHour = "0" + Integer.toString(hour);
            }
            printClock();
        }
    }

    private void printClock() 
    {
        if(!strMinute.equals(minPrec))
        {
            clock.setText(strHour + ":" + strMinute);//+":"+strSecond);
        }       
        minPrec = strMinute;
    }
}

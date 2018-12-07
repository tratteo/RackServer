/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author aguiz
 */
/* 
   Classe per simulare il comportamento di un orologio.
   La classe contiene un costruttore con tre argomenti (ore, min, sec)
   e un metodo public che incrementa i secondi di una unita' e un 
   metodo che stampa l'ora. 
*/


public class DigitalClock implements Runnable
{
    // testo la classe:
    static int hour;
    static int minute;
    //static int second;
    static String strHour;
    static String strMinute;
    //static String strSecond;
    static LocalTime time;
    static String minPrec="";
    static JLabel clock;
    
    
    public DigitalClock(JLabel _clock)
    {
        clock=_clock;
        time=LocalTime.now();
        hour=time.getHour();
        minute=time.getMinute();
        //second=time.getSecond();
        //strSecond=Integer.toString(second);
        strMinute=Integer.toString(minute);
        strHour=Integer.toString(hour);
        
        clock.setText(strHour+":"+strMinute);
    }
    
    @Override
    public void run() 
    {
        while(true)
        {
            time=LocalTime.now();
            hour=time.getHour();
            minute=time.getMinute();
            
            //strSecond=Integer.toString(second);
            strMinute=Integer.toString(minute);
            strHour=Integer.toString(hour);
            
            if(minute<10)
            {
                strMinute="0"+Integer.toString(minute);
            }
            else if(hour<10)
            {
                strHour="0"+Integer.toString(hour);
            }
            
            //System.err.println(strHour+":"+strMinute);//+":"+strSecond);
            printClock();
        }
    }

    private void printClock() 
    {
        if(!strMinute.equals(minPrec))
        {
            clock.setText(strHour+":"+strMinute);//+":"+strSecond);
        }
        
        minPrec=strMinute;
    }
}

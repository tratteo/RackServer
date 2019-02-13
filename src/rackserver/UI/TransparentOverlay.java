package rackserver.UI;

import com.sun.awt.AWTUtilities;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TransparentOverlay
    {
        private JFrame frameTime;
        private JFrame frameTemp;
        private JLabel timelabel;
        private JLabel templabel;
        
        public TransparentOverlay() 
        {
            frameTime= new JFrame();
            
            timelabel= new JLabel();
            timelabel.setOpaque(true);
            timelabel.setBackground(new Color(255, 255, 255, 128));
            
            frameTime.setUndecorated(true);
            AWTUtilities.setWindowOpaque(frameTime, false);
            
            frameTime.add(timelabel);
            frameTime.setSize(200,100);
            frameTime.setVisible(true);
            frameTime.setLocation(200,200);
            frameTime.setAlwaysOnTop(true);
            
            frameTemp= new JFrame();
            
            templabel= new JLabel();
            templabel.setOpaque(true);
            templabel.setBackground(new Color(255, 255, 255, 128));
            
            frameTemp.setUndecorated(true);
            AWTUtilities.setWindowOpaque(frameTemp, false);
            
            frameTemp.add(templabel);
            frameTemp.setSize(200,100);
            frameTemp.setVisible(true);
            frameTemp.setLocation(200,300);
            frameTemp.setAlwaysOnTop(true);
            
        }

        
        public void setTimeText(String text)
        {
            frameTime.remove(timelabel);
            frameTime.pack();
            timelabel = null;
            timelabel = new JLabel(text);
            timelabel.setOpaque(true);
            timelabel.setLocation(30, 10);
            timelabel.setBackground(new Color(255, 255, 255, 128));
            timelabel.setFont(new java.awt.Font("Dialog", 0, 40)); // NOI18N
            timelabel.setHorizontalAlignment(SwingConstants.CENTER);
            frameTime.add(timelabel);
            //frame.pack();
            frameTime.setSize(200,100);
        }
        
        public void setTempText(String text)
        {
            frameTemp.remove(templabel);
            frameTemp.pack();
            templabel=null;
            templabel = new JLabel(text);
            templabel.setOpaque(true);
            templabel.setLocation(30, 10);
            templabel.setBackground(new Color(255, 255, 255, 128));
            templabel.setFont(new java.awt.Font("Dialog", 0, 40)); // NOI18N
            templabel.setHorizontalAlignment(SwingConstants.CENTER);
            frameTemp.add(templabel);
            //frame.pack();
            frameTemp.setSize(200,100);
        }
    }
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import rackserver.UI.RackServerFrame;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;

/**
 *
 * @author Matteo
 */
public class Main 
{ 
    public static void main(String args[]) 
    {         
        RackServerFrame frame = new RackServerFrame();
        
        Rectangle maxBoundSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(maxBoundSize.getSize());
        frame.setUndecorated(true);
        frame.setVisible(true); 
        
        Server application = new Server(frame);
        FrameListener listener = new FrameListener(frame, application);
        frame.setMouseListener(listener);
        
        new Thread(application).start();
    }   
}

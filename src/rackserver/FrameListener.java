/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import java.awt.Frame;
import java.awt.event.*;
import rackserver.UI.RackServerFrame;

/**
 *
 * @author Matteo
 */
public class FrameListener extends MouseAdapter
{
    RackServerFrame serverFrame;
    Server context;
    
    public FrameListener(RackServerFrame serverFrame, Server context) {this.serverFrame = serverFrame; this.context = context;}
    
    @Override
    public void mouseClicked(MouseEvent me)
    {
        Object sender = me.getSource();
        if(sender.equals(serverFrame.exitLabel))
        {
            UtilitiesClass.getInstance().CloseService(context);
            System.exit(0);
        }
        else if(sender.equals(serverFrame.minimizeLabel))
        {
            serverFrame.setState(Frame.ICONIFIED);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.UI;

import java.awt.Color;
import java.util.Scanner;
import rackserver.Application;
import rackserver.RunnableUtils.DigitalClock;
import rackserver.UtilitiesClass;


/**
 *
 * @author aguiz
 */
public class Overlay extends javax.swing.JFrame 
{
    private boolean overlayVisible;
    private final Application context;
    public Overlay(Application context) 
    {
        this.context = context;
        initComponents();
        initFrame();
    }
    
    private void initFrame()
    {
        overlayVisible = true;
        new Thread(new DigitalClock(clockLabel)).start();
        new Thread(new TemperatureThread()).start();
        this.dispose();
        this.setUndecorated(true);
        this.setLocation(30,0);
        this.setSize(190,150);
        this.setVisible(true);
        this.pack();      
    }
    
    private class TemperatureThread implements Runnable
    {     
        @Override
        public void run()
        {
            while(overlayVisible)
            {
                try{Thread.sleep(1000);} catch(Exception e) {};
                tempLabel.setText(context.currentTemperature + "°"); 
                float temperature = 36f;
                if(context.currentTemperature != null)
                    temperature = Float.parseFloat(context.currentTemperature);
                int[] rgbValues = UtilitiesClass.getInstance().getRGBValuesFromTemperature(temperature);
                tempLabel.setForeground(new Color(rgbValues[0], rgbValues[1], rgbValues[2]));
            }
        }
    }
    
    public void Destroy()
    {
        overlayVisible = false;
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clockLabel = new javax.swing.JLabel();
        tempLabel = new javax.swing.JLabel();

        setAlwaysOnTop(true);
        setResizable(false);
        setSize(new java.awt.Dimension(200, 200));

        clockLabel.setFont(new java.awt.Font("Dialog", 0, 40)); // NOI18N
        clockLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        tempLabel.setFont(new java.awt.Font("Dialog", 0, 40)); // NOI18N
        tempLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clockLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tempLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(tempLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clockLabel;
    private javax.swing.JLabel tempLabel;
    // End of variables declaration//GEN-END:variables
}

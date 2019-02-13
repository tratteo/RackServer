/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.UI;


import java.awt.*;
import javax.swing.text.DefaultCaret;

/*
 *
 * @author Matteo
 * 
 */

public class RackServerFrame extends javax.swing.JFrame
{
    public RackServerFrame() 
    {
        initComponents();  
        DefaultCaret caret = (DefaultCaret)commandLineText.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        tratPiLabel.setForeground(Color.RED);
        guizPiLabel.setForeground(Color.RED);  
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        commandLineScroll = new javax.swing.JScrollPane();
        commandLineText = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        guizPiLabel = new javax.swing.JLabel();
        tratPiLabel = new javax.swing.JLabel();
        clockLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ClientConnectedText = new javax.swing.JTextArea();
        temperatureLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rack Server");
        setIconImages(null);
        setResizable(false);
        setSize(new java.awt.Dimension(1360, 500));

        commandLineScroll.setToolTipText("");

        commandLineText.setEditable(false);
        commandLineText.setBackground(new java.awt.Color(255, 244, 229));
        commandLineText.setColumns(20);
        commandLineText.setFont(new Font("Droid Sans Mono", Font.PLAIN, 20));
        commandLineText.setRows(5);
        commandLineText.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        commandLineText.setMargin(new java.awt.Insets(10, 10, 10, 10));
        commandLineScroll.setViewportView(commandLineText);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Connnected Pi:");

        guizPiLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        guizPiLabel.setForeground(new java.awt.Color(255, 0, 0));
        guizPiLabel.setText("GuizPi");

        tratPiLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        tratPiLabel.setForeground(new java.awt.Color(255, 0, 0));
        tratPiLabel.setText("TratPi");

        clockLabel.setFont(new java.awt.Font("Arial", 2, 48)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setText("Inside Temperature:");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setText("Client Connected:");

        jScrollPane1.setBackground(new java.awt.Color(102, 255, 255));
        jScrollPane1.setAutoscrolls(true);

        ClientConnectedText.setEditable(false);
        ClientConnectedText.setBackground(new java.awt.Color(204, 255, 204));
        ClientConnectedText.setColumns(20);
        ClientConnectedText.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        ClientConnectedText.setRows(5);
        ClientConnectedText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(ClientConnectedText);

        temperatureLabel.setFont(new java.awt.Font("Dialog", 0, 40)); // NOI18N
        temperatureLabel.setText("0°");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(commandLineScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(tratPiLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(guizPiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(temperatureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(commandLineScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(guizPiLabel)
                        .addGap(18, 18, 18)
                        .addComponent(tratPiLabel)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(temperatureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1458, 824);
    }// </editor-fold>//GEN-END:initComponents
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextArea ClientConnectedText;
    public javax.swing.JLabel clockLabel;
    private javax.swing.JScrollPane commandLineScroll;
    public javax.swing.JTextArea commandLineText;
    public javax.swing.JLabel guizPiLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel temperatureLabel;
    public javax.swing.JLabel tratPiLabel;
    // End of variables declaration//GEN-END:variables
}

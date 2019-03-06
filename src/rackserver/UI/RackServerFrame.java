/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.UI;


import java.awt.*;
import javax.swing.text.DefaultCaret;
import rackserver.FrameMouseListener;

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
    
    public void setMouseListener(FrameMouseListener listener) 
    {
        exitLabel.addMouseListener(listener);
        minimizeLabel.addMouseListener(listener);
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
        clientsScrollArea = new javax.swing.JScrollPane();
        conectedClientText = new javax.swing.JTextArea();
        temperatureLabel = new javax.swing.JLabel();
        exitLabel = new javax.swing.JLabel();
        minimizeLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rack Server");
        setBackground(new java.awt.Color(51, 51, 255));
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

        clientsScrollArea.setBackground(new java.awt.Color(102, 255, 255));
        clientsScrollArea.setAutoscrolls(true);

        conectedClientText.setEditable(false);
        conectedClientText.setBackground(new java.awt.Color(204, 255, 204));
        conectedClientText.setColumns(20);
        conectedClientText.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        conectedClientText.setRows(5);
        conectedClientText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        clientsScrollArea.setViewportView(conectedClientText);

        temperatureLabel.setFont(new java.awt.Font("Dialog", 0, 40)); // NOI18N
        temperatureLabel.setText("0°");

        exitLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit_18dp.png"))); // NOI18N

        minimizeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minimize_18dp.png"))); // NOI18N

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
                    .addComponent(clientsScrollArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(minimizeLabel)
                .addGap(18, 18, 18)
                .addComponent(exitLabel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minimizeLabel)
                    .addComponent(exitLabel))
                .addGap(16, 16, 16)
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
                        .addComponent(clientsScrollArea, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(temperatureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1458, 824);
    }// </editor-fold>//GEN-END:initComponents
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane clientsScrollArea;
    public javax.swing.JLabel clockLabel;
    private javax.swing.JScrollPane commandLineScroll;
    public javax.swing.JTextArea commandLineText;
    public javax.swing.JTextArea conectedClientText;
    public javax.swing.JLabel exitLabel;
    public javax.swing.JLabel guizPiLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JLabel minimizeLabel;
    public javax.swing.JLabel temperatureLabel;
    public javax.swing.JLabel tratPiLabel;
    // End of variables declaration//GEN-END:variables
}

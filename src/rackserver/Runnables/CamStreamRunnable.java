/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver.Runnables;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rackserver.Server;

public class CamStreamRunnable implements Runnable
{
    byte[] handshakeData, current;
    DatagramSocket socket;
    int packetSize, packetNumber, waitTime;
    
    Server server;
    ClientRunnable client;
    Webcam webcam;
    boolean stop = false;
    
    int mappedPort;
    DatagramPacket handshakePacket;

    
    public CamStreamRunnable(Server server, ClientRunnable client)
    {
        this.server = server;
        this.client = client;
        packetSize = 4096;
        packetNumber = 5;
        waitTime = (1000 / 15) / packetNumber;
    }
    
        @Override
        public void run() 
        {
            try
            {                               
                //Wait for client Hand shake
                socket = new DatagramSocket(7676);
                handshakeData = new byte[3];
              
//                descriptionArea.setText("Maximum transferable size per frame: "+packetNumber * packetSize
//                                      + " bytes\nEffective transferable size per frame: "+(packetNumber * (packetSize - 5))
//                                      + " bytes\nSize used for protocol management: "+5*packetNumber+" bytes"
//                                      + "\n\nProtocol initialized: \nPacket size: "+packetSize+" b, packets number: "+packetNumber+", FPS(cap): "+(1000 /( packetNumber * waitTime)));
                
                DatagramPacket recv_packet = new DatagramPacket(handshakeData, handshakeData.length);
                System.out.println("Waiting for handshake");
                socket.receive(recv_packet);
                System.out.println("handshake");
                webcam = Webcam.getDefault();
                webcam.setViewSize(new Dimension(320, 240));
                webcam.open();
                BufferedImage img = webcam.getImage();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", baos);
                baos.flush();
                byte[] frameData = baos.toByteArray();
                if(frameData.length >= (packetNumber * (packetSize - 5)))
                {
                    server.frame.commandLineText.append("Can't start stream with this protocol\n");
                    stop = true;
                    return;
                }
                //Prepare protocol info and send them to the client
                //Need 2 byte for the packet dimension field, one byte is not enough (one byte goes from 0-255, 2 bytes goes from 0-65536)
                //Split first 8 bits and last 8 bits of an integer into 2 bytes
                byte a = (byte)(packetSize >> 8);
                byte b = (byte)packetSize;
                //Place the data in the handshake packet
                handshakeData[0] = a;
                handshakeData[1] = b;
                //Packet number for this protocol, 0-255 is enough
                handshakeData[2] = (byte)packetNumber;
                DatagramPacket protocolDataPacket = new DatagramPacket(handshakeData, handshakeData.length, recv_packet.getAddress(), recv_packet.getPort());
                //Send protocol data
                socket.send(protocolDataPacket);
                try{Thread.sleep(1500);}catch(Exception e){}
                while(!stop)
                {   
                    img = webcam.getImage();
                    baos = new ByteArrayOutputStream();
                    ImageIO.write(img, "jpg", baos);
                    baos.flush();
                    frameData = baos.toByteArray();
                    //Effective dimension for each packet
                    int fraction = frameData.length / packetNumber;
                    int from = 0, to;
                    for(int i = 0; i < packetNumber-1; i++)
                    {
                        to = (fraction * (i+1)) - 1;
                        current = new byte[packetSize];
                        //Copy the right part of the total frame into the current packet
                        System.arraycopy(frameData, from, current, 0, fraction);
                        
                        a = (byte)(fraction >> 8);
                        b = (byte)fraction;
                        //First packet of frame
                        if(i == 0)
                        {
                            current[(current.length - 1)] = (byte)'S';
                        }
                        
                        //Set effective dimension data in the last 2 bytes of the packet
                        current[(current.length - 2)] = a;
                        current[(current.length - 3)] = b;
                        a = (byte)(frameData.length >> 8);
                        b = (byte)(frameData.length);
                        //Set total frame length in the last 2-4 bytes of the packet
                        current[(current.length - 4)] = a;
                        current[(current.length - 5)] = b;
                        
                        from = to + 1;
                        //Prepare and send packet
                        DatagramPacket sendPack = new DatagramPacket(current, current.length, recv_packet.getAddress(), recv_packet.getPort());
                        socket.send(sendPack);
                        try{Thread.sleep(waitTime);}catch(Exception e){}
                    }
                    //Prepare and send last packet
                    to = frameData.length-1;
                    current = new byte[packetSize];
                    //Effective remaining dimension, put all in the last packet (will contain only the sum of the rest of the fraction division, so last packet will be +- 2 byte)
                    int length = to-from+1;
                    System.arraycopy(frameData, from, current, 0, to-from+1);
                    a = (byte)(length >> 8);
                    b = (byte)length;
                    //Set effective dimension and terminator in the last packet
                    current[(current.length - 1)] = (byte)'E';
                    current[(current.length - 2)] = a;
                    current[(current.length - 3)] = b;
                    //System.out.println("Last packet, f: "+from+", to: "+to+", length: " +(to-from+1)+"\nA: "+a+", B: "+b);
      
                    //Prepare and send packet
                    DatagramPacket sendPack = new DatagramPacket(current, current.length, recv_packet.getAddress(), recv_packet.getPort());
                    socket.send(sendPack);
                    //System.out.println("Sent");
                }
            }
            catch(Exception e){}
        }
    
    public void kill()
    {
        stop = true;
        webcam.close();
        socket.close();
    }
    
    public synchronized boolean isRunning() {return !stop;}
}

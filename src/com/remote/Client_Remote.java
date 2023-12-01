/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.remote;

import com.remote.desktop_control_client.StartClient;
import com.remote.desktop_control_server.InitConnection;
import com.remote.desktop_control_server.StartServer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jdk.management.resource.ResourceApprover;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author UtsavVatsa
 */
public class Client_Remote {

    protected static ResourceBundle confi;
    protected Admin_Panel admin_Panel;
    protected AdminUserClient adminUserClient;

    protected StartClient ControlClient;

    public String userIP;
    public String userName;
    public boolean online;
    public boolean remote;

    protected JPanel tempPanel;
    protected JPanel detPanel;
    protected JLabel nLabel;
    protected JLabel ipLabel;
    protected JLabel tempLabel;
    protected JLabel statLabel;

    public Client_Remote(Admin_Panel ap, String IP, String name, BufferedImage startImage, int connNo) throws ClassNotFoundException, Exception {
        //CONSTRUCTOR

        this.userIP = IP;
        this.userName = name;
        this.confi = ResourceBundle.getBundle("config", Locale.getDefault());
        this.online = true;
        this.remote = false;

        this.admin_Panel = ap;

        //MAIN USER SCREEN PANEL
        tempPanel = new JPanel();
        LayoutManager overlay = new OverlayLayout(tempPanel);
        tempPanel.setLayout(overlay);
        //tempPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tempPanel.setBackground(Color.black);

        //USER DETAILS PANEL
        detPanel = new JPanel();
        detPanel.setLayout(new BoxLayout(detPanel, BoxLayout.PAGE_AXIS));

        nLabel = new JLabel(name);
        nLabel.setForeground(Color.white);
        nLabel.setFont(new Font("Verdana", Font.BOLD, 18));

        ipLabel = new JLabel(IP);
        ipLabel.setForeground(Color.white);
        ipLabel.setFont(new Font("Verdana", 1, 16));

        statLabel = new JLabel("Connected");
        statLabel.setForeground(Color.white);
        statLabel.setFont(new Font("Verdana", 1, 16));

        detPanel.add(nLabel);
        detPanel.add(ipLabel);
        detPanel.add(statLabel);
        detPanel.setBackground(new Color(40, 40, 40, 90));
        detPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        tempPanel.add(detPanel);

        tempPanel.addMouseListener(new java.awt.event.MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Remotely Connecting :" + userName);
                remote = true;

                //throw new UnsupportedOperationException("Not supported yet); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        //SETTING IMAGE
        tempLabel = new JLabel();
        //startImage=Util.shrink(startImage, 20/100D);
        //TODO should be done earlier

        tempLabel.setIcon(new ImageIcon(startImage));
        tempPanel.add(tempLabel);

        ap.grid.add(tempPanel);
        ap.frame.pack();
        //ap.screens.add(tempLabel);

        startThread();
    }

    public void startThread() throws SQLException, Exception {
        //START THREAD TO RECEIVE IMAGES IN SMALL PANEL
        adminUserClient = new AdminUserClient(this);
        adminUserClient.start();
    }

    public void startRemoteClient() {
        System.out.println("Starting Remote Control Client for :" + userIP + " " + userName);
        ControlClient = new StartClient(this);
    }

    public void pauseThread() {
        statLabel.setText("Disconnected");
        online = false;
    }

    public void resumeThread(String newname) {
        statLabel.setText("Connected");
        this.userName = newname;
        nLabel.setText(this.userName);
        online = true;
        adminUserClient.resume();
    }

    public void updateScreen(BufferedImage img) {
        //img=Util.shrink(img, 24/100D);
        //TODO change 

        tempLabel.setIcon(new ImageIcon(img));
    }
}

class AdminUserClient extends Thread {

    Client_Remote parent;
    public Socket client;  //SOCKET TO RECEIVE IMAGES IN SMALL PANEL
    public String serverIP;
    int serverPort;
    public BufferedImage image;
    public String serverName;

    public AdminUserClient(Client_Remote parent) throws IOException, SQLException, ClassNotFoundException, Exception {
        this.parent = parent;
        this.serverIP = parent.userIP;
        this.serverPort = 7078;
        this.serverName = parent.userName;
    }

    @Override
    public void run() {
        int x = 0;
        try {
            System.out.println("Loop " + x++ + "for user " + parent.userName + " at IP: " + parent.userIP);
            while (true) {
                if (parent.online == false) //IF USER_PANEL is closed then suspend this thread
                {
                    this.suspend();
                }

                client = new Socket( serverIP, serverPort);
                DataOutputStream dout = new DataOutputStream(client.getOutputStream());
                DataInputStream din = new DataInputStream(client.getInputStream());

                try {

                    if (parent.remote == true) {
                        dout.writeInt(1); // writing 1 to start remote control server in user_panel
                        //parent.ControlClient=new StartClient(parent);
                        parent.startRemoteClient();
                        parent.remote = false;
                    } else {
                        dout.writeInt(-1);  // writing -1 indicating no need to start remote control server in user_panel 
                    }

                    //METHOD 1
//                        byte[] sizeAr = new byte[4];
//                        din.read(sizeAr);
//                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
//                        total+=size;
//                        System.out.println("total="+total);
//                        byte[] imageAr= new byte[size];
//                        din.read(imageAr);
//                        BufferedImage img= ImageIO.read(new ByteArrayInputStream(imageAr));     
                    this.sleep(200);
                    if (parent.online != false) {
                        ImageInputStream imgin = ImageIO.createImageInputStream(din);
                        if (imgin != null) {
                            BufferedImage img = (BufferedImage) ImageIO.read(imgin);
                            parent.updateScreen(img);
                        }
                    }
                    //imgin.flush();
                    client.close();

                } catch (SocketTimeoutException st) {
                    System.out.println("Admin User Client Socket timed out for :" + parent.userIP + " " + parent.userName);
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception ex) {
                    System.out.println(ex);
                    ex.printStackTrace();
                    break;
                }
            }//while ends

        } catch (SocketTimeoutException st) {
            System.out.println("Admin User Client Socket timed out for :" + parent.userIP + " " + parent.userName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

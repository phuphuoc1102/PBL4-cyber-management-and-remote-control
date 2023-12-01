package com.remote;
// MINE

import com.entity.Client;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import jdk.management.resource.ResourceApprover;
import javax.swing.UIManager;

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

import javax.imageio.ImageIO;

public class User_Panel implements WindowListener {

    private static ResourceBundle confi;
    public Socket clientSocket;
    private Client client;
    private String servIP;

    public UserServer us;
    public StartServer controlServer;

    ServerSocket remoteSocket;

    public User_Panel(Client client) {
        this.client = client;
        try {
            //CONSTRUCTOR
            if (System.getProperty("swing.defaultlaf") == null) {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                }
            }
            remoteSocket = new ServerSocket(7080);
        } catch (IOException ex) {
            Logger.getLogger(User_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        confi = ResourceBundle.getBundle("config", Locale.getDefault());
        clientSocket = new Socket();
        connect(client);
    }

    public void connect(Client client) {
        servIP = config("defaultServerIP");
        int result = connectToAdmin(client.getUser_name(), servIP);
    }

    public void resetPanel() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }
            clientSocket = null;
            if (us != null) {
                us.stop();
            }
            us = null;
            if (controlServer != null) {
                controlServer = null;
            }
            System.gc();
        } catch (IOException ex) {
            Logger.getLogger(User_Panel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int connectToAdmin(String name, String serverIP) {
        int port = Integer.parseInt(confi.getString("serverPort"));
        int result = 0;
        try {
            //CREATE CLIENT SOCKET TO MAKE NEW CONNECTION
            clientSocket.connect(new InetSocketAddress(serverIP, port), 150);
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream din = new DataInputStream(clientSocket.getInputStream());

            String ip = clientSocket.getLocalSocketAddress().toString();

            dout.writeUTF(ip);
            dout.writeUTF(name);
            dout.writeInt(9);    // +9 denotes connect

            //CHECK IF IP EXISTS 1-NEW 2-EXISTS 3-RECONNECT
            int status = din.readInt();

            if (status == 2) // EXISTS AND ONLINE
            {
                clientSocket.close();
                result = -1;
            }
            if (status == 3) //EXISTS AND OFFLINE 
            {
                System.out.println("Reconnecting");
                us = new UserServer(this, 7078);
                us.start();
                MultiCastClient mc = new MultiCastClient();
                mc.receive();
                result = 1;
            } else {
                //STATUS is 1   // NEW CONNECTION
                BufferedImage img = Util.getScreenshot();
                img = Util.shrink(img, 24 / 100D);
                //STEP 1
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", byteArrayOutputStream);
                byte[] arr = byteArrayOutputStream.toByteArray();
                byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
                dout.write(size);
                dout.write(byteArrayOutputStream.toByteArray());
                int mesg = din.readInt();
                if (mesg == 1) {
                    System.out.println("Success");
                    us = new UserServer(this, 7078);
                    us.start();
                    MulticastStringReceive();
                }

                clientSocket.close();
                result = 1;

            }//else closes

        }//try closes
        catch (IOException ex) {
            Logger.getLogger(User_Panel.class
                    .getName()).log(Level.SEVERE, null, ex);
            resetPanel();

        } catch (AWTException ex) {
            Logger.getLogger(User_Panel.class
                    .getName()).log(Level.SEVERE, null, ex);
            resetPanel();

        } catch (Exception ex) {
            Logger.getLogger(User_Panel.class
                    .getName()).log(Level.SEVERE, null, ex);
            resetPanel();
        }
        return result;
    }

    public String config(String key) {

        ResourceBundle config = ResourceBundle.getBundle("config", Locale.getDefault());
        String value;
        try {
            value = config.getString(key);
        } catch (Exception e) {
            value = "";
        }

        return value;
    }

    public void MulticastStringReceive() {
        byte buf[] = new byte[65507];
        String ip = config("defaultClientIP");
        int port = Integer.parseInt(config("defaultPort"));
        InetAddress ia = null;
        MulticastSocket ms = null;

        try {
            ia = InetAddress.getByName(ip);
            ms = null;
            ms = new MulticastSocket(port);
            ms.joinGroup(ia);
        } catch (UnknownHostException ex) {
            Logger.getLogger(User_Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(User_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }

        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        ByteArrayInputStream bais = null;
        BufferedImage img = null;
        int x;
        while (true) {
            try {
                ms.receive(dp);
                bais = new ByteArrayInputStream(dp.getData());
                img = ImageIO.read(bais);
            } catch (IOException ex) {
                Logger.getLogger(User_Panel.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }

        }
        ms.close();
    }

    public void disConnect(String name, String serverIP) {
        int port = Integer.parseInt(confi.getString("serverPort"));

        try {
            //CREATE CLIENT SOCKET TO MAKE NEW CONNECTION
            clientSocket = new Socket(serverIP, port);
            DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream din = new DataInputStream(clientSocket.getInputStream());

            String ip = clientSocket.getLocalSocketAddress().toString();
            dout.writeUTF(ip);
            dout.writeUTF(name);
            dout.writeInt(-9);    //-9 denotes disconnect

            clientSocket.close();

            System.exit(1);
            //  System.exit();

        } catch (IOException ex) {
            Logger.getLogger(User_Panel.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // DO NOTHING
        // User_Panel u = new User_Panel();
        // u.build();
        User_Panel user_panel = new User_Panel(new Client());
    }

    public void startRemoteServer() {
        System.out.println("Starting Remote Server");
        controlServer = null;
        controlServer = new StartServer(remoteSocket);

    }

    @Override
    public void windowOpened(WindowEvent e) {
        int x = 10;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        disConnect(this.client.getName(), servIP);
        us.close();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
}

class UserServer extends Thread {

    User_Panel parent;
    ServerSocket serverSocket;
    Socket server;
    int port;
    int x;
    boolean open;

    public UserServer(User_Panel parent, int p) throws IOException, ClassNotFoundException, Exception {
        this.parent = parent;
        port = p;
        serverSocket = new ServerSocket(port);
        x = 0;

        open = true;
    }

    public void close() {
        open = false;

    }

    public void run() {
        try {
            while (open) {
                server = serverSocket.accept();
                DataInputStream din = new DataInputStream(server.getInputStream());
                DataOutputStream dout = new DataOutputStream(server.getOutputStream());

                try {
                    //dout.writeInt(9);
                    int remote = din.readInt();  //1 to start
                    if (remote == 1) {
                        parent.startRemoteServer();
                        remote = -1;
                    }
                    BufferedImage img = Util.getScreenshot();
                    img = Util.shrink(img, 24 / 100D);
//                       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); 
//                    ImageIO.write(img, "jpg", byteArrayOutputStream);
//                    byte[] arr=byteArrayOutputStream.toByteArray();
//                        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
//                        dout.write(size);
//                        dout.write(byteArrayOutputStream.toByteArray());
                    ImageIO.write(img, "jpg", dout);
                    server.close();
                    dout.flush();
//                            //pw.flush();
                    // System.out.println("hi:"+(x++));

//                        }
//                        else 
//                           parent.open=false;
                } catch (IOException ex) {
                    server.close();
                    dout.flush();
                    parent.resetPanel();

                    Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AWTException ex) {
                    Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
                    server.close();
                    dout.flush();
                    parent.resetPanel();
                }

//                   else
//                   {
//                       //handle close
//                      // dout.writeInt(-9);
//                       dout.writeInt(-9);
//                       server.close();
//                       break;
//                       
//                       
                //}
            }
            //server.close();
        } catch (IOException ex) {
            Logger.getLogger(UserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

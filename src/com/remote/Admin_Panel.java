package com.remote;

import com.entity.Computer;
import java.awt.Toolkit;
import java.util.*;
import java.awt.image.BufferedImage;

import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Admin_Panel
        implements ActionListener, WindowListener {

    protected ArrayList<Client_Remote> clients;
    private AdminServer adminServer;
    public JFrame frame;
    public JPanel grid;
    public JLabel cs;
    public static int connSize;

    public java.util.Date dt;
    public SimpleDateFormat sdf;

    public Admin_Panel() {
        //CONSTRUCTOR
        if (System.getProperty("swing.defaultlaf") == null) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
            }
        }
        clients = new ArrayList<Client_Remote>();
        dt = new java.util.Date();
        sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        connSize = 0;
        frame = new JFrame("Admin Panel");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        frame.setSize(xSize, ySize);
        //frame.setResizable(false);
//        frame.setVisible(true);

        // COLOR
        int red = 55;
        int green = 55;
        int blue = 55;
        Color back = new Color(red, green, blue);
        red = 200;
        green = 200;
        blue = 200;
        Color white = new Color(red, green, blue);
        red = 12;
        green = 122;
        blue = 207;
        frame.getContentPane().setBackground(back);
        frame.setLayout(new BorderLayout());
        grid = new JPanel(new GridLayout(0, 4));
        grid.setBackground(new Color(63, 63, 63));
        JScrollPane screenPane = new JScrollPane(grid);
        screenPane.setSize(xSize, ySize);
//        screenPane.setVisible(true);
        screenPane.setBorder(null);
        frame.add(screenPane, BorderLayout.CENTER);
        JPanel buttonPane = new JPanel();
        frame.add(buttonPane, BorderLayout.SOUTH);

        //  frame.pack();
        JPanel connPane = new JPanel();
        JLabel tempL = new JLabel("Connected Clients : ");
        cs = new JLabel();
        connPane.add(tempL);
        connPane.add(cs);
        cs.setText("" + connSize);
        connPane.setBackground(back);
        cs.setForeground(white);
        tempL.setForeground(white);
        buttonPane.add(connPane);

        // STARTS THREAD TO RECIEVE INFO FROM CLIENTS
        receive();

    }

    public void receive() {
        try {
            adminServer = new AdminServer(this);
            adminServer.start();
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void MultiCastString() {

        String ip = config("defaultIP");
        int port = Integer.parseInt(config("defaultPort"));
        try {
            InetAddress ia = InetAddress.getByName(ip);
            MulticastSocket ms = null;
            try {
                ms = new MulticastSocket();
                ms.setTimeToLive(64); //gói dữ liệu multicast sẽ được chuyển tiếp tối đa qua 64 router hoặc mạng trung gian trước khi bị hủy bỏ
            } catch (IOException ex) {
                System.out.println("Mulicast Exception");
                ex.printStackTrace();
                Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
            int x = 1;
            BufferedImage img = null;
            while (true) {
                byte[] buffer = null;
                try {
                    img = Util.getScreenshot();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(img, "jpg", baos);
                    baos.flush();
                    buffer = baos.toByteArray();
                } catch (AWTException ex) {
                    Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ia, port);
                System.out.println(buffer.length);
                System.out.println(buffer);

                buffer = Pack.compressData(buffer);
                System.out.println(buffer.length);
                System.out.println(buffer);

                try {
                    ms.send(dp);
                } catch (IOException ex) {
                    Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }

            ms.close();

        } catch (UnknownHostException ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public byte[] intToByteArray(int a) {
        return new byte[]{
            (byte) ((a >> 24) & 0xFF),
            (byte) ((a >> 16) & 0xFF),
            (byte) ((a >> 8) & 0xFF),
            (byte) (a & 0xFF)
        };

    }

    public int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF
                | (b[2] & 0xFF) << 8
                | (b[1] & 0xFF) << 16
                | (b[0] & 0xFF) << 24;

    }

    public String config(String key) {

        ResourceBundle config = ResourceBundle.getBundle("desktop_vnc.config", Locale.getDefault());
        String value;
        try {
            value = config.getString(key);
        } catch (Exception e) {
            value = "";
        }

        return value;
    }

    public void createConn(String IP, String name, BufferedImage img) {
        try {
            connSize++;
            cs.setText("" + connSize);
            Client_Remote newClient = new Client_Remote(this, IP, name, img, connSize);
            clients.add(newClient);

            System.out.println(" Name: " + name + " IP: " + IP + " Connected at: " + sdf.format(dt));

        } catch (Exception ex) {
            Logger.getLogger(Admin_Panel.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception! Cannot Add User!");
            ex.printStackTrace();
        }
    }

    public void refreshConn(Client_Remote user, String name) {
        System.out.println(" Name: " + name + " IP: " + user.userIP + " ReConnected at: " + sdf.format(dt) + " with new Name: " + name);
        connSize++;
        cs.setText("" + connSize);
        //RESUMING THREAD FOR USER AND SENDING NEW NAME
        user.resumeThread(name);
    }

    public void closeConn(String IP, String Name) {
        for (Client_Remote user : clients) {
            if (IP.equalsIgnoreCase(user.userIP) == true) {
                user.pauseThread();
                connSize--;
                cs.setText("" + connSize);
                System.out.println(" Name: " + Name + " IP: " + IP + " disconnected at: " + sdf.format(dt));
                break;
            }
        }
    }

    public static void main(String[] args) {
        Admin_Panel admin_Panel = new Admin_Panel();
        admin_Panel.frame.setVisible(true);
    }

    public void windowActivated(WindowEvent windowevent) {
    }

    public void windowClosed(WindowEvent windowevent) {
    }

    public void windowClosing(WindowEvent arg) {
        //frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //DO SOMETHING
    }

    public void windowDeactivated(WindowEvent windowevent) {
    }

    public void windowDeiconified(WindowEvent windowevent) {
    }

    public void windowIconified(WindowEvent windowevent) {
    }

    public void windowOpened(WindowEvent windowevent) {
    }

    public void actionPerformed(ActionEvent action) {
    }

}

class AdminServer extends Thread {

    Admin_Panel parent;
    private ServerSocket serverSocket;
    private Socket server;
    private BufferedImage image;

    public AdminServer(Admin_Panel parent) throws IOException, SQLException, ClassNotFoundException, Exception {
        this.parent = parent;
        serverSocket = new ServerSocket(7077);
    }

    public void run() {
        String IP = "";
        String name = "";
        while (true) {
            try {
                server = serverSocket.accept();
                DataInputStream din = new DataInputStream(server.getInputStream());
                DataOutputStream dout = new DataOutputStream(server.getOutputStream());

                // IDENTIFY CONNECT AND DISCONNECT
                IP = din.readUTF();
                IP = IP.substring(0, (IP.indexOf(":")));
                IP = IP.substring(1);
                name = din.readUTF();

                int dowhat = din.readInt();   //+9 to connect -9 to disconnect

                if (dowhat == 9) {
                    //CONNECT
                    int flag = 1;
                    for (Client_Remote user : parent.clients) {

                        if (IP.equalsIgnoreCase(user.userIP) == true) // IP EXISTS
                        {
                            if (user.online == true) //IP EXISTS AND ONLINE
                            {
                                flag = 2;
                                break;
                            } else // IP EXISTS BUT OFFLINE  
                            {
                                dout.writeInt(3);    //writing 3 to indicate re-connection
                                parent.refreshConn(user, name); // refreshing connection sending newname in name
                                flag = 3;
                                break;      //RESUMING 
                            }
                        }

                    }

                    if (flag == 2) //IP ALREADY EXISTS AND ONLINE
                    {
                        dout.writeInt(2);   // writing 2 to indicate already connected
                        Thread.sleep(30);
                        server.close();
                    }
                    if (flag == 1) {                       //CREATE NEW CONNECTION
                        dout.writeInt(1);   //writing 1 to indiacte new connection                
                        byte[] sizeAr = new byte[4];
                        din.read(sizeAr);
                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
                        byte[] imageAr = new byte[size];
                        din.read(imageAr);
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageAr));

                        dout.writeInt(1);

                        parent.createConn(IP, name, img);                   //CREATE CONENCTION
                        server.close();

                    }//if flag==1 ends here
                } else {
                    //DISCONNECT
                    parent.closeConn(IP, name);
                    server.close();
                }
            }//try ends here
            catch (SocketTimeoutException st) {
                System.out.println("Admin Server Socket timed out!");
                System.out.println("While attempting to connect " + IP + " of: " + name);
                st.printStackTrace();
                // break;
            } catch (IOException e) {
                System.out.println("IO Exception");
                System.out.println("While attempting to connect " + IP + " of: " + name);
                e.printStackTrace();
                //break;
            } catch (Exception ex) {
                System.out.println(ex);
                System.out.println("While attempting to connect " + IP + " of: " + name);
                ex.printStackTrace();
            }
        }//while loop ends
    }
}

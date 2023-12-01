package com.remote.desktop_control_server;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitConnection {

    ServerSocket socket = null;
    Socket sc = null;
    DataOutputStream outstream = null;
    StartServer parent;
    SendScreen senscre;
    ReceiveEvents re;

    InitConnection(int port, StartServer Caller, ServerSocket ss) {
        this.parent = Caller;
        Robot robot = null;
        Rectangle rectangle = null;
        try {
            System.out.println("Awaiting Connection from Client");
            socket = ss;
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            String width = "" + dim.getWidth();
            String height = "" + dim.getHeight();
            rectangle = new Rectangle(dim);
            robot = new Robot(gDev);
            drawGUI();
            sc = socket.accept();
            outstream = new DataOutputStream(sc.getOutputStream());
            System.out.println("Hi");
            outstream.writeUTF(width);
            outstream.writeUTF(height);
            senscre = new SendScreen(sc, robot, rectangle, this);
            re = new ReceiveEvents(sc, robot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawGUI() {
    }

    public void closeINIT() {
        try {
            sc.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(InitConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void endConn() {
        re.finalize();
        re = null;
        System.gc();
        senscre.finalize();
        System.gc();
        closeINIT();
        System.out.println("Closing Remote Desktop Server ");
        parent.close();
    }
}

package com.remote.desktop_control_client;

import com.remote.Client_Remote;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
public class StartClient {

    static String port;
    static String IP;
    public Client_Remote parent;
    public CreateFrame CF;
    public DataOutputStream outstream;
    public DataInputStream instream;
    public String width;
    public String height;
    private Socket cSocket;

    public StartClient(Client_Remote user) {
        //CONSTRUCTOR
        port = "7080";
        this.parent = user;
        this.IP = user.userIP;
        Start();
    }

    public void Start() {
        System.out.println("Remote Desktop Client Started for Client:" + parent.userIP + " " + parent.userName);
        initialize(Integer.parseInt(port));
    }

    public void initialize(int port) {
        System.out.println("Connecting to the Server of Client:" + parent.userIP + " " + parent.userName);
        try {
            cSocket = new Socket(parent.userIP, port);
            outstream = new DataOutputStream(cSocket.getOutputStream());
            instream = new DataInputStream(cSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            width = instream.readUTF();
            height = instream.readUTF();
            System.err.println("width = " + width);
            System.err.println("height = " + height);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        CF = new CreateFrame(cSocket, width, height, this);
    }

    public void freeChild() {
        CF.frame.dispose();
        CF = null;
        System.out.println("Closing Remote Desktop Client for :" + parent.userIP + " " + parent.userName);
    }

}

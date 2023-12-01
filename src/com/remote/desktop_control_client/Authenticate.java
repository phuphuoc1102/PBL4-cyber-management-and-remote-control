package com.remote.desktop_control_client;

import com.remote.Client_Remote;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class Authenticate extends JFrame implements ActionListener {

    private Socket cSocket = null;
    DataInputStream verification = null;
    JButton SUBMIT;
    JPanel panel;
    JLabel label, label1;
    String width = "", height = "";
    final JTextField text1;
    Client_Remote parent;
    int port;
    StartClient caller;
    CreateFrame abc;

    Authenticate(Client_Remote client_remote, int port, StartClient cl) {
        this.parent = client_remote;
        this.port = port;
        label1 = new JLabel();
        label1.setText("Password");
        text1 = new JTextField(15);
        caller = cl;
        label = new JLabel();
        label.setText("");
        this.setLayout(new BorderLayout());
        SUBMIT = new JButton("SUBMIT");
        panel = new JPanel(new GridLayout(2, 1));
        panel.add(label1);
        panel.add(text1);
        panel.add(label);
        panel.add(SUBMIT);
        add(panel, BorderLayout.CENTER);
        SUBMIT.addActionListener(this);
        setTitle("LOGIN FORM");
    }

    public void actionPerformed(ActionEvent ae) {
        String value1 = text1.getText();

        try {
            cSocket = new Socket(parent.userIP, port);
            verification = new DataInputStream(cSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            width = verification.readUTF();
            height = verification.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            cSocket.close();
            System.out.println("Closing Sockets and connection for: " + parent.userName + " " + parent.userIP);
            abc = null;
            caller.freeChild();
        } catch (IOException ex) {
            Logger.getLogger(Authenticate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

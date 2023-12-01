package com.remote.desktop_control_server;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SetPassword extends JFrame implements ActionListener {

    JButton SUBMIT;
    JPanel panel;
    JLabel label1;
    JTextField text1;
    String value1;
    JLabel label;

    SetPassword() {
        label1 = new JLabel();
        label1.setText("Set Password");
        text1 = new JTextField(15);
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
        setTitle("Set Password to connect to the Client");
    }
    public void actionPerformed(ActionEvent ae) {
        value1 = text1.getText();
        dispose();
    }

    public String getValue1() {
        return value1;
    }

    public static void main(String[] args) {
        SetPassword frame1 = new SetPassword();
        frame1.setSize(300, 80);
        frame1.setLocation(500, 300);
        frame1.setVisible(true);
    }
}

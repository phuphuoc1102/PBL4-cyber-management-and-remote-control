/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Time;
import javax.swing.JFrame;

/**
 *
 * @author boixi
 */
public class Client extends JFrame {

    private String user_name;
    private String password;
    private InetAddress host;
    private int port;
    private static Socket socket;
    private Time time_remaining;

    public Client(InetAddress host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.host = host;
        this.port = port;
    }

    public Client() {

    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InetAddress getHost() {
        return host;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Client.socket = socket;
    }

    public Time getTime_remaining() {
        return time_remaining;
    }

    public void setTime_remaining(Time time_remaining) {
        this.time_remaining = time_remaining;
    }
}


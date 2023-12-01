/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.untity;

/**
 *
 * @author ADMIN
 */
import java.net.Socket;

import java.net.Socket;

public class ShareSocketHolder {
    private static Socket socket = new Socket();

    private ShareSocketHolder() {} // Private constructor to prevent instantiation

    public static synchronized Socket getSocket() {
        if (socket == null) {
            throw new IllegalStateException("Socket has not been initialized yet.");
        }
        return socket;
    }

    public static synchronized void setSocket(Socket newSocket) {
        if (socket != null) {
            throw new IllegalStateException("Socket has already been initialized.");
        }
        socket = newSocket;
    }
}


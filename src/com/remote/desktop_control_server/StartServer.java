package com.remote.desktop_control_server;

import java.net.ServerSocket;

public class StartServer {

    InitConnection ins;
    ServerSocket socket = null;

    public StartServer(ServerSocket socket) {
        this.socket = socket;
        System.out.println("Remote Desktop Server Started");
        ins = new InitConnection(7080, this, socket);
    }
    public void close() {
        ins = null;
    }
}

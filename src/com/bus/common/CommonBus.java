package com.bus.common;


import com.bus.tcp.TcpClient;
import com.bus.tcp.TcpServer;
import com.gui.chat.MainChatPanel;
import java.awt.AWTException;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NotBoundException;

public class CommonBus {
    // TODO: for server
    private TcpServer tcp_server;
    // TODO: for client
    private TcpClient tcp_client;
    
        private ServerSocket request;


    public CommonBus() {
        this.tcp_server = new TcpServer();
        this.tcp_client = new TcpClient();
    }

    public void setMainChatPanel(MainChatPanel main_chat_panel) {
        this.tcp_server = new TcpServer(main_chat_panel);
        this.tcp_client = new TcpClient(main_chat_panel);
    }

   
    
    public TcpServer getTcpServer() {
        return this.tcp_server;
    }



    public TcpClient getTcpClient() {
        return this.tcp_client;
    }


    // TODO: handle events of server
   public void startListeningOnServer(String host, int port) throws IOException, AWTException {
        if(!this.tcp_server.isListening() ) {
            // Port rmi = port tcp + 1
            System.out.println("Start listening on server");
            this.tcp_server.startListeningOnTcpServer(host, port);
        }
    }
    public void startListening(String host, int port, String password) throws IOException, AWTException {
        if(!this.tcp_server.isListening() ) {
            // Port rmi = port tcp + 1
            System.out.println("Start listening");
            this.tcp_server.startListeningOnTcpServer(host, port);
//            this.rmi_server.startBindingOnRmiServer(host, port + 1);
        }
    }

    public void stopListeningOnServer() throws IOException, NotBoundException {
        if(this.tcp_server.isListening()) {
            this.tcp_server.stopListeningOnTcpServer();
        }
    }

    public void startConnectingToServer(String host, int port) throws Exception {
        // TODO: check server is listening?
        if(this.tcp_server.isListening()) {
            String ip_server = this.tcp_server.getServer().getInetAddress().getHostAddress();
            if(host.equals(ip_server)) throw new Exception("Can't remote yourself!");
            System.out.println(ip_server);
            System.out.println(host);
        }
        if(this.tcp_client.isConnectedServer()) throw new Exception("You are remoting!");
        this.tcp_client.startConnectingToTcpServer(host, port);
    }
}
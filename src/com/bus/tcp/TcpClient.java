package com.bus.tcp;

import com.bus.chat.ChatBus;
import com.gui.chat.MainChatPanel;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpClient extends Thread {

    private MainChatPanel main_chat_panel;

    private Socket client;
    private boolean is_connected_server;

    public TcpClient(MainChatPanel main_chat_panel) {
        this.client = null;
        this.is_connected_server = false;
        this.main_chat_panel = main_chat_panel;
    }
    public TcpClient() {
        this.client = new Socket();
        this.is_connected_server = false;
    }
    
   public void startConnectingToTcpServer(String host, int port) throws IOException {
        if(this.is_connected_server == false) {
            this.client = new Socket(host, port);
            DataOutputStream dos = new DataOutputStream(this.client.getOutputStream());
            DataInputStream dis = new DataInputStream(this.client.getInputStream());

//            String result = dis.readUTF();
            String result = true + "";
            if(result.equals("true")) {
                ChatBus chat_bus = new ChatBus(this.client);

                this.main_chat_panel.addNewConnection(chat_bus);
                this.is_connected_server = true;
            }
            else if(result.equals("false")) {
                this.client.close();
                throw new IOException("Wrong password of server");
            }
        }
    }


    public void stopConnectingToTcpServer() throws IOException {
        if (this.is_connected_server = true) {
            this.client.close();
            //this.chat_bus.setSocket(null);
            this.is_connected_server = false;
        }
    }

    public boolean isConnectedServer() {
        return this.is_connected_server;
    }

    public void setConnectedServer(boolean b) {
        this.is_connected_server = b;
    }

    public Socket getClient() {
        return this.client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }
    
}

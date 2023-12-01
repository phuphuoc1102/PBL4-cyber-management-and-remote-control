package com.bus.tcp;

import com.bus.chat.ChatBus;
import com.gui.chat.MainChatPanel;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class TcpServer {

    private MainChatPanel main_chat_panel;

    private ServerSocket server;
    private Socket client;
    private List<Socket> connectedSocket;
    private boolean is_listening;
    private boolean is_has_partner;

    public ServerSocket getServer() {
        return this.server;
    }

    public List<Socket> getConnectedSocket() {
        return this.connectedSocket;
    }

    public Socket getSocket() {
        return this.client;
    }

    public TcpServer(MainChatPanel main_chat_panel) {
        this.connectedSocket = new ArrayList<>();
        try {
            this.server = new ServerSocket();
            this.client = new Socket();

        } catch (Exception E) {

        }
        this.is_listening = false;
        this.is_has_partner = false;
        this.main_chat_panel = main_chat_panel;
    }

    public TcpServer() {
        this.connectedSocket = new CopyOnWriteArrayList<>();
        try {
            this.server = new ServerSocket();
            this.client = new Socket();

        } catch (Exception E) {

        }
        this.is_listening = false;
        this.is_has_partner = false;
    }

    public void startListeningOnTcpServer(String host, int port) throws IOException {
        if (this.is_listening == false) {
            InetSocketAddress endpoint = new InetSocketAddress(host, port);
            this.server = new ServerSocket();
            this.server.bind(endpoint);
            this.is_listening = true;
        }
    }

    public void stopListeningOnTcpServer() throws IOException {
        if (this.is_listening == true) {
            this.server.close();
            if (this.client != null) {
                this.client.close();
            }
            this.is_listening = false;
            this.is_has_partner = false;
        }
    }

    public synchronized void addSocket(Socket socket) {
        connectedSocket.add(socket);
    }

    public void waitingConnectionFromClient() throws IOException {
        this.client = this.server.accept();
        addSocket(this.client);
        ChatBus chat_bus = new ChatBus(this.client);
        this.main_chat_panel.addNewConnection(chat_bus);
        this.is_has_partner = true;


    }
    public Vector<String> getAllIpv4AddressesOnLocal() throws SocketException {
        Vector<String> ipv4_addresses = new Vector<>();
        Enumeration networks = NetworkInterface.getNetworkInterfaces();
        while (networks.hasMoreElements()) {
            NetworkInterface sub_networks = (NetworkInterface) networks.nextElement();
            Enumeration inet_addresses = sub_networks.getInetAddresses();
            while (inet_addresses.hasMoreElements()) {
                try {
                    Inet4Address ipv4 = (Inet4Address) inet_addresses.nextElement();
                    ipv4_addresses.add(ipv4.getHostAddress());
                } catch (Exception e) {
                    // TODO: pass ip version 6
                }
            }
        }
        return ipv4_addresses;
    }

    public boolean isListening() {
        return this.is_listening;
    }

    public boolean isHasPartner() {
        return this.is_has_partner;
    }

    public void setHasPartner(boolean b) {
        this.is_has_partner = b;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package test;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Desktop
 */
public class BaiTap15_RemoteServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: "+socket.getInetAddress().getHostAddress());
                
                // Tạo luồng thực thi
                Thread thread = new Thread(
                    ()->handleClientRequest(socket)
                );
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void handleClientRequest(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                System.out.println("\nMENU:");
                System.out.println("1. Shutdown");
                System.out.println("2. Restart");
                int choice = sc.nextInt();
                sc.nextLine();
                System.out.println("Bạn đã chọn: " + choice);
                switch (choice) {
                    case 1:
                        writer.println("shutdown");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;
                    case 2:
                        writer.println("restart");
                        writer.flush();
                        System.out.println(reader.readLine());
                        break;
                    default:
                        throw new AssertionError();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

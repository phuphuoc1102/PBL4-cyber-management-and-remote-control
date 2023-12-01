/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author Desktop
 */
public class BaiTap15_RemoteClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
             Socket socket = new Socket("localhost", 5000);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream());

            while(true){
                String request = reader.readLine();
                System.out.println(request);
                if(request.equals("shutdown")){
                    // Sử dụng Runtime
                    Runtime.getRuntime().exec("shutdown -s -t 1");
                    writer.println("Máy tính đang tắt ... ");
                    writer.flush();
                }else if(request.equals("restart")){
                    // Sử dụng Runtime
                    Runtime.getRuntime().exec("shutdown -r -t 1");
                    writer.println("Máy tính đang khởi động lại ... ");
                    writer.flush();
                }else if(request.equals("cancel")){
                    // Sử dụng Runtime
                    Runtime.getRuntime().exec("shutdown -a");
                    writer.println("Máy tính đang khởi động lại ... ");
                    writer.flush();
                }
            }
        } catch (Exception e) {
        }
       
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.repository;

import com.entity.Client;
import com.untity.DBUtility;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author boixi
 */
public class ClientRepository {

    private static ClientRepository instance;
    private static final int unitPrice=10000;

    public ClientRepository() {
    }

    public static ClientRepository getInstance() {
        if (instance == null) {
            instance = new ClientRepository();
        }
        return instance;
    }

    public static void setInstance(ClientRepository instance) {
        ClientRepository.instance = instance;
    }


    public List<Client> getListClient() {
        List<Client> list = new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * from client");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Client client = new Client();
                client.setUser_name(rs.getString("user_name"));
                client.setTime_remaining(Time.valueOf(rs.getString("time_remaining")));
                list.add(client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Client getClientByUserName(String user_name) {
        Connection con = DBUtility.openConnection();
        Client client = new Client();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM client WHERE user_name = ?");
            pstmt.setString(1, user_name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                client.setUser_name(rs.getString("user_name"));
                client.setTime_remaining(rs.getTime("time_remaining"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return client;
    }

    public Boolean create(String user_name,String password,Double top_up_value) {
        Connection con = DBUtility.openConnection();
        try {
            int totalMinutes=(int)(top_up_value*60)/unitPrice;
            int hours=totalMinutes/60;
            int minutes=totalMinutes%60;
            LocalTime localTime=LocalTime.of(hours,minutes);
            Time time=Time.valueOf(localTime);
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO client(user_name, password,time_remaining) VALUES (?,?,?)");
            pstmt.setString(1, user_name);
            pstmt.setString(2, password);
            pstmt.setTime(3, time);
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Boolean update(String user_name,Double top_up_value) {
        Connection con = DBUtility.openConnection();
        try {
            Client client=this.getClientByUserName(user_name);
            Time time=client.getTime_remaining();
            int totalMinutes=(int) ((top_up_value*60)/unitPrice) ;
            int hours=totalMinutes/60;
            int minutes=totalMinutes%60;
            LocalTime localTime=LocalTime.of(hours+ time.getHours(),minutes +time.getMinutes());

            PreparedStatement pstmt = con.prepareStatement("UPDATE client SET time_remaining=? WHERE user_name=?");
            pstmt.setTime(1, Time.valueOf(localTime));
            pstmt.setString(2, user_name);
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Boolean delete(String user_name) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("Delete from `client` where `user_name`=?");
            pstmt.setString(1, user_name);
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Client> findClient(String key) {
        List<Client> list = new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            String apkey = "%" + key + "%";
            PreparedStatement pstmt = con.prepareStatement("SELECT * from `client` WHERE  `user_name` like ?");
            pstmt.setString(1, apkey);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Client client = new Client();
                client.setUser_name(rs.getString("user_name"));
                client.setTime_remaining(rs.getTime("time_remaining"));
                list.add(client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean isLogin(Client client) throws SQLException, NoSuchAlgorithmException, ClassNotFoundException, Exception {
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rset = null;
        final String SQL = "select * from client where user_name=? and password=?";
        try {
            con = DBUtility.openConnection();
            psmt = con.prepareStatement(SQL);
            psmt.setString(1, client.getUser_name());
            psmt.setString(2, client.getPassword());
            rset = psmt.executeQuery();
            return rset.next();
        } finally {
            if (rset != null) {
                rset.close();
            }
            if (psmt != null) {
                psmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    public void updateTime(String user_name){
        Connection con = DBUtility.openConnection();
        Client client = new Client();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM client WHERE user_name = ?");
            pstmt.setString(1, user_name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                client.setUser_name(rs.getString("user_name"));
                client.setTime_remaining(rs.getTime("time_remaining"));
            }
            Time time=client.getTime_remaining();
            int totalMinutes=time.getHours()*60 + time.getMinutes() - 1;
            int hours=totalMinutes/60;
            int minutes=totalMinutes%60;
            LocalTime localTime=LocalTime.of(hours,minutes);
            PreparedStatement pstmtUpdate = con.prepareStatement("UPDATE client SET time_remaining=? WHERE user_name = ?");
            pstmtUpdate.setTime(1, Time.valueOf(localTime));
            pstmtUpdate.setString(2, user_name);
            pstmtUpdate.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

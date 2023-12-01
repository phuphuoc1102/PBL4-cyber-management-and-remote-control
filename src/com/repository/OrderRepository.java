/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.repository;

import com.entity.Client;
import com.entity.Order;
import com.entity.enums.OrderStatus;
import com.untity.DBUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.UUID;
import java.sql.Timestamp;

/**
 *
 * @author boixi
 */
public class OrderRepository {

    private static OrderRepository instance;

    public OrderRepository() {
    }

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public List<Order> getListOrderByStatus(OrderStatus orderStatus) {
        List<Order> list = new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            String order_sql = "SELECT * FROM ORDERS WHERE status=? order by order_time";
            PreparedStatement order_pstmt = con.prepareStatement(order_sql);
            order_pstmt.setString(1, orderStatus.toString());
            ResultSet order_rs = order_pstmt.executeQuery();

            while (order_rs.next()) {
                Order order=new Order();
                String client_sql="SELECT * FROM client WHERE user_name=?";
                PreparedStatement client_pstmt = con.prepareStatement(client_sql);
                client_pstmt.setString(1, order_rs.getString("user_name"));
                ResultSet client_rs=client_pstmt.executeQuery();
                while (client_rs.next()) {
                    Client client = new Client();
                    client.setUser_name(client_rs.getString("user_name"));
                    client.setTime_remaining(client_rs.getTime("time_remaining"));
                    order.setClient(client);
                }
                order.setId(UUID.fromString(order_rs.getString("id")));
                order.setTotal_price(order_rs.getDouble("total_price"));
                order.setOrder_time(order_rs.getTimestamp("order_time"));
                order.setCompleted_time(order_rs.getTimestamp("completed_time"));
                order.setCompleted_time(order_rs.getTimestamp("canceled_time"));
                order.setStatus(OrderStatus.valueOf(order_rs.getString("status")));
                list.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public Boolean create(UUID computer_id, String user_name, double total_price) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO `orders`(`order_id`,computer_id ,`user_name`, `order_time`, `total_price`, `status`) VALUES (?,?,?,?,?,?)");
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2,computer_id.toString());
            pstmt.setString(3, user_name);
            // Lấy giờ và ngày hiện tại
            Date now = new Date();

// Chuyển đổi từ java.util.Date sang java.sql.Timestamp
            Timestamp currentTimestamp = new Timestamp(now.getTime());
            pstmt.setTimestamp(4, currentTimestamp); // Sử dụng setTimestamp để chèn giờ và ngày vào câu truy vấn
            pstmt.setDouble(5, total_price);
            pstmt.setString(6, OrderStatus.PENDING.toString());
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public void update(UUID order_id,OrderStatus order_status) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("UPDATE orders SET status=? WHERE id = ?");
            pstmt.setString(1, order_status.toString());
            pstmt.setString(2, order_id.toString());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<Order> findOrderByUserName(String key) {
        List<Order> list = new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            String apkey = "%" + key + "%";
            String order_sql = "SELECT * FROM ORDERS WHERE user_name like ? order by order_time";
            PreparedStatement order_pstmt = con.prepareStatement(order_sql);
            order_pstmt.setString(1, apkey);
            ResultSet order_rs = order_pstmt.executeQuery();

            while (order_rs.next()) {
                Order order=new Order();
                String client_sql="SELECT * FROM client WHERE user_name=?";
                PreparedStatement client_pstmt = con.prepareStatement(client_sql);
                client_pstmt.setString(1, order_rs.getString("user_name"));
                ResultSet client_rs=client_pstmt.executeQuery();
                while (client_rs.next()) {
                    Client client = new Client();
                    client.setUser_name(client_rs.getString("user_name"));
                    client.setTime_remaining(client_rs.getTime("time_remaining"));
                    order.setClient(client);
                }
                order.setId(UUID.fromString(order_rs.getString("id")));
                order.setTotal_price(order_rs.getDouble("total_price"));
                order.setOrder_time(order_rs.getTimestamp("order_time"));
                order.setCompleted_time(order_rs.getTimestamp("completed_time"));
                order.setCompleted_time(order_rs.getTimestamp("canceled_time"));
                order.setStatus(OrderStatus.valueOf(order_rs.getString("status")));
                list.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}

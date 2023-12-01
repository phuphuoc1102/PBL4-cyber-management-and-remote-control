/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.repository;

import com.entity.*;
import com.entity.enums.MenuCategory;
import com.untity.DBUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author boixi
 */
public class OrderDetailRepository {
    private static OrderDetailRepository instance;

    public OrderDetailRepository() {
    }

    public static OrderDetailRepository getInstance() {
        if (instance == null) {
            instance = new OrderDetailRepository();
        }
        return instance;
    }

    public List<OrderDetail> getOrderDetailByOrderId(Order order){
        List<OrderDetail> list = new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            String orderDetail_sql = "SELECT * FROM order_detail WHERE order_id=?";
            PreparedStatement order_pstmt = con.prepareStatement(orderDetail_sql);
            order_pstmt.setString(1, order.getId().toString());
            ResultSet orderDetail_rs = order_pstmt.executeQuery();

            while (orderDetail_rs.next()) {
                OrderDetail order_detail=new OrderDetail();
                String menu_sql="SELECT * FROM menu WHERE id=?";
                PreparedStatement menu_pstmt = con.prepareStatement(menu_sql);
                menu_pstmt.setString(1, orderDetail_rs.getString("menu_id"));
                ResultSet menu_rs=menu_pstmt.executeQuery();
                while (menu_rs.next()) {
                    Menu menu = new Menu();
                    menu.setId(UUID.fromString(menu_rs.getString("id")));
                    menu.setName(menu_rs.getString("name"));
                    menu.setCategory(MenuCategory.valueOf(menu_rs.getString("category")));
                    menu.setPrice(menu_rs.getDouble("price"));
                    order_detail.setMenu(menu);
                }
                order_detail.setOrder(order);
                order_detail.setQuantity(orderDetail_rs.getInt("quantity"));
                list.add(order_detail);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public Boolean create(Order order,UUID menu_id,int quantity) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO order_detail(order_id,menu_id, quantity) VALUES (?, ?, ?)");
            pstmt.setString(1, order.getId().toString());
            pstmt.setString(2, menu_id.toString());
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

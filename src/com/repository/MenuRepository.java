/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.repository;

import com.entity.Menu;
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
 * @author boixi
 */
public class MenuRepository {
    private static MenuRepository instance;

    public MenuRepository() {
    }

    public static MenuRepository getInstance() {
        if (instance == null) {
            instance = new MenuRepository();
        }
        return instance;
    }

    public List<Menu> getListMenu() {
        List<Menu> list = new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM menu");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(UUID.fromString(rs.getString("id")));
                menu.setName(rs.getString("name"));
                menu.setPrice(rs.getDouble("price"));
                menu.setCategory(MenuCategory.valueOf(rs.getString("category")));
                list.add(menu);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public boolean create(String name, String price, String category) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO `menu`(`id`,`name`, `price`, `category`) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, name);
            pstmt.setString(3, price);
            pstmt.setString(4, category);
            int i=pstmt.executeUpdate();
            if (i>0) return true;

        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Menu> getListMenuByCategory(MenuCategory category) {
        List<Menu> list=new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM menu where category=?");
            pstmt.setString(1, category.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(UUID.fromString(rs.getString("id")));
                menu.setName(rs.getString("name"));
                menu.setCategory(category);
                menu.setPrice(rs.getDouble("price"));
                list.add(menu);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
    public boolean update(UUID id,String name,String price,String category){
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("UPDATE menu SET name=?,price=?,category=? WHERE id=?");
            pstmt.setString(1, name);
            pstmt.setString(2, price);
            pstmt.setString(3, category);
            pstmt.setString(4, id.toString());
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public boolean delete(UUID id){
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM menu WHERE id=?");
            pstmt.setString(1, id.toString());
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Menu> getMenuByName(String name) {
        List<Menu> list=new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            String apKey="%"+name +"%";
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM menu where name like ?");
            pstmt.setString(1,apKey);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setId(UUID.fromString(rs.getString("id")));
                menu.setName(rs.getString("name"));
                menu.setCategory(MenuCategory.valueOf(rs.getString("category")));
                menu.setPrice(rs.getDouble("price"));
                list.add(menu);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(MenuRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
}

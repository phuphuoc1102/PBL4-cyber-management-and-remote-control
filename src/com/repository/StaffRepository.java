/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.repository;

import com.entity.Staff;
import com.untity.DBUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRI
 */
public class StaffRepository {

    private static StaffRepository instance;
    private Staff staff=new Staff();
    public StaffRepository() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public static StaffRepository getInstance() {
        if (instance == null) {
            instance = new StaffRepository();
        }
        return instance;
    }

    public static void setInstance(StaffRepository instance) {
        StaffRepository.instance = instance;
    }

    public Boolean login(String username, String password) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM `staff` WHERE username = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            Staff staff = new Staff();
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                staff.setId(UUID.fromString(rs.getString("id")));
                staff.setUsername(rs.getString("username"));
                staff.setPassword(rs.getString("password"));
                staff.setName(rs.getNString("name"));
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    public List<Staff> listAccount() {
        List<Staff> list = new ArrayList<>();
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * from `nhan_vien`");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Staff account = new Staff();
                account.setId(UUID.fromString(rs.getString("id_nhan_vien")));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setName(rs.getNString("ten_nhan_vien"));
                account.setDate(rs.getDate("date_of_birth"));
                account.setAddress(rs.getNString("address"));
                account.setPhoneNumber(rs.getString("phone_number"));
                account.setEmail(rs.getString("email"));

                list.add(account);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Boolean create(String name, String username, String pass, String date, String address, String phoneNumber, String position, String id) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO `nhan_vien`(`username`, `password`, `ten_nhan_vien`, `ngay_sinh`, `so_dien_thoai`, `dia_chi`, `chuc_vu`, `email`, `id_nhan_vien`) VALUES (?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2, pass);
            pstmt.setNString(3, name);
            pstmt.setString(4, date);
            pstmt.setNString(5, phoneNumber);
            pstmt.setString(6, address);
            pstmt.setNString(7, position);
            pstmt.setString(8, id);

            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Boolean update(String pass, String name, String date, String address, String phoneNumber, UUID id) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("UPDATE `staff` SET `password`=?,`name`=?, `date_of_birth`=?, `phone_number`=?,`address`=? WHERE `id`=?");
            pstmt.setString(1, pass);
            pstmt.setString(2, name);
            pstmt.setString(3, date);
            pstmt.setString(4, phoneNumber);
            pstmt.setString(5, address);
            pstmt.setString(6, id.toString());

            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Boolean delete(UUID id) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("Delete from `nhan_vien` where `id_nhan_vien`=?");
            pstmt.setString(1, id.toString());
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Boolean resetPassword(UUID id, String pass) {
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("UPDATE `nhan_vien` SET `password`=? WHERE `id`=?");
            pstmt.setString(1, pass);
            pstmt.setString(2, id.toString());
            int i = pstmt.executeUpdate();
            if (i > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String getNameById(UUID id) throws SQLException {
        Connection con = null;
        PreparedStatement psmt = null;
        ResultSet rs=null;
        final String SQL = "select * from nhan_vien where id =?";
        try {
            con = DBUtility.openConnection();
            psmt = con.prepareStatement(SQL);
            psmt.setString(1, id.toString());
            rs = psmt.executeQuery();
            List<Staff> list = new ArrayList<>();

            while (rs.next()) {
                Staff account = new Staff();
                account.setId(UUID.fromString(rs.getString("id")));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                account.setName(rs.getNString("name"));
                account.setDate(rs.getDate("date_of_birth"));
                account.setAddress(rs.getNString("address"));
                account.setPhoneNumber(rs.getString("phone_number"));
                account.setEmail(rs.getString("email"));


                list.add(account);
            }
            return list.get(0).getName();

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (psmt != null) {
                psmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}

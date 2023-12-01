/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.repository;

import com.entity.Computer;
import com.entity.enums.ComputerStatus;
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
public class ComputerRepository {
    private static ComputerRepository instance;

    public ComputerRepository() {
    }

    public static ComputerRepository getInstance() {
        if (instance == null) {
            instance = new ComputerRepository();
        }
        return instance;
    }

    public Computer getComputerById(String id) {
        Connection con = DBUtility.openConnection();
        Computer computer = new Computer();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM computer WHERE id = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                computer.setId(UUID.fromString(rs.getString("id")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComputerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Computer> getListComputer() {
        List<Computer> listTable = new ArrayList<>();
        
        Connection con = DBUtility.openConnection();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM computer");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Computer computer = new Computer();
                computer.setId(UUID.fromString(rs.getString("id")));
                computer.setPrice(rs.getInt("so_cho"));
                computer.setStatus(ComputerStatus.valueOf(rs.getString("status")));
                listTable.add(computer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComputerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listTable;
    }
}

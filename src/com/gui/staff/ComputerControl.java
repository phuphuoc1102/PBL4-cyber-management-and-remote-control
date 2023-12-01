/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.gui.staff;

import com.entity.Client;
import com.formdev.flatlaf.FlatLightLaf;
import com.repository.ClientRepository;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class ComputerControl extends javax.swing.JFrame {

    private ServerSocket requestServer;
    private Socket requestSocket;
    private com.entity.Computer computer;
    private DefaultTableModel tableModel;
    private Object[][][] data;

    public ComputerControl(com.entity.Computer computer, ServerSocket server, Socket soc, Object[][] data) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ManageComputer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.data = new Object[4][][];
        this.computer = computer;
        this.requestServer = server;
        this.requestSocket = soc;
        this.data[0] = data;
        initComponents();
        tableModel = new DefaultTableModel();
        tableModel.setRowCount(0);
        tableModel.addColumn("ID");
        tableModel.addColumn("URL");
        tableModel.addColumn("Visit Count");
        jTable1.setModel(tableModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        for (Object[] data1 : data) {
            if (data1[0] != null) {
                tableModel.addRow(data1);
            }
        }
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(300);
        }
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        initBrowserHistory();
        initBlockList();
        jButtonBlockAccess.setEnabled(false);
    }

    private void initBrowserHistory() {
        DataOutputStream dos;
        try {
            String browserName = "coc_coc";
            dos = new DataOutputStream(requestSocket.getOutputStream());
            dos.writeUTF("view_history_" + browserName);
            this.data[1] = receiveBrowserHistory();
            dos.writeUTF("view_history_chrome");
            this.data[2] = receiveBrowserHistory();
        } catch (IOException ex) {
            Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initBlockList() {
        String request = "view_blocked_url_list";
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(requestSocket.getOutputStream());
            dos.writeUTF(request);
        } catch (IOException ex) {
            Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.data[3] = receiveBlockedList();
    }

    private Object[][] receiveBrowserHistory() {
        DataInputStream dis = null;
        try {

            Object[][] data = new Object[10000][3];
            dis = new DataInputStream(requestSocket.getInputStream());
            int i = 0;
            while (true) {
                String url_read = null;
                try {
                    url_read = dis.readUTF();
                } catch (EOFException e) {
                    System.err.println("eof");
                } catch (IOException ex) {
                    Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (url_read.equals("")) {
                    break;
                }
                String[] values = url_read.split(",,");
                int id = Integer.parseInt(values[0]);
                String url = values[1];
                int visitCount = Integer.parseInt(values[2]);
                data[i][0] = id;
                data[i][1] = url;
                data[i][2] = visitCount;
                i++;
            }
            return data;
        } catch (IOException ex) {
            Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonViewBlockedList = new javax.swing.JButton();
        jTextFieldURL = new javax.swing.JTextField();
        jComboBoxChoose = new javax.swing.JComboBox<>();
        jButtonBlockAccess = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        unBlockButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jButtonViewBlockedList.setText("View Blocked URL List");
        jButtonViewBlockedList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonViewBlockedListMouseClicked(evt);
            }
        });
        jButtonViewBlockedList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewBlockedListActionPerformed(evt);
            }
        });

        jTextFieldURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldURLActionPerformed(evt);
            }
        });
        jTextFieldURL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldURLKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldURLKeyReleased(evt);
            }
        });

        jComboBoxChoose.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Edge", "Chrome", "Cốc Cốc" }));
        jComboBoxChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxChooseActionPerformed(evt);
            }
        });

        jButtonBlockAccess.setText("Block Access");
        jButtonBlockAccess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBlockAccessActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Select browser to view history:");

        unBlockButton.setText("Unblock");
        unBlockButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                unBlockButtonMouseClicked(evt);
            }
        });
        unBlockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unBlockButtonActionPerformed(evt);
            }
        });
        unBlockButton.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldURL, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonBlockAccess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxChoose, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 394, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unBlockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonViewBlockedList, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(121, 121, 121))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxChoose, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonViewBlockedList, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldURL, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBlockAccess, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unBlockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonViewBlockedListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonViewBlockedListMouseClicked


    }//GEN-LAST:event_jButtonViewBlockedListMouseClicked

    private void jButtonViewBlockedListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewBlockedListActionPerformed

        tableModel = new DefaultTableModel();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        tableModel.addColumn("ID");
        tableModel.addColumn("Blocked URL");
        jTable1.setModel(tableModel);
        for (Object[] data : data[3]) {
            if (data[0] != null) {
                tableModel.addRow(data);
            }
        }
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(300);
        }
        jComboBoxChoose.setSelectedItem(null);
        // Lắng nghe sự kiện chọn dòng trong bảng
        ListSelectionModel selectionModel = jTable1.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Kiểm tra xem có phải là sự kiện chọn dòng kết thúc không
                if (!e.getValueIsAdjusting()) {
                    // Kiểm tra xem có ít nhất một dòng được chọn không va kiem tra xem bang nay co phai la bang view blocklist hay ko
                    if (jTable1.getSelectedRow() != -1 && jTable1.getColumnCount() == 2) {
                        // Nếu có dòng được chọn, enable nút
                        unBlockButton.setEnabled(true);
                    } else {
                        // Nếu không có dòng nào được chọn, disable nút
                        unBlockButton.setEnabled(false);
                    }
                }
            }
        });
    }//GEN-LAST:event_jButtonViewBlockedListActionPerformed

    private void jTextFieldURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldURLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldURLActionPerformed

    private void jButtonBlockAccessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBlockAccessActionPerformed
        // TODO add your handling code here:
        String blockURL = jTextFieldURL.getText();
        String request = "block_access";
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(requestSocket.getOutputStream());
            dos.writeUTF(request + "_" + blockURL);
            System.err.println(request + "_" + blockURL);

        } catch (IOException ex) {
            Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTextFieldURL.setText("");
        data[3] = receiveBlockedList();
        tableModel = new DefaultTableModel();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        tableModel.addColumn("ID");
        tableModel.addColumn("Blocked URL");
        jTable1.setModel(tableModel);
        for (Object[] dt : data[3]) {
            if (data[3][0] != null) {
                tableModel.addRow(dt);
            }
        }
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(300);
        }
        jComboBoxChoose.setSelectedItem(null);

    }//GEN-LAST:event_jButtonBlockAccessActionPerformed

    private void jComboBoxChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxChooseActionPerformed
        // TODO add your handling code here:
        if (jComboBoxChoose.getSelectedItem() != null) {
            tableModel = new DefaultTableModel();
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);
            tableModel.addColumn("ID");
            tableModel.addColumn("URL");
            tableModel.addColumn("Visit Count");
            tableModel.setRowCount(0);
            jTable1.setModel(tableModel);
            String browserName = jComboBoxChoose.getSelectedItem().toString();
            browserName = (browserName.equals("Cốc Cốc") ? "coc_coc" : (browserName.equals("Edge") ? "edge" : "chrome"));
            if (browserName.equals("edge")) {
                for (Object[] data1 : data[0]) {
                    if (data1[0] != null) {
                        tableModel.addRow(data1);
                    }
                }
            } else if (browserName.equals("coc_coc")) {
                for (Object[] data1 : data[1]) {
                    if (data1[0] != null) {
                        tableModel.addRow(data1);
                    }
                }
            } else if (browserName.equals("chrome")) {
                for (Object[] data1 : data[2]) {
                    if (data1[0] != null) {
                        tableModel.addRow(data1);
                    }
                }
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            jTable1.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            jTable1.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            jScrollPane1.setViewportView(jTable1);
            if (jTable1.getColumnModel().getColumnCount() > 0) {
                jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
                jTable1.getColumnModel().getColumn(2).setMaxWidth(300);
            }
        }
    }//GEN-LAST:event_jComboBoxChooseActionPerformed

    private void unBlockButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unBlockButtonMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_unBlockButtonMouseClicked

    private void unBlockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unBlockButtonActionPerformed
        // TODO add your handling code here:
        DataOutputStream dos;
        try {
            dos = new DataOutputStream(requestSocket.getOutputStream());
            dos.writeUTF("unblock_" + jTable1.getValueAt(jTable1.getSelectedRow(), 1));
        } catch (IOException ex) {
            Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        data[3] = receiveBlockedList();
        tableModel = new DefaultTableModel();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        tableModel.addColumn("ID");
        tableModel.addColumn("Blocked URL");
        jTable1.setModel(tableModel);
        for (Object[] dt : data[3]) {
            if (data[3][0] != null) {
                tableModel.addRow(dt);
            }
        }
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(300);
        }
        jComboBoxChoose.setSelectedItem(null);

    }//GEN-LAST:event_unBlockButtonActionPerformed

    private void jTextFieldURLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldURLKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldURLKeyPressed

    private void jTextFieldURLKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldURLKeyReleased
        // TODO add your handling code here:
        jButtonBlockAccess.setEnabled(true);
    }//GEN-LAST:event_jTextFieldURLKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                com.entity.Computer c = new com.entity.Computer();
                ServerSocket server = null;
                Socket s = null;
                Object[][] data = null;
                new ComputerControl(c, server, s, data).setVisible(true);
            }
        });
    }

    private Object[][] receiveBlockedList() {
        DataInputStream dis = null;
        try {
            Object[][] blockedURL = new Object[1000][2];
            dis = new DataInputStream(requestSocket.getInputStream());
            int i = 0;
            while (true) {
                String url_read = null;
                try {
                    url_read = dis.readUTF();
                    if (url_read.equals("Blocked list is blank")) {
                        System.out.println("blank");
                        break;
                    }
                } catch (EOFException e) {
                    System.err.println("eof");
                } catch (IOException ex) {
                    Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (url_read.equals("")) {
                    break;
                }
                blockedURL[i][0] = i + 1;
                blockedURL[i][1] = url_read.substring(10);
                i++;
            }
            return blockedURL;
        } catch (IOException ex) {
            Logger.getLogger(ComputerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBlockAccess;
    private javax.swing.JButton jButtonViewBlockedList;
    private javax.swing.JComboBox<String> jComboBoxChoose;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldURL;
    private javax.swing.JButton unBlockButton;
    // End of variables declaration//GEN-END:variables

}

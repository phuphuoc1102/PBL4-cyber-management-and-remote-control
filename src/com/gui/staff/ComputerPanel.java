/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gui.staff;

/**
 *
 * @author ADMIN
 */
import javax.swing.*;
import java.awt.*;

public class ComputerPanel extends JFrame {

    public ComputerPanel() {
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 55, 60));

        for (int i = 0; i < 3; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 55, 0));

            for (int j = 0; j < 4; j++) {
                JPanel computerPanel = new JPanel();
                computerPanel.setPreferredSize(new Dimension(175, 225));
                computerPanel.setBackground(new Color(204, 204, 255));
                computerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                JButton computerButton = new JButton(new ImageIcon(getClass().getResource("/images/icons8-computer-64.png")));
                JLabel computerLabel = new JLabel("Máy tính " + (i * 4 + j + 1));
                JButton shutdownButton = new JButton("Shutdown");
                JButton restartButton = new JButton("Restart");

                computerPanel.add(computerButton);
                computerPanel.add(computerLabel);
                computerPanel.add(shutdownButton);
                computerPanel.add(restartButton);

                rowPanel.add(computerPanel);
            }

            mainPanel.add(rowPanel);
        }

        add(mainPanel);
        setTitle("Computer Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ComputerPanel computerPanel = new ComputerPanel();
            computerPanel.setVisible(true);
        });
    }
}


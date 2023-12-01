/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author ADMIN
 */
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TableSelectionExample {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Table Selection Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tạo bảng
        String[] columnNames = {"Name", "Age", "City"};
        Object[][] data = {
            {"John", 25, "New York"},
            {"Alice", 30, "Los Angeles"},
            {"Bob", 22, "Chicago"}
        };

        JTable table = new JTable(data, columnNames);

        // Tạo JScrollPane để hiển thị bảng
        JScrollPane scrollPane = new JScrollPane(table);

        // Tạo JButton với thuộc tính enable là false ban đầu
        JButton button = new JButton("Click me");
        button.setEnabled(false);

        // Lắng nghe sự kiện chọn dòng trong bảng
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Kiểm tra xem có phải là sự kiện chọn dòng kết thúc không
                if (!e.getValueIsAdjusting()) {
                    // Kiểm tra xem có ít nhất một dòng được chọn không
                    if (table.getSelectedRow() != -1) {
                        // Nếu có dòng được chọn, enable nút
                        button.setEnabled(true);
                    } else {
                        // Nếu không có dòng nào được chọn, disable nút
                        button.setEnabled(false);
                    }
                }
            }
        });

        // Lắng nghe sự kiện click trên nút
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Button Clicked!");
            }
        });

        // Thêm các thành phần vào frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(button, BorderLayout.SOUTH);

        // Hiển thị frame
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

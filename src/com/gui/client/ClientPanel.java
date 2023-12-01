package com.gui.client;

import com.bus.common.CommonBus;
import com.gui.staff.MainFrame;
import com.gui.common.CommonLabel;
import com.gui.common.CommonPanel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.entity.Menu;
import com.repository.MenuRepository;
import com.repository.OrderRepository;

import java.util.List;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import com.entity.Client;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

public class ClientPanel extends JPanel {

    public final static String BACKGROUND = "0xD3D3D3";
    public final static String FOREGROUND = "0x003927";
    private Menu menu = new Menu();
    private CommonPanel main_panel;
    private CommonLabel connect_label;
    private ButtonGroup button_group;
    private JRadioButton low_radio;
    private JRadioButton high_radio;
    private JLabel loader_label;
    private double total_price = 0;
    private int quantity = 0;
    private String dishName = "";
    private double dishPrice = 0;
    private CommonBus common_bus;
    private MenuRepository menuRepository = new MenuRepository();

    public ClientPanel(CommonBus common_bus, Client client) {
        // TODO: style ClientPanel
        this.setLocation(0, MainFrame.HEIGHT_TASKBAR);
        this.setSize(MainFrame.WIDTH_FRAME, MainFrame.HEIGHT_FRAME - MainFrame.HEIGHT_TASKBAR);
        this.setBackground(Color.decode(ClientPanel.BACKGROUND));
        this.setLayout(null);

        // TODO: class for handle events
        this.common_bus = common_bus;

        // TODO: add components
        this.initComponents(client);
    }

    private void initComponents(Client client) {
        // TODO: constructor
        this.main_panel = new CommonPanel();
        this.connect_label = new CommonLabel();
        this.button_group = new ButtonGroup();
        this.low_radio = new JRadioButton();
        this.high_radio = new JRadioButton();
        this.loader_label = new JLabel();

        // TODO: style main panel
        this.main_panel.setBorder(BorderFactory.createTitledBorder(null, "Hello " + client.getUser_name(),
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("segoe ui", Font.BOLD, 16),
                Color.decode(ClientPanel.FOREGROUND))
        );
        this.add(this.main_panel);

        // TODO: style host_label
        this.main_panel.getHostLabel().setText("Select menu:");
        main_panel.getHostCombo().removeAllItems();
        List<Menu> menus = menuRepository.getListMenu();
        for (Menu item : menus) {
            main_panel.getHostCombo().addItem(item.toString());
        }
        this.main_panel.getHostCombo().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        // TODO: style host_text
        this.main_panel.getHostCombo().setVisible(true);
        this.main_panel.getHostText().setVisible(true);

        // TODO: style port_label
        this.main_panel.getPortLabel().setText("Quantity:");
        this.main_panel.getPortText().setText("");

        // TODO: style pass_label
        this.main_panel.getPassLabel().setText("Price: ");
        // TODO: style pass_field
        this.main_panel.getPassText().setVisible(true);
        this.main_panel.getPassText().setText("");
        this.main_panel.getPassText().setEditable(false);
        this.main_panel.getPassField().setVisible(false);

        // TODO: style help_label
        this.main_panel.getHelpLabel().setText("");
        main_panel.getPortText().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTotalPrice();
            }

        });
        main_panel.getHostCombo().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Thực hiện các hành động khi một mục được chọn
                    updateTotalPrice();
                }
            }
        });
        // TODO: style connect_label
        this.connect_label.setIcon(new ImageIcon(this.getClass().getResource("/images/connect_icon.png")));
        this.connect_label.setText("Order now ");
        this.connect_label.setBounds(220, 290, 150, 50);
        this.connect_label.setForeground(Color.decode(ClientPanel.FOREGROUND));
        this.connect_label.setFont(new Font("segoe ui", Font.PLAIN, 15));
        this.connect_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dishName = main_panel.getHostCombo().getSelectedItem().toString();
                dishPrice = menu.getPrice();
                if (!main_panel.getPortText().getText().equals("")) {
                    quantity = Integer.parseInt(main_panel.getPortText().getText() + "");
                }
                double orderPrice = dishPrice * quantity;
                main_panel.getPassText().setText(orderPrice + "");
                updateTotalPrice();
                OrderRepository orderRepository = new OrderRepository();
                String user_name = client.getUser_name();
                boolean insert = orderRepository.create(UUID.randomUUID(), user_name, total_price);
            }
        });

        this.add(this.connect_label);

        // TODO: style low_radio
        this.low_radio.setText("Low quality");
        this.low_radio.setBounds(60, 290, 100, 30);
        this.low_radio.setOpaque(false);
        this.low_radio.setSelected(true);
//        this.button_group.add(this.low_radio);
//        this.add(this.low_radio);

        // TODO: style high_radio
        this.high_radio.setText("High quality");
        this.high_radio.setBounds(60, 310, 100, 30);
        this.high_radio.setOpaque(false);
//        this.button_group.add(this.high_radio);
//        this.add(this.high_radio);

        // TODO: style loader_label
        this.loader_label.setIcon(new ImageIcon(this.getClass().getResource("/images/loader_icon.gif")));
        this.loader_label.setBounds(340, 307, 16, 16);
        this.loader_label.setVisible(false);
        this.add(this.loader_label);
        this.main_panel.setVisible(true);
    }
//    public double getDishBy(String dishName){
//        MenuRepository menuRepository = new MenuRepository();
//        return menuRepository.getOrderPrice(dishName);
//    }

    @Override
    public void setEnabled(boolean b) {
        this.main_panel.setEnabled(b);
        this.low_radio.setEnabled(b);
        this.high_radio.setEnabled(b);
        this.connect_label.setEnabled(b);
    }

    private boolean isFormatIpv4(String host) {
        int count = 0;
        for (int i = 0; i < host.length(); ++i) {
            if (host.charAt(i) == '.') {
                ++count;
            }
        }
        // TODO: count = 3 for ipv4
        // TODO: count = 0 for host name
        return count == 3 || count == 0;
    }

    public void updateTotalPrice() {
        try {
            dishName = main_panel.getHostCombo().getSelectedItem().toString();
            //   menu = menuRepository.getMenuByDishName(dishName);
            dishPrice = menu.getPrice();
            String quantityText = main_panel.getPortText().getText();
            if (!quantityText.isEmpty()) {
                quantity = Integer.parseInt(quantityText);
            } else {
                quantity = 0;
            }
            total_price = dishPrice * quantity;
            main_panel.getPassText().setText(String.valueOf(total_price));
        } catch (NumberFormatException ex) {
            // Xử lý ngoại lệ nếu người dùng nhập không phải là số
            main_panel.getPassText().setText("Invalid Quantity");
        }
    }

    // TODO: handle events of connect_label
    public void connectToServer() {
        Thread connect_thread = new Thread(() -> {
            try {
                String host = config("defaultServerIP");
                int port = 9999;
                // TODO: check format ipv4
                if (!this.isFormatIpv4(host)) {
                    throw new Exception("Wrong format IPV4");
                }
                // TODO: start connect
                this.common_bus.startConnectingToServer(host, port);

                // TODO: show remote screen
                EventQueue.invokeLater(() -> {
                    try {
                        if (this.low_radio.isSelected()) {
//                                new RemoteFrame(this, this.common_bus, "jpg");
                        } else if (this.high_radio.isSelected()) {
//                                new RemoteFrame(this, this.common_bus, "png");
                        }
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(this, "Can't start remoting to server:\n" + exception.getMessage());
                    }
                });
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Can't connect to server:\n" + exception.getMessage());
            }
            this.setEnabled(true);
            this.loader_label.setVisible(false);
        });
        connect_thread.setDaemon(true);
        connect_thread.start();
    }

    public String config(String key) {

        ResourceBundle config = ResourceBundle.getBundle("config", Locale.getDefault());
        String value;
        try {
            value = config.getString(key);
        } catch (Exception e) {
            value = "";
        }

        return value;
    }

}

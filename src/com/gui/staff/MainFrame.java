package com.gui.staff;

import com.bus.common.CommonBus;
import com.gui.chat.MainChatPanel;
import com.gui.client.ClientPanel;
import com.gui.common.CommonLabel;
import com.gui.server.ServerPanel;

import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

public class MainFrame extends JFrame {

    public final static int WIDTH_FRAME = 400;
    public final static int HEIGHT_FRAME = 420;
    public final static int HEIGHT_TASKBAR = 50;
    public final static String TASKBAR_BACKGROUND = "0x000942";

    // TODO: class for handle events
    private CommonBus common_bus;
    private ServerPanel server_panel;
    private MainChatPanel main_chat_panel;
    private StaffLogin staff_login;
    private Login login;
    private int focus_key;

    public MainFrame() throws IOException {
        this.setIconImage(new ImageIcon(this.getClass().getResource("/images/window_icon.png")).getImage());
        this.initComponents();
    }

    private void initComponents() {
        // TODO: constructor
        this.staff_login = new StaffLogin();
        this.staff_login.setVisible(true);
        this.common_bus = new CommonBus();
        this.server_panel = new ServerPanel(this.common_bus);
        this.common_bus.setMainChatPanel(this.main_chat_panel);
//        this.server_panel.connect();
        try {
            this.add(this.staff_login);
        } catch (IllegalArgumentException e) {
        }
    }
}

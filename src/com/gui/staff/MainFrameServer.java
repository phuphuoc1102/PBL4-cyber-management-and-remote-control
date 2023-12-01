package com.gui.staff;

import com.bus.common.CommonBus;
import com.formdev.flatlaf.FlatLightLaf;
import com.gui.chat.MainChatPanel;
import com.gui.client.ClientPanel;
import com.gui.common.CommonLabel;
import com.gui.server.ServerPanel;
import com.gui.staff.Login;
import com.gui.staff.StaffLogin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrameServer extends JFrame {

    public final static int WIDTH_FRAME = 400;
    public final static int HEIGHT_FRAME = 420;
    public final static int HEIGHT_TASKBAR = 50;
    public final static String TASKBAR_BACKGROUND = "0x000942";

    // TODO: class for handle events
    private CommonBus common_bus;

    private JPanel taskbar_panel;

    private CommonLabel server_label;
    private CommonLabel chat_label;
    private ServerPanel server_panel;
    private MainChatPanel main_chat_panel;

    private int focus_key;

    public CommonBus getCommonBus() {
        return this.common_bus;
    }

    public MainFrameServer() throws IOException {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ManageComputer.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageIO.setUseCache(false);
        // TODO: set UI
        UIManager.put("Label.disabledForeground", Color.decode("0xD3D3D3"));
        UIManager.put("RadioButton.disabledText", Color.decode("0xD3D3D3"));

        // TODO: style main frame
        this.getContentPane().setPreferredSize(new Dimension(MainFrameServer.WIDTH_FRAME, MainFrameServer.HEIGHT_FRAME));
        this.pack();
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Remote Desktop Software");
        this.setIconImage(new ImageIcon(this.getClass().getResource("/images/window_icon.png")).getImage());
        this.setVisible(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    mainFrameWindowClosing(e);
                } catch (Exception exception) {
                }
            }
        });

        // TODO: add components
        this.initComponents();

    }

    private void initComponents() {
        this.common_bus = new CommonBus();
        this.taskbar_panel = new JPanel();
        this.server_label = new CommonLabel();
        this.chat_label = new CommonLabel();
        this.server_panel = new ServerPanel(this.common_bus);
        this.main_chat_panel = new MainChatPanel(this.common_bus);
        this.common_bus.setMainChatPanel(this.main_chat_panel);
        this.server_panel.connect();

        // TODO: set focus_key = 1 for client_panel
        this.focus_key = 1;

        // TODO: layout of taskbar_panel
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{100};

        // TODO: style taskbar_panel
        this.taskbar_panel.setLayout(gridBagLayout);
        this.taskbar_panel.setBackground(Color.decode(MainFrameServer.TASKBAR_BACKGROUND));
        this.taskbar_panel.setBounds(0, 0, MainFrameServer.WIDTH_FRAME, MainFrameServer.HEIGHT_TASKBAR);
        this.add(this.taskbar_panel);

 
        // TODO: style chat_label
        this.chat_label.setText("Chat");
        this.chat_label.setBigFont();
        this.chat_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabLabelMouseClicked(e, chat_label, 2);
            }
        });
        this.taskbar_panel.add(this.chat_label);

        // TODO: default
        this.server_panel.setVisible(false);
        this.main_chat_panel.setVisible(true);
//        this.server_panel.
//        this.add(this.server_panel);
        this.add(this.main_chat_panel);

    }

    private void mainFrameWindowClosing(WindowEvent e) throws IOException, NotBoundException {
        this.dispose();
    }

    private void tabLabelMouseClicked(MouseEvent e, CommonLabel common_label, int key) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (key == focus_key) {
                return;
            }
            JPanel show_panel = (key == 1) ? this.server_panel : this.main_chat_panel;
            JPanel hide_panel = (focus_key == 1) ? this.server_panel : this.main_chat_panel;
            if (key > focus_key) {
                this.showPanelsSlider(show_panel, hide_panel, true);
            } else {
                this.showPanelsSlider(show_panel, hide_panel, false);
            }

            // TODO: update status
            this.focus_key = key;
            this.chat_label.setSmallFont();
        }
    }

    private void showPanelsSlider(JPanel show_panel, JPanel hide_panel, boolean isLeft) {
        show_panel.setVisible(true);

        // TODO: atomic integer for lambda expression
        AtomicInteger x_hide_location = new AtomicInteger(0);
        AtomicInteger x_show_location = new AtomicInteger(0);
        AtomicInteger value = new AtomicInteger(0);

        if (isLeft) {
            x_show_location.set(MainFrameServer.WIDTH_FRAME);
            value.set(-50);
        } else {
            x_show_location.set(-MainFrameServer.WIDTH_FRAME);
            value.set(50);
        }

        Timer timer = new Timer(10, (e) -> {
            hide_panel.setLocation(x_hide_location.get(), MainFrameServer.HEIGHT_TASKBAR);
            show_panel.setLocation(x_show_location.get(), MainFrameServer.HEIGHT_TASKBAR);
            if (x_show_location.get() == 0) {
                ((Timer) e.getSource()).stop();
                hide_panel.setVisible(false);
            }
            x_show_location.addAndGet(value.get());
            x_hide_location.addAndGet(value.get());
        });
        timer.start();
    }
}

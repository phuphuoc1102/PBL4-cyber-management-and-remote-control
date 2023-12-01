/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gui.client;

/**
 *
 * @author ADMIN
 */
import com.bus.common.CommonBus;
import com.gui.chat.MainChatPanel;
import com.gui.client.ClientPanel;
import com.gui.common.CommonLabel;
import com.gui.server.ServerPanel;
import com.gui.staff.Login;
import com.gui.staff.StaffLogin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.entity.Client;
import com.gui.staff.ManageClient;
import com.repository.ClientRepository;
import com.untity.ShareSocketHolder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Time;
import java.util.stream.Collectors;

public class MainFrameClient extends JFrame {

    public final static int WIDTH_FRAME = 400;
    public final static int HEIGHT_FRAME = 420;
    public final static int HEIGHT_TASKBAR = 50;
    public final static String TASKBAR_BACKGROUND = "0x000942";

    // TODO: class for handle events
    public CommonBus common_bus;

    private JPanel taskbar_panel;
    private CommonLabel client_label;
    private CommonLabel chat_label;
    private ClientPanel client_panel;
    private MainChatPanel main_chat_panel;
    private StaffLogin staffLogin;
    private Login login;
    private int focus_key;
    private Socket requestSocket;
    private static Client client;

    public MainFrameClient(Client client) throws IOException {
        ImageIO.setUseCache(false);
        MainFrameClient.client = client;
        // TODO: set UI
        UIManager.put("Label.disabledForeground", Color.decode("0xD3D3D3"));
        UIManager.put("RadioButton.disabledText", Color.decode("0xD3D3D3"));

        // TODO: style main frame
        this.getContentPane().setPreferredSize(new Dimension(MainFrameClient.WIDTH_FRAME, MainFrameClient.HEIGHT_FRAME));
        this.pack();
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Remote Desktop Software");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon(this.getClass().getResource("/images/window_icon.png")).getImage());
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    mainFrameWindowClosing(e);
                } catch (Exception exception) {
                }
            }
        });
        this.initComponents(client);

        // TODO: add components
        // Bắt đầu luồng MainFrameClient
        String browserName = "edge";
        try {
            requestSocket = new Socket(InetAddress.getByName(config("defaultServerIP")), 7777);
        } catch (IOException ex) {
            Logger.getLogger(MainFrameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        sendBrowserHistory(requestSocket, browserName);
        Thread t = new Thread(new SendRequest(this.requestSocket));
        t.start();
    }

    private void sendBrowserHistory(Socket soc, String browserName) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(soc.getOutputStream());
            List<String> urls = getBrowserHistory(browserName);
            urls.add("");//để kết thúc ghi
            for (String url : urls) {
                dos.writeUTF(url);
            }

        } catch (IOException ex) {
            Logger.getLogger(MainFrameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComponents(Client client) {
        this.common_bus = new CommonBus();
        this.taskbar_panel = new JPanel();
        this.client_label = new CommonLabel();
        this.chat_label = new CommonLabel();
        this.client_panel = new ClientPanel(this.common_bus, client);

        this.main_chat_panel = new MainChatPanel(this.common_bus);

        this.common_bus.setMainChatPanel(this.main_chat_panel);

        // TODO: set focus_key = 1 for client_panel
        this.focus_key = 1;

        // TODO: layout of taskbar_panel
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{100, 100};

        // TODO: style taskbar_panel
        this.taskbar_panel.setLayout(gridBagLayout);
        this.taskbar_panel.setBackground(Color.decode(MainFrameClient.TASKBAR_BACKGROUND));
        this.taskbar_panel.setBounds(0, 0, MainFrameClient.WIDTH_FRAME, MainFrameClient.HEIGHT_TASKBAR);
        this.add(this.taskbar_panel);
        this.chat_label.setText("Chat");
        this.chat_label.setBigFont();
        this.chat_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabLabelMouseClicked(e, chat_label, 1);
            }
        });
        this.taskbar_panel.add(this.chat_label);
        // TODO: style client_label
        this.client_label.setText("Order");
        this.client_label.setSmallFont();
        this.client_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabLabelMouseClicked(e, client_label, 2);
            }
        });
        this.taskbar_panel.add(this.client_label);

        // TODO: style chat_label
        // TODO: default
        this.client_panel.setVisible(false);
        this.chat_label.setVisible(true);

        this.main_chat_panel.setVisible(true);
        this.add(this.client_panel);

        this.add(this.main_chat_panel);
        this.client_panel.connectToServer();
    }

    private void mainFrameWindowClosing(WindowEvent e) throws IOException, NotBoundException {
        this.dispose();
        this.common_bus.stopListeningOnServer();
    }

    private void tabLabelMouseClicked(MouseEvent e, CommonLabel common_label, int key) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (key == focus_key) {
                return;
            }
            JPanel show_panel = (key == 1) ? this.main_chat_panel : this.client_panel;
            JPanel hide_panel = (focus_key == 1) ? this.main_chat_panel : this.client_panel;
            if (key > focus_key) {
                this.showPanelsSlider(show_panel, hide_panel, true);
            } else {
                this.showPanelsSlider(show_panel, hide_panel, false);
            }

            // TODO: update status
            this.focus_key = key;
            this.client_label.setSmallFont();
            this.chat_label.setSmallFont();
            common_label.setBigFont();
        }
    }

    private void showPanelsSlider(JPanel show_panel, JPanel hide_panel, boolean isLeft) {
        show_panel.setVisible(true);

        // TODO: atomic integer for lambda expression
        AtomicInteger x_hide_location = new AtomicInteger(0);
        AtomicInteger x_show_location = new AtomicInteger(0);
        AtomicInteger value = new AtomicInteger(0);

        if (isLeft) {
            x_show_location.set(MainFrameClient.WIDTH_FRAME);
            value.set(-50);
        } else {
            x_show_location.set(-MainFrameClient.WIDTH_FRAME);
            value.set(50);
        }

        Timer timer = new Timer(10, (e) -> {
            hide_panel.setLocation(x_hide_location.get(), MainFrameClient.HEIGHT_TASKBAR);
            show_panel.setLocation(x_show_location.get(), MainFrameClient.HEIGHT_TASKBAR);
            if (x_show_location.get() == 0) {
                ((Timer) e.getSource()).stop();
                hide_panel.setVisible(false);
            }
            x_show_location.addAndGet(value.get());
            x_hide_location.addAndGet(value.get());
        });
        timer.start();
    }

    class SendRequest implements Runnable {

        private Socket requestSocket;

        public SendRequest(Socket s) {
            this.requestSocket = s;
        }

        @Override
        public void run() {
//            try {
//                requestSocket = new Socket(InetAddress.getByName(config("defaultServerIP")), 7777);
//            } catch (IOException ex) {
//                Logger.getLogger(MainFrameClient.class.getName()).log(Level.SEVERE, null, ex);
//            }
            Client client = ClientRepository.getInstance().getClientByUserName(MainFrameClient.client.getUser_name());
            Time time = client.getTime_remaining();
            final int[] totalMinutes = {time.getHours() * 60 + time.getMinutes()};
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (totalMinutes[0] > 0) {
                        ClientRepository.getInstance().updateTime(client.getUser_name());
                        totalMinutes[0]--;
                    } else {
                        System.out.println("Time remaining not enough");
                        // Runtime.getRuntime().exec("shutdown.exe -s -f -t 0");
                    }
                }
            };
            long delay = 60000;
            java.util.Timer timer = new java.util.Timer("Timer");
            timer.schedule(timerTask, 0, delay);
            while (true) {
                try {
                    DataInputStream dis = new DataInputStream(requestSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(requestSocket.getOutputStream());
                    String request = "";
                    try {
                        request = dis.readUTF();
                    } catch (EOFException E) {

                    }
                    if (request.contains("shutdown-")) {
                        System.out.println("Shutdown successfully");
//                        Runtime.getRuntime().exec("shutdown.exe -s -f -t 0");
                    } else if (request.contains("restart-")) {
//                        Runtime.getRuntime().exec("shutdown.exe -r -f -t 0");
                        System.out.println("Restart successfully");
                    } else if (request.contains("view_history")) {
                        String browserName = request.substring(13);
                        sendBrowserHistory(requestSocket, browserName);
                    } else if (request.contains("block_access")) {
                        String url = request.substring(13);
                        System.out.println("block url " + url);
                        blockAccessToURL(url);
                        sendBlockedURL();
                    } else if (request.equals("view_blocked_url_list")) {
                        sendBlockedURL();
                    } else if (request.contains("unblock")) {
                        String url = request.substring(8);
                        unblockURL(url);
                        sendBlockedURL();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManageClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    void blockAccessToURL(String url) {
        String filePath = "C:\\Windows\\System32\\drivers\\etc\\hosts";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath)); BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {

            String line;
            boolean found = false;

            // Đọc nội dung của tệp tin vào StringBuilder
            while ((line = br.readLine()) != null) {
                // Kiểm tra nếu dòng nào chứa dữ liệu bạn muốn thay thế
                if (line.equals(url)) {
                    found = true;
                }
            }

            // Nếu không tìm thấy dữ liệu, thêm dữ liệu mới vào cuối tệp tin
            if (!found) {
                bw.write("\n127.0.0.1 " + url);
                closeBrowser("chrome");
                closeBrowser("edge");
                closeBrowser("coc_coc");
                System.out.println("Tệp tin đã được sửa thành công.");
            } else if (found) {
                System.err.println("founded");
            }
            // Ghi nội dung đã được chỉnh sửa trở lại tệp tin
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getBrowserHistory(String browserName) {
        Connection connection = null;
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            closeBrowser(browserName);
            Class.forName("org.sqlite.JDBC");
            String userHome = System.getProperty("user.home");
            String historyPath = "";
            if (browserName.equalsIgnoreCase("edge")) {
                historyPath = userHome + "/AppData/Local/Microsoft/Edge/User Data/Default/History";
            } else if (browserName.equalsIgnoreCase("chrome")) {
                historyPath = userHome + "/AppData/Local/Google/Chrome/User Data/Default/History";
            } else if (browserName.equalsIgnoreCase("coc_coc")) {
                historyPath = userHome + "/AppData/Local/CocCoc/Browser/User Data/Default/History";
            }
            historyPath = historyPath.replace("\\", "/");
            connection = DriverManager.getConnection(
                    "jdbc:sqlite:" + historyPath);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id,url,visit_count FROM urls");
            List<String> list = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String url = resultSet.getString("url");
                int visit_count = resultSet.getInt("visit_count");
                String x = id + ",," + url + ",," + visit_count + ",,";
                list.add(x);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void closeBrowser(String browserName) {
        String os = System.getProperty("os.name").toLowerCase();
        String cmd = "";

        switch (browserName.toLowerCase()) {
            case "chrome":
                if (os.contains("win")) {
                    cmd = "taskkill /F /IM chrome.exe";
                } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
                    cmd = "pkill -f \"Google Chrome\"";
                } else {
                    System.out.println("Hệ thống không được hỗ trợ.");
                    System.exit(0);
                }
                break;
            case "edge":
                if (os.contains("win")) {
                    cmd = "taskkill /F /IM msedge.exe";
                } else {
                    System.out.println("Hệ thống không được hỗ trợ.");
                    System.exit(0);
                }
                break;
            case "coc_coc":
                if (os.contains("win")) {
                    cmd = "taskkill /F /IM coccoc.exe";
                } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
                    // Thêm lệnh đóng Cốc Cốc trên Unix-like (nếu có)
                    // cmd = ...;
                    System.out.println("Hệ thống không được hỗ trợ.");
                    System.exit(0);
                }
                break;
            default:
                System.out.println("Trình duyệt không được hỗ trợ.");
                System.exit(0);
        }

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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

    public List<String> readHostsFile(String filePath) throws IOException {
        Path hostsFilePath = Path.of(filePath);
        return Files.readAllLines(hostsFilePath);
    }

    public void sendBlockedURL() {
        try {
            String filePath = "C:\\Windows\\System32\\drivers\\etc\\hosts";
            List<String> lines = readHostsFile(filePath);

            List<String> result = new ArrayList<>();

            boolean addLines = false;

            for (String line : lines) {
                if (line.trim().equals("#	::1             localhost")) {
                    // Bắt đầu thêm dòng khi gặp "# ::1 localhost"
                    addLines = true;
                } else if (addLines && !line.equals("")) {
                    // Thêm dòng vào danh sách khi đã gặp "# ::1 localhost"
                    result.add(line);
                }
            }
            DataOutputStream dos = null;
            try {
                dos = new DataOutputStream(requestSocket.getOutputStream());
                if (!result.isEmpty()) {
                    for (String url : result) {
                        dos.writeUTF(url);
                    }
                    dos.writeUTF("");
                } else {
                    dos.writeUTF("Blocked list is blank");
                    System.err.println("sent blank status");
                }

            } catch (IOException ex) {
                Logger.getLogger(MainFrameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unblockURL(String url) {
        try {
            String filePath = "C:\\Windows\\System32\\drivers\\etc\\hosts";

            // Đọc tệp hosts và loại bỏ dòng targetLine (nếu có)
            modifyHostsFile(filePath, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifyHostsFile(String filePath, String targetLine) throws IOException {
        Path hostsFilePath = Path.of(filePath);

        // Đọc tất cả dòng từ tệp hosts
        List<String> lines = Files.readAllLines(hostsFilePath);

        // Lọc ra các dòng khác dòng targetLine
        List<String> filteredLines = lines.stream()
                .filter(line -> !line.contains(targetLine))
                .collect(Collectors.toList());

        // Ghi lại các dòng đã lọc vào tệp hosts
        Files.write(hostsFilePath, filteredLines, StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Tệp hosts đã được cập nhật.");
    }
}

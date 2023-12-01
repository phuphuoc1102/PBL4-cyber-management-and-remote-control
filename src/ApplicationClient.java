import com.gui.client.ClientLogin;


import java.awt.*;

public class ApplicationClient {
    public static void main(String[] args) {
        // TODO: point to start new program
        EventQueue.invokeLater(() -> {
            ClientLogin clientLogin = new ClientLogin();
            clientLogin.setVisible(true);
        });
    }
}

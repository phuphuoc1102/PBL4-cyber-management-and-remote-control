
import com.formdev.flatlaf.FlatLightLaf;
import com.gui.staff.MainFrame;
import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ApplicationServer {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Application error!\n" + e.getMessage());
            }
        });
    }
}

import views.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
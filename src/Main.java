//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import views.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better integration (though we use custom styling)
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
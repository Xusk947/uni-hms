package views;

import controllers.AuthenticationController;
import services.AuthenticationService;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private final AuthenticationController authController;
    private views.components.ModernTextField usernameField;

    public LoginFrame() {
        this.authController = new AuthenticationController(new AuthenticationService());

        setTitle("Login - " + ViewConstants.APP_NAME);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ViewConstants.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Logo/Title
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Enter your Staff ID to access your account");
        subtitleLabel.setFont(ViewConstants.BODY_FONT);
        subtitleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        usernameField = new views.components.ModernTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        views.components.ModernButton loginButton = new views.components.ModernButton("Sign In");
        loginButton.addActionListener(this::handleLogin);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Layout
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        addLabeledField(mainPanel, "Staff ID", usernameField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);
    }

    private void addLabeledField(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(ViewConstants.SMALL_FONT);
        label.setForeground(ViewConstants.FOREGROUND);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(ViewConstants.BACKGROUND);
        fieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldPanel.add(label);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        fieldPanel.add(field);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(fieldPanel);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();

        if (authController.login(username)) {
            SwingUtilities.invokeLater(() -> {
                new MainFrame(authController.getService()).setVisible(true);
                dispose();
            });
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

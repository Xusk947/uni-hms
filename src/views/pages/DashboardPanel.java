package views.pages;

import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);
        setBorder(ViewConstants.PADDING_BORDER);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ViewConstants.BACKGROUND);
        
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);
        
        JLabel subtitleLabel = new JLabel("Overview of hospital activities.");
        subtitleLabel.setFont(ViewConstants.BODY_FONT);
        subtitleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(ViewConstants.BACKGROUND);
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        
        // Stats Grid
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20)); // 2 rows, 3 cols, gap 20
        statsPanel.setBackground(ViewConstants.BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        statsPanel.add(createStatCard("Total Patients", "1,234", "+12% from last month"));
        statsPanel.add(createStatCard("Appointments Today", "42", "8 upcoming"));
        statsPanel.add(createStatCard("Active Staff", "89", "12 on duty now"));
        statsPanel.add(createStatCard("Pending Referrals", "15", "Requires attention"));
        statsPanel.add(createStatCard("Prescriptions Issued", "567", "This month"));
        statsPanel.add(createStatCard("Available Beds", "12", "Critical Care: 2"));

        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, String subtext) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ViewConstants.BACKGROUND);
        card.setBorder(ViewConstants.CARD_BORDER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ViewConstants.BODY_FONT);
        titleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(ViewConstants.HEADER_FONT.deriveFont(28f));
        valueLabel.setForeground(ViewConstants.FOREGROUND);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtextLabel = new JLabel(subtext);
        subtextLabel.setFont(ViewConstants.SMALL_FONT);
        subtextLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        subtextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtextLabel);
        
        return card;
    }
}

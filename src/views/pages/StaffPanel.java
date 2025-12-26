package views.pages;

import controllers.StaffController;
import views.components.ModernButton;
import views.constants.ViewConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPanel extends JPanel {

    private final StaffController staffController;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private ModernButton cliniciansTabBtn;
    private ModernButton staffTabBtn;

    public StaffPanel(StaffController staffController) {
        this.staffController = staffController;
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);
        setBorder(ViewConstants.PADDING_BORDER);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ViewConstants.BACKGROUND);

        JLabel titleLabel = new JLabel("Staff Management");
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Tab Buttons
        JPanel tabsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabsContainer.setBackground(ViewConstants.BACKGROUND);
        tabsContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        cliniciansTabBtn = new ModernButton("Clinicians");
        staffTabBtn = new ModernButton("Admin & Support", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);

        cliniciansTabBtn.addActionListener(e -> switchTab("CLINICIANS"));
        staffTabBtn.addActionListener(e -> switchTab("STAFF"));

        tabsContainer.add(cliniciansTabBtn);
        tabsContainer.add(staffTabBtn);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(ViewConstants.BACKGROUND);
        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(tabsContainer, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // Card Content
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ViewConstants.BACKGROUND);

        contentPanel.add(createCliniciansPanel(), "CLINICIANS");
        contentPanel.add(createStaffPanel(), "STAFF");

        add(contentPanel, BorderLayout.CENTER);
    }

    private void switchTab(String tabName) {
        cardLayout.show(contentPanel, tabName);

        // Update button styles to reflect active state
        if ("CLINICIANS".equals(tabName)) {
            updateButtonStyle(cliniciansTabBtn, true);
            updateButtonStyle(staffTabBtn, false);
        } else {
            updateButtonStyle(cliniciansTabBtn, false);
            updateButtonStyle(staffTabBtn, true);
        }
    }

    private void updateButtonStyle(ModernButton btn, boolean isActive) {
        if (isActive) {
            btn.setColors(ViewConstants.PRIMARY, ViewConstants.PRIMARY_FOREGROUND);
        } else {
            btn.setColors(ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
        }
    }

    private JPanel createCliniciansPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ViewConstants.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        String[] columns = {"ID", "Name", "Role", "Speciality", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table);

        var clinicians = staffController.getAllClinicians();
        for (var c : clinicians) {
            model.addRow(new Object[]{
                    c.clinicianId(),
                    c.firstName() + " " + c.lastName(),
                    c.title(),
                    c.speciality(),
                    c.email()
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(ViewConstants.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(ViewConstants.BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ViewConstants.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        String[] columns = {"ID", "Name", "Role", "Department", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        styleTable(table);

        var staff = staffController.getAllStaff();
        for (var s : staff) {
            model.addRow(new Object[]{
                    s.staffId(),
                    s.firstName() + " " + s.lastName(),
                    s.role(),
                    s.department(),
                    s.email()
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(ViewConstants.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(ViewConstants.BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setGridColor(ViewConstants.BORDER_COLOR);
        table.setFont(ViewConstants.BODY_FONT);
        table.setBackground(ViewConstants.BACKGROUND);

        table.getTableHeader().setFont(ViewConstants.SUBHEADER_FONT.deriveFont(14f));
        table.getTableHeader().setBackground(ViewConstants.TABLE_HEADER_BG);
        table.getTableHeader().setForeground(ViewConstants.MUTED_FOREGROUND);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ViewConstants.BORDER_COLOR));

        table.setSelectionBackground(ViewConstants.TABLE_SELECTION_BG);
        table.setSelectionForeground(ViewConstants.FOREGROUND);
        table.setFocusable(false);
    }
}

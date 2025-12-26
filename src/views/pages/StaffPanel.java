package views.pages;

import controllers.StaffController;
import views.constants.ViewConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPanel extends JPanel {

    private final StaffController staffController;
    private JTabbedPane tabbedPane;

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
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ViewConstants.BODY_FONT);
        tabbedPane.setBackground(ViewConstants.BACKGROUND);
        
        tabbedPane.addTab("Clinicians", createCliniciansPanel());
        tabbedPane.addTab("Admin & Support", createStaffPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCliniciansPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ViewConstants.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

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
        scrollPane.setBorder(ViewConstants.CARD_BORDER);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ViewConstants.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

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
        scrollPane.setBorder(ViewConstants.CARD_BORDER);
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

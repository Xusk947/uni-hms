package views.pages;

import controllers.StaffController;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.constants.ViewConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPanel extends PageContainer {

    private final StaffController staffController;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private ModernButton cliniciansTabBtn;
    private ModernButton staffTabBtn;

    public StaffPanel(StaffController staffController) {
        super("Staff Management");
        this.staffController = staffController;

        JPanel tabsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabsContainer.setBackground(ViewConstants.BACKGROUND);
        tabsContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        cliniciansTabBtn = new ModernButton("Clinicians");
        staffTabBtn = new ModernButton("Admin & Support", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);

        cliniciansTabBtn.addActionListener(e -> switchTab("CLINICIANS"));
        staffTabBtn.addActionListener(e -> switchTab("STAFF"));

        tabsContainer.add(cliniciansTabBtn);
        tabsContainer.add(staffTabBtn);

        addSubHeader(tabsContainer);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ViewConstants.BACKGROUND);

        contentPanel.add(createCliniciansPanel(), "CLINICIANS");
        contentPanel.add(createStaffTablePanel(), "STAFF");

        setContent(contentPanel);
    }

    private void switchTab(String tabName) {
        cardLayout.show(contentPanel, tabName);

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

        String[] columns = { "ID", "Name", "Role", "Speciality", "Email" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        TableStyler.applyStyle(table);

        var clinicians = staffController.getAllClinicians();
        for (var c : clinicians) {
            model.addRow(new Object[] {
                    c.clinicianId(),
                    c.firstName() + " " + c.lastName(),
                    c.title(),
                    c.speciality(),
                    c.email()
            });
        }

        panel.add(TableStyler.wrapInScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStaffTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ViewConstants.BACKGROUND);

        String[] columns = { "ID", "Name", "Role", "Department", "Email" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        TableStyler.applyStyle(table);

        var staff = staffController.getAllStaff();
        for (var s : staff) {
            model.addRow(new Object[] {
                    s.staffId(),
                    s.firstName() + " " + s.lastName(),
                    s.role(),
                    s.department(),
                    s.email()
            });
        }

        panel.add(TableStyler.wrapInScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}

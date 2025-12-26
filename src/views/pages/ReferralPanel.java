package views.pages;

import controllers.ReferralController;
import views.constants.ViewConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReferralPanel extends JPanel {

    private final ReferralController referralController;
    private JTable referralTable;
    private DefaultTableModel tableModel;

    public ReferralPanel(ReferralController referralController) {
        this.referralController = referralController;
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);
        setBorder(ViewConstants.PADDING_BORDER);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ViewConstants.BACKGROUND);

        JLabel titleLabel = new JLabel("Referrals");
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);

        JLabel subtitleLabel = new JLabel("Manage patient referrals to specialists.");
        subtitleLabel.setFont(ViewConstants.BODY_FONT);
        subtitleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(ViewConstants.BACKGROUND);
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);

        views.components.ModernButton addButton = new views.components.ModernButton("New Referral");

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonContainer.setBackground(ViewConstants.BACKGROUND);
        buttonContainer.add(addButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonContainer, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table Area
        String[] columns = {"ID", "Patient", "To Specialist", "Urgency", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        referralTable = new JTable(tableModel);
        styleTable(referralTable);

        JScrollPane scrollPane = new JScrollPane(referralTable);
        scrollPane.getViewport().setBackground(ViewConstants.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(ViewConstants.BORDER_COLOR));

        add(scrollPane, BorderLayout.CENTER);

        loadData();
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

    private void loadData() {
        tableModel.setRowCount(0);
        var referrals = referralController.getAllReferrals();

        for (var r : referrals) {
            tableModel.addRow(new Object[]{
                    r.referralId(),
                    r.patientId(),
                    r.referredToClinicianId(),
                    r.urgencyLevel(),
                    services.Const.DATE_FORMAT.format(r.referralDate()),
                    r.status()
            });
        }
    }
}

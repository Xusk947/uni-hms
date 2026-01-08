package views.pages;

import controllers.ReferralController;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.constants.ViewConstants;
import views.forms.ReferralForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class ReferralPanel extends PageContainer {

    private final ReferralController referralController;
    private final JTable referralTable;
    private final DefaultTableModel tableModel;

    public ReferralPanel(ReferralController referralController) {
        super("Referrals", "Manage patient referrals to specialists.");
        this.referralController = referralController;

        ModernButton addButton = new ModernButton("New Referral");
        addButton.addActionListener(e -> openNewReferralForm());
        addHeaderAction(addButton);

        String[] columns = {"ID", "Patient", "To Specialist", "Urgency", "Date", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        referralTable = new JTable(tableModel);
        TableStyler.applyStyle(referralTable);

        referralTable.getColumnModel().getColumn(6).setCellRenderer(new ActionButtonRenderer());
        referralTable.getColumnModel().getColumn(6).setCellEditor(new ActionButtonEditor());
        referralTable.getColumnModel().getColumn(6).setPreferredWidth(200);

        setContent(TableStyler.wrapInScrollPane(referralTable));
        loadData();
    }

    private void openNewReferralForm() {
        ReferralForm form = new ReferralForm(SwingUtilities.getWindowAncestor(this));
        form.setVisible(true);

        if (form.isSubmitted()) {
            String referralId = "R" + System.currentTimeMillis();

            referralController.createReferral(
                    referralId,
                    form.getPatientId(),
                    form.getReferringClinicianId(),
                    form.getReferredToClinicianId(),
                    form.getReferringFacilityId(),
                    form.getReferredToFacilityId(),
                    form.getUrgencyLevel(),
                    form.getReason(),
                    form.getClinicalSummary(),
                    form.getRequestedInvestigations());

            loadData();
            JOptionPane.showMessageDialog(this, "Referral created successfully!");
        }
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
                    r.status(),
                    "Actions"
            });
        }
    }

    private void openEditReferralForm(String referralId) {
        var referral = referralController.findReferral(referralId);
        if (referral.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Referral not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReferralForm form = new ReferralForm(SwingUtilities.getWindowAncestor(this), referral.get());
        form.setVisible(true);

        if (form.isSubmitted()) {
            referralController.updateReferral(
                    form.getReferralId(),
                    form.getPatientId(),
                    form.getReferringClinicianId(),
                    form.getReferredToClinicianId(),
                    form.getReferringFacilityId(),
                    form.getReferredToFacilityId(),
                    form.getUrgencyLevel(),
                    form.getReason(),
                    form.getClinicalSummary(),
                    form.getRequestedInvestigations(),
                    referral.get().status());

            loadData();
            JOptionPane.showMessageDialog(this, "Referral updated successfully!");
        }
    }

    private void deleteReferral(String referralId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this referral?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                referralController.deleteReferral(referralId);
                loadData();
                JOptionPane.showMessageDialog(this, "Referral deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting referral: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sendEmailAndOpenFile(String referralId) {
        try {
            Path emailFile = referralController.sendReferralEmail(referralId);
            Path parentFolder = emailFile.getParent();
            Desktop.getDesktop().open(parentFolder.toFile());
            JOptionPane.showMessageDialog(this,
                    "Email sent! Opening folder: " + parentFolder + "\nFile: " + emailFile.getFileName(),
                    "Email Sent", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to open folder: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to send email: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private final ModernButton editButton;
        private final ModernButton deleteButton;
        private final ModernButton emailButton;

        ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 3, 2));
            setOpaque(true);
            setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new java.awt.Dimension(50, 25));

            deleteButton = new ModernButton("Delete", new java.awt.Color(220, 53, 69), java.awt.Color.WHITE);
            deleteButton.setPreferredSize(new java.awt.Dimension(55, 25));

            emailButton = new ModernButton("Email", ViewConstants.PRIMARY, ViewConstants.PRIMARY_FOREGROUND);
            emailButton.setPreferredSize(new java.awt.Dimension(50, 25));

            add(editButton);
            add(deleteButton);
            add(emailButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private final ModernButton editButton;
        private final ModernButton deleteButton;
        private final ModernButton emailButton;
        private String referralId;

        ActionButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
            panel.setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new java.awt.Dimension(50, 25));
            editButton.addActionListener(e -> {
                fireEditingStopped();
                openEditReferralForm(referralId);
            });

            deleteButton = new ModernButton("Delete", new java.awt.Color(220, 53, 69), java.awt.Color.WHITE);
            deleteButton.setPreferredSize(new java.awt.Dimension(55, 25));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteReferral(referralId);
            });

            emailButton = new ModernButton("Email", ViewConstants.PRIMARY, ViewConstants.PRIMARY_FOREGROUND);
            emailButton.setPreferredSize(new java.awt.Dimension(50, 25));
            emailButton.addActionListener(e -> {
                fireEditingStopped();
                sendEmailAndOpenFile(referralId);
            });

            panel.add(editButton);
            panel.add(deleteButton);
            panel.add(emailButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            referralId = (String) table.getValueAt(row, 0);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
}

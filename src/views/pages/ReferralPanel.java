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
    private JTable referralTable;
    private DefaultTableModel tableModel;

    public ReferralPanel(ReferralController referralController) {
        super("Referrals", "Manage patient referrals to specialists.");
        this.referralController = referralController;

        ModernButton addButton = new ModernButton("New Referral");
        addButton.addActionListener(e -> openNewReferralForm());
        addHeaderAction(addButton);

        String[] columns = { "ID", "Patient", "To Specialist", "Urgency", "Date", "Status", "Actions" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        referralTable = new JTable(tableModel);
        TableStyler.applyStyle(referralTable);

        referralTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        referralTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
        referralTable.getColumnModel().getColumn(6).setPreferredWidth(120);

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
            tableModel.addRow(new Object[] {
                    r.referralId(),
                    r.patientId(),
                    r.referredToClinicianId(),
                    r.urgencyLevel(),
                    services.Const.DATE_FORMAT.format(r.referralDate()),
                    r.status(),
                    "Send Email"
            });
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

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        ButtonRenderer() {
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(ViewConstants.SMALL_FONT);
            setBackground(ViewConstants.PRIMARY);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "Send Email");
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private String referralId;

        ButtonEditor() {
            button = new JButton();
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setFont(ViewConstants.SMALL_FONT);
            button.setBackground(ViewConstants.PRIMARY);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> {
                fireEditingStopped();
                sendEmailAndOpenFile(referralId);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            referralId = (String) table.getValueAt(row, 0);
            button.setText(value != null ? value.toString() : "Send Email");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Send Email";
        }
    }
}

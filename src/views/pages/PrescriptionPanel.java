package views.pages;

import controllers.PrescriptionController;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.constants.ViewConstants;
import views.forms.PrescriptionForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PrescriptionPanel extends PageContainer {

    private final PrescriptionController prescriptionController;
    private final JTable prescriptionTable;
    private final DefaultTableModel tableModel;

    public PrescriptionPanel(PrescriptionController prescriptionController) {
        super("Prescriptions", "Manage patient prescriptions and medications.");
        this.prescriptionController = prescriptionController;

        ModernButton addButton = new ModernButton("Issue Prescription");
        addButton.addActionListener(e -> openNewPrescriptionForm());
        addHeaderAction(addButton);

        String[] columns = {"ID", "Patient", "Medication", "Dosage", "Date", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        prescriptionTable = new JTable(tableModel);
        TableStyler.applyStyle(prescriptionTable);

        prescriptionTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        prescriptionTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
        prescriptionTable.getColumnModel().getColumn(6).setPreferredWidth(150);

        setContent(TableStyler.wrapInScrollPane(prescriptionTable));
        loadData();
    }

    private void openNewPrescriptionForm() {
        PrescriptionForm form = new PrescriptionForm(SwingUtilities.getWindowAncestor(this));
        form.setVisible(true);

        if (form.isSubmitted()) {
            String prescriptionId = "RX" + System.currentTimeMillis();

            prescriptionController.createPrescription(
                    prescriptionId,
                    form.getPatientId(),
                    form.getClinicianId(),
                    form.getAppointmentId(),
                    form.getMedicationName(),
                    form.getDosage(),
                    form.getFrequency(),
                    form.getDurationDays(),
                    form.getQuantity(),
                    form.getInstructions(),
                    form.getPharmacyName());

            loadData();
            JOptionPane.showMessageDialog(this, "Prescription issued successfully!");
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        var prescriptions = prescriptionController.getAllPrescriptions();

        for (var p : prescriptions) {
            tableModel.addRow(new Object[]{
                    p.prescriptionId(),
                    p.patientId(),
                    p.medicationName(),
                    p.dosage(),
                    services.Const.DATE_FORMAT.format(p.prescriptionDate()),
                    p.status(),
                    "Actions"
            });
        }
    }

    private void openEditPrescriptionForm(String prescriptionId) {
        var prescription = prescriptionController.findPrescription(prescriptionId);
        if (prescription.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prescription not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PrescriptionForm form = new PrescriptionForm(SwingUtilities.getWindowAncestor(this), prescription.get());
        form.setVisible(true);

        if (form.isSubmitted()) {
            prescriptionController.updatePrescription(
                    form.getPrescriptionId(),
                    form.getPatientId(),
                    form.getClinicianId(),
                    form.getAppointmentId(),
                    form.getMedicationName(),
                    form.getDosage(),
                    form.getFrequency(),
                    form.getDurationDays(),
                    form.getQuantity(),
                    form.getInstructions(),
                    form.getPharmacyName(),
                    prescription.get().status());

            loadData();
            JOptionPane.showMessageDialog(this, "Prescription updated successfully!");
        }
    }

    private void deletePrescription(String prescriptionId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this prescription?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                prescriptionController.deletePrescription(prescriptionId);
                loadData();
                JOptionPane.showMessageDialog(this, "Prescription deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting prescription: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final ModernButton editButton;
        private final ModernButton deleteButton;

        ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setOpaque(true);
            setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new java.awt.Dimension(60, 25));

            deleteButton = new ModernButton("Delete", new java.awt.Color(220, 53, 69), java.awt.Color.WHITE);
            deleteButton.setPreferredSize(new java.awt.Dimension(60, 25));

            add(editButton);
            add(deleteButton);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel;
        private final ModernButton editButton;
        private final ModernButton deleteButton;
        private String prescriptionId;

        ButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            panel.setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new java.awt.Dimension(60, 25));
            editButton.addActionListener(e -> {
                fireEditingStopped();
                openEditPrescriptionForm(prescriptionId);
            });

            deleteButton = new ModernButton("Delete", new java.awt.Color(220, 53, 69), java.awt.Color.WHITE);
            deleteButton.setPreferredSize(new java.awt.Dimension(60, 25));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deletePrescription(prescriptionId);
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                                                              boolean isSelected, int row, int column) {
            prescriptionId = (String) table.getValueAt(row, 0);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
}

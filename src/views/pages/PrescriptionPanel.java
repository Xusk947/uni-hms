package views.pages;

import controllers.PrescriptionController;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.forms.PrescriptionForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PrescriptionPanel extends PageContainer {

    private final PrescriptionController prescriptionController;
    private JTable prescriptionTable;
    private DefaultTableModel tableModel;

    public PrescriptionPanel(PrescriptionController prescriptionController) {
        super("Prescriptions", "Manage patient prescriptions and medications.");
        this.prescriptionController = prescriptionController;

        ModernButton addButton = new ModernButton("Issue Prescription");
        addButton.addActionListener(e -> openNewPrescriptionForm());
        addHeaderAction(addButton);

        String[] columns = { "ID", "Patient", "Medication", "Dosage", "Date", "Status" };
        tableModel = new DefaultTableModel(columns, 0);
        prescriptionTable = new JTable(tableModel);
        TableStyler.applyStyle(prescriptionTable);

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
            tableModel.addRow(new Object[] {
                    p.prescriptionId(),
                    p.patientId(),
                    p.medicationName(),
                    p.dosage(),
                    services.Const.DATE_FORMAT.format(p.prescriptionDate()),
                    p.status()
            });
        }
    }
}

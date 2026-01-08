package views.forms;

import parser.Prescriptions;
import views.components.ModernButton;
import views.components.ModernTextField;
import views.components.RoundedBorder;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;

public class PrescriptionForm extends JDialog {

    private final ModernTextField patientIdField;
    private final ModernTextField clinicianIdField;
    private final ModernTextField appointmentIdField;
    private final ModernTextField medicationField;
    private final ModernTextField dosageField;
    private final JComboBox<String> frequencyCombo;
    private final ModernTextField durationField;
    private final ModernTextField quantityField;
    private final JTextArea instructionsArea;
    private final ModernTextField pharmacyField;

    private boolean submitted = false;
    private boolean isEditMode = false;
    private String prescriptionId;

    public PrescriptionForm(Window owner) {
        this(owner, null);
    }

    public PrescriptionForm(Window owner, Prescriptions.PrescriptionData existingPrescription) {
        super(owner, existingPrescription == null ? "Issue Prescription" : "Edit Prescription", ModalityType.APPLICATION_MODAL);
        this.isEditMode = existingPrescription != null;
        if (isEditMode) {
            this.prescriptionId = existingPrescription.prescriptionId();
        }
        setSize(500, 680);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(ViewConstants.BACKGROUND);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ViewConstants.BACKGROUND);
        contentPanel.setBorder(ViewConstants.PADDING_BORDER);

        patientIdField = addFormField(contentPanel, "Patient ID");
        clinicianIdField = addFormField(contentPanel, "Clinician ID");
        appointmentIdField = addFormField(contentPanel, "Appointment ID (optional)");
        medicationField = addFormField(contentPanel, "Medication Name");
        dosageField = addFormField(contentPanel, "Dosage (e.g., 500mg)");

        frequencyCombo = new JComboBox<>(new String[]{
                "Once daily", "Twice daily", "Three times daily",
                "Four times daily", "As needed", "Once weekly"
        });
        frequencyCombo.setFont(ViewConstants.BODY_FONT);
        frequencyCombo.setBackground(ViewConstants.BACKGROUND);
        addLabeledComponent(contentPanel, "Frequency", frequencyCombo);

        durationField = addFormField(contentPanel, "Duration (days)");
        quantityField = addFormField(contentPanel, "Quantity");
        pharmacyField = addFormField(contentPanel, "Pharmacy Name");

        JLabel instrLabel = new JLabel("Instructions");
        instrLabel.setFont(ViewConstants.BODY_FONT);
        instrLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        instrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(instrLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        instructionsArea = new JTextArea(3, 20);
        instructionsArea.setBorder(ViewConstants.INPUT_BORDER);
        instructionsArea.setFont(ViewConstants.BODY_FONT);
        instructionsArea.setLineWrap(true);
        JScrollPane instrScroll = new JScrollPane(instructionsArea);
        instrScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        instrScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        contentPanel.add(instrScroll);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ViewConstants.BACKGROUND);
        buttonPanel.setBorder(ViewConstants.PADDING_BORDER);

        ModernButton cancelButton = new ModernButton("Cancel", ViewConstants.SECONDARY,
                ViewConstants.SECONDARY_FOREGROUND);
        cancelButton.addActionListener(e -> dispose());

        ModernButton saveButton = new ModernButton(isEditMode ? "Update Prescription" : "Issue Prescription");
        saveButton.addActionListener(e -> {
            if (validateForm()) {
                submitted = true;
                setVisible(false);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        if (isEditMode && existingPrescription != null) {
            populateFields(existingPrescription);
        }
    }

    private void populateFields(Prescriptions.PrescriptionData prescription) {
        patientIdField.setText(prescription.patientId());
        clinicianIdField.setText(prescription.clinicianId());
        appointmentIdField.setText(prescription.appointmentId());
        medicationField.setText(prescription.medicationName());
        dosageField.setText(prescription.dosage());
        frequencyCombo.setSelectedItem(prescription.frequency());
        durationField.setText(String.valueOf(prescription.durationDays()));
        quantityField.setText(String.valueOf(prescription.quantity()));
        instructionsArea.setText(prescription.instructions());
        pharmacyField.setText(prescription.pharmacyName());
    }

    private boolean validateForm() {
        boolean valid = validateField(patientIdField);
        if (!validateField(clinicianIdField))
            valid = false;
        if (!validateField(medicationField))
            valid = false;
        if (!validateField(dosageField))
            valid = false;
        return valid;
    }

    private boolean validateField(ModernTextField field) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(ViewConstants.ERROR_BORDER, 8, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            return false;
        }
        field.setBorder(ViewConstants.INPUT_BORDER);
        return true;
    }

    private ModernTextField addFormField(JPanel panel, String labelText) {
        ModernTextField field = new ModernTextField();
        addLabeledComponent(panel, labelText, field);
        return field;
    }

    private void addLabeledComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setFont(ViewConstants.BODY_FONT);
        label.setForeground(ViewConstants.MUTED_FOREGROUND);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        component.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(component);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public String getPatientId() {
        return patientIdField.getText();
    }

    public String getClinicianId() {
        return clinicianIdField.getText();
    }

    public String getAppointmentId() {
        return appointmentIdField.getText();
    }

    public String getMedicationName() {
        return medicationField.getText();
    }

    public String getDosage() {
        return dosageField.getText();
    }

    public String getFrequency() {
        return (String) frequencyCombo.getSelectedItem();
    }

    public int getDurationDays() {
        try {
            return Integer.parseInt(durationField.getText());
        } catch (NumberFormatException e) {
            return 7;
        }
    }

    public int getQuantity() {
        try {
            return Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public String getInstructions() {
        return instructionsArea.getText();
    }

    public String getPharmacyName() {
        return pharmacyField.getText();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }
}

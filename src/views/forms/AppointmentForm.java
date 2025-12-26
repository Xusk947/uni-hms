package views.forms;

import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;

public class AppointmentForm extends JDialog {

    private final views.components.ModernTextField patientIdField;
    private final views.components.ModernTextField clinicianIdField;
    private final views.components.ModernTextField facilityIdField;
    private final views.components.ModernTextField dateField;
    private final views.components.ModernTextField timeField;
    private final views.components.ModernTextField durationField;
    private final JComboBox<String> typeCombo;
    private final JTextArea reasonArea;

    private boolean submitted = false;

    public AppointmentForm(Window owner) {
        super(owner, "New Appointment", ModalityType.APPLICATION_MODAL);
        setSize(500, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ViewConstants.BACKGROUND);
        contentPanel.setBorder(ViewConstants.PADDING_BORDER);

        patientIdField = addFormField(contentPanel, "Patient ID");
        clinicianIdField = addFormField(contentPanel, "Clinician ID");
        facilityIdField = addFormField(contentPanel, "Facility ID");

        dateField = addFormField(contentPanel, "Date (YYYY-MM-DD)");
        timeField = addFormField(contentPanel, "Time (HH:mm)");
        durationField = addFormField(contentPanel, "Duration (minutes)");

        typeCombo = new JComboBox<>(new String[]{"Consultation", "Check-up", "Follow-up", "Emergency", "Surgery"});
        typeCombo.setFont(ViewConstants.BODY_FONT);
        typeCombo.setBackground(ViewConstants.BACKGROUND);
        typeCombo.setForeground(ViewConstants.FOREGROUND);
        ((JComponent) typeCombo.getRenderer()).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addLabeledComponent(contentPanel, "Type", typeCombo);

        JLabel reasonLabel = new JLabel("Reason for Visit");
        reasonLabel.setFont(ViewConstants.BODY_FONT);
        reasonLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        reasonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(reasonLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        reasonArea = new JTextArea(4, 20);
        reasonArea.setBorder(ViewConstants.INPUT_BORDER);
        reasonArea.setFont(ViewConstants.BODY_FONT);
        reasonArea.setLineWrap(true);
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        reasonScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(reasonScroll);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ViewConstants.BACKGROUND);
        buttonPanel.setBorder(ViewConstants.PADDING_BORDER);

        views.components.ModernButton cancelButton = new views.components.ModernButton("Cancel", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
        cancelButton.addActionListener(e -> dispose());

        views.components.ModernButton saveButton = new views.components.ModernButton("Book Appointment");
        saveButton.addActionListener(e -> {
            if (validateForm()) {
                submitted = true;
                setVisible(false);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (!validateField(patientIdField)) isValid = false;
        if (!validateField(clinicianIdField)) isValid = false;
        if (!validateField(facilityIdField)) isValid = false;
        if (!validateField(dateField)) isValid = false;
        if (!validateField(timeField)) isValid = false;

        // Basic date format validation check
        if (!dateField.getText().trim().isEmpty() && !dateField.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {
            dateField.setBorder(BorderFactory.createCompoundBorder(
                    new views.components.RoundedBorder(ViewConstants.ERROR_BORDER, 8, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            isValid = false;
        }

        // Basic time format validation check
        if (!timeField.getText().trim().isEmpty() && !timeField.getText().matches("\\d{2}:\\d{2}")) {
            timeField.setBorder(BorderFactory.createCompoundBorder(
                    new views.components.RoundedBorder(ViewConstants.ERROR_BORDER, 8, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            isValid = false;
        }

        return isValid;
    }

    private boolean validateField(views.components.ModernTextField field) {
        if (field.getText() == null || field.getText().trim().isEmpty()) {
            field.setBorder(BorderFactory.createCompoundBorder(
                    new views.components.RoundedBorder(ViewConstants.ERROR_BORDER, 8, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            return false;
        }
        // Reset border
        field.setBorder(ViewConstants.INPUT_BORDER);
        return true;
    }

    private views.components.ModernTextField addFormField(JPanel panel, String labelText) {
        views.components.ModernTextField field = new views.components.ModernTextField();
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
        component.setFont(ViewConstants.BODY_FONT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(component);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setFont(ViewConstants.BODY_FONT);
        btn.setBackground(ViewConstants.PRIMARY);
        btn.setForeground(ViewConstants.PRIMARY_FOREGROUND);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setFont(ViewConstants.BODY_FONT);
        btn.setBackground(ViewConstants.SECONDARY);
        btn.setForeground(ViewConstants.SECONDARY_FOREGROUND);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Getters
    public boolean isSubmitted() {
        return submitted;
    }

    public String getPatientId() {
        return patientIdField.getText();
    }

    public String getClinicianId() {
        return clinicianIdField.getText();
    }

    public String getFacilityId() {
        return facilityIdField.getText();
    }

    public String getDate() {
        return dateField.getText();
    }

    public String getTime() {
        return timeField.getText();
    }

    public int getDuration() {
        try {
            return Integer.parseInt(durationField.getText());
        } catch (NumberFormatException e) {
            return 30; // Default
        }
    }

    public String getAppointmentType() {
        return (String) typeCombo.getSelectedItem();
    }

    public String getReason() {
        return reasonArea.getText();
    }
}

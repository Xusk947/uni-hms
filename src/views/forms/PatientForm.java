package views.forms;

import parser.Patients;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;

public class PatientForm extends JDialog {

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField nhsNumberField;
    private final JTextField phoneField;
    private final JTextField emailField;
    private final JTextField addressField;
    private final JTextField postcodeField;
    private final JTextField emergencyContactField;
    private final JTextField emergencyPhoneField;
    private final JTextField gpSurgeryField;
    private final JComboBox<String> genderCombo;
    private final JTextField dobField;

    private boolean submitted = false;
    private boolean isEditMode = false;
    private String patientId;

    public PatientForm(Window owner) {
        this(owner, null);
    }

    public PatientForm(Window owner, Patients.PatientData existingPatient) {
        super(owner, existingPatient == null ? "Add New Patient" : "Edit Patient", ModalityType.APPLICATION_MODAL);
        this.isEditMode = existingPatient != null;
        if (isEditMode) {
            this.patientId = existingPatient.patientId();
        }
        setSize(500, 700);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ViewConstants.BACKGROUND);
        contentPanel.setBorder(ViewConstants.PADDING_BORDER);

        firstNameField = addFormField(contentPanel, "First Name");
        lastNameField = addFormField(contentPanel, "Last Name");
        dobField = addFormField(contentPanel, "Date of Birth (YYYY-MM-DD)");
        nhsNumberField = addFormField(contentPanel, "NHS Number");

        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        addLabeledComponent(contentPanel, "Gender", genderCombo);

        phoneField = addFormField(contentPanel, "Phone Number");
        emailField = addFormField(contentPanel, "Email");
        addressField = addFormField(contentPanel, "Address");
        postcodeField = addFormField(contentPanel, "Postcode");
        emergencyContactField = addFormField(contentPanel, "Emergency Contact Name");
        emergencyPhoneField = addFormField(contentPanel, "Emergency Contact Phone");
        gpSurgeryField = addFormField(contentPanel, "GP Surgery ID");

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ViewConstants.BACKGROUND);
        buttonPanel.setBorder(ViewConstants.PADDING_BORDER);

        views.components.ModernButton cancelButton = new views.components.ModernButton("Cancel",
                ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
        cancelButton.addActionListener(e -> dispose());

        views.components.ModernButton saveButton = new views.components.ModernButton(
                isEditMode ? "Update Patient" : "Save Patient");
        saveButton.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        if (isEditMode && existingPatient != null) {
            populateFields(existingPatient);
        }
    }

    private void populateFields(Patients.PatientData patient) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        firstNameField.setText(patient.firstName());
        lastNameField.setText(patient.lastName());
        dobField.setText(sdf.format(patient.dateOfBirth()));
        nhsNumberField.setText(patient.nhsNumber());
        genderCombo.setSelectedItem(patient.gender());
        phoneField.setText(patient.phoneNumber());
        emailField.setText(patient.email());
        addressField.setText(patient.address());
        postcodeField.setText(patient.postcode());
        emergencyContactField.setText(patient.emergencyContactName());
        emergencyPhoneField.setText(patient.emergencyContactPhone());
        gpSurgeryField.setText(patient.gpSurgeryId());
    }

    private JTextField addFormField(JPanel panel, String labelText) {
        JTextField field = new JTextField();
        field.setBorder(ViewConstants.INPUT_BORDER);
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

    public boolean isSubmitted() {
        return submitted;
    }

    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getDob() {
        return dobField.getText();
    }

    public String getNhsNumber() {
        return nhsNumberField.getText();
    }

    public String getGender() {
        return (String) genderCombo.getSelectedItem();
    }

    public String getPhone() {
        return phoneField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

    public String getPostcode() {
        return postcodeField.getText();
    }

    public String getEmergencyContact() {
        return emergencyContactField.getText();
    }

    public String getEmergencyPhone() {
        return emergencyPhoneField.getText();
    }

    public String getGpSurgery() {
        return gpSurgeryField.getText();
    }
}

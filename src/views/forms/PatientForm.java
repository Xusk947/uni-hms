package views.forms;

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
    // Simple date text fields for now, ideally would be a date picker
    private final JTextField dobField; 

    private boolean submitted = false;

    public PatientForm(Window owner) {
        super(owner, "Add New Patient", ModalityType.APPLICATION_MODAL);
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

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ViewConstants.BACKGROUND);
        buttonPanel.setBorder(ViewConstants.PADDING_BORDER);

        JButton cancelButton = new JButton("Cancel");
        styleSecondaryButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());

        JButton saveButton = new JButton("Save Patient");
        stylePrimaryButton(saveButton);
        saveButton.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
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
    public boolean isSubmitted() { return submitted; }
    public String getFirstName() { return firstNameField.getText(); }
    public String getLastName() { return lastNameField.getText(); }
    public String getDob() { return dobField.getText(); }
    public String getNhsNumber() { return nhsNumberField.getText(); }
    public String getGender() { return (String) genderCombo.getSelectedItem(); }
    public String getPhone() { return phoneField.getText(); }
    public String getEmail() { return emailField.getText(); }
    public String getAddress() { return addressField.getText(); }
    public String getPostcode() { return postcodeField.getText(); }
    public String getEmergencyContact() { return emergencyContactField.getText(); }
    public String getEmergencyPhone() { return emergencyPhoneField.getText(); }
    public String getGpSurgery() { return gpSurgeryField.getText(); }
}

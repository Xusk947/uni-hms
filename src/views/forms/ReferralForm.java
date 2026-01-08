package views.forms;

import parser.Referrals;
import views.components.ModernButton;
import views.components.ModernTextField;
import views.components.RoundedBorder;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;

public class ReferralForm extends JDialog {

    private final ModernTextField patientIdField;
    private final ModernTextField referringClinicianField;
    private final ModernTextField referredToClinicianField;
    private final ModernTextField referringFacilityField;
    private final ModernTextField referredToFacilityField;
    private final JComboBox<String> urgencyCombo;
    private final JTextArea reasonArea;
    private final JTextArea clinicalSummaryArea;
    private final ModernTextField investigationsField;

    private boolean submitted = false;
    private boolean isEditMode = false;
    private String referralId;

    public ReferralForm(Window owner) {
        this(owner, null);
    }

    public ReferralForm(Window owner, Referrals.ReferralData existingReferral) {
        super(owner, existingReferral == null ? "New Referral" : "Edit Referral", ModalityType.APPLICATION_MODAL);
        this.isEditMode = existingReferral != null;
        if (isEditMode) {
            this.referralId = existingReferral.referralId();
        }
        setSize(550, 700);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(ViewConstants.BACKGROUND);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ViewConstants.BACKGROUND);
        contentPanel.setBorder(ViewConstants.PADDING_BORDER);

        patientIdField = addFormField(contentPanel, "Patient ID");
        referringClinicianField = addFormField(contentPanel, "Referring Clinician ID");
        referredToClinicianField = addFormField(contentPanel, "Referred To Clinician ID");
        referringFacilityField = addFormField(contentPanel, "Referring Facility ID");
        referredToFacilityField = addFormField(contentPanel, "Referred To Facility ID");

        urgencyCombo = new JComboBox<>(new String[]{"Routine", "Urgent", "Non-urgent"});
        urgencyCombo.setFont(ViewConstants.BODY_FONT);
        urgencyCombo.setBackground(ViewConstants.BACKGROUND);
        addLabeledComponent(contentPanel, "Urgency Level", urgencyCombo);

        reasonArea = addTextAreaField(contentPanel, "Referral Reason");
        clinicalSummaryArea = addTextAreaField(contentPanel, "Clinical Summary");
        investigationsField = addFormField(contentPanel, "Requested Investigations");

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

        ModernButton saveButton = new ModernButton(isEditMode ? "Update Referral" : "Create Referral");
        saveButton.addActionListener(e -> {
            if (validateForm()) {
                submitted = true;
                setVisible(false);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        if (isEditMode && existingReferral != null) {
            populateFields(existingReferral);
        }
    }

    private void populateFields(Referrals.ReferralData referral) {
        patientIdField.setText(referral.patientId());
        referringClinicianField.setText(referral.referringClinicianId());
        referredToClinicianField.setText(referral.referredToClinicianId());
        referringFacilityField.setText(referral.referringFacilityId());
        referredToFacilityField.setText(referral.referredToFacilityId());
        urgencyCombo.setSelectedItem(referral.urgencyLevel());
        reasonArea.setText(referral.referralReason());
        clinicalSummaryArea.setText(referral.clinicalSummary());
        investigationsField.setText(referral.requestedInvestigations());
    }

    private boolean validateForm() {
        boolean valid = validateField(patientIdField);
        if (!validateField(referringClinicianField))
            valid = false;
        if (!validateField(referredToClinicianField))
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

    private JTextArea addTextAreaField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(ViewConstants.BODY_FONT);
        label.setForeground(ViewConstants.MUTED_FOREGROUND);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextArea area = new JTextArea(3, 20);
        area.setBorder(ViewConstants.INPUT_BORDER);
        area.setFont(ViewConstants.BODY_FONT);
        area.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.add(scroll);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        return area;
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

    public String getReferringClinicianId() {
        return referringClinicianField.getText();
    }

    public String getReferredToClinicianId() {
        return referredToClinicianField.getText();
    }

    public String getReferringFacilityId() {
        return referringFacilityField.getText();
    }

    public String getReferredToFacilityId() {
        return referredToFacilityField.getText();
    }

    public String getUrgencyLevel() {
        return (String) urgencyCombo.getSelectedItem();
    }

    public String getReason() {
        return reasonArea.getText();
    }

    public String getClinicalSummary() {
        return clinicalSummaryArea.getText();
    }

    public String getRequestedInvestigations() {
        return investigationsField.getText();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public String getReferralId() {
        return referralId;
    }
}

package views.pages;

import controllers.*;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardPanel extends JPanel {

    private final PatientController patientController;
    private final AppointmentController appointmentController;
    private final PrescriptionController prescriptionController;
    private final ReferralController referralController;
    private final StaffController staffController;

    public DashboardPanel(PatientController patientController,
                          AppointmentController appointmentController,
                          PrescriptionController prescriptionController,
                          ReferralController referralController,
                          StaffController staffController) {
        this.patientController = patientController;
        this.appointmentController = appointmentController;
        this.prescriptionController = prescriptionController;
        this.referralController = referralController;
        this.staffController = staffController;

        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);
        setBorder(ViewConstants.PADDING_BORDER);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ViewConstants.BACKGROUND);

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);

        JLabel subtitleLabel = new JLabel("Overview of hospital activities.");
        subtitleLabel.setFont(ViewConstants.BODY_FONT);
        subtitleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(ViewConstants.BACKGROUND);
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Stats Grid
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20)); // 2 rows, 3 cols, gap 20
        statsPanel.setBackground(ViewConstants.BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        refreshStats(statsPanel);

        add(statsPanel, BorderLayout.CENTER);
    }

    private void refreshStats(JPanel statsPanel) {
        // 1. Total Patients
        int totalPatients = patientController.getAllPatients().size();
        statsPanel.add(createStatCard("Total Patients", String.valueOf(totalPatients), "Registered in system"));

        // 2. Appointments Today
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        long appointmentsToday = appointmentController.getAllAppointments().stream()
                .filter(a -> sdf.format(a.appointmentDate()).equals(today))
                .count();
        statsPanel.add(createStatCard("Appointments Today", String.valueOf(appointmentsToday), "Scheduled for " + today));

        // 3. Active Staff (Clinicians + Admin)
        int totalStaff = staffController.getAllClinicians().size() + staffController.getAllStaff().size();
        statsPanel.add(createStatCard("Total Staff", String.valueOf(totalStaff), "Clinicians & Support"));

        // 4. Pending Referrals
        long pendingReferrals = referralController.getPendingReferrals().size();
        statsPanel.add(createStatCard("Pending Referrals", String.valueOf(pendingReferrals), "Requires attention"));

        // 5. Prescriptions Issued
        long prescriptionsIssued = prescriptionController.getAllPrescriptions().stream()
                .filter(p -> "Issued".equalsIgnoreCase(p.status()))
                .count();
        statsPanel.add(createStatCard("Prescriptions Issued", String.valueOf(prescriptionsIssued), "Active prescriptions"));

        // 6. Urgent Referrals
        long urgentReferrals = referralController.getAllReferrals().stream()
                .filter(r -> "Urgent".equalsIgnoreCase(r.urgencyLevel()))
                .count();
        statsPanel.add(createStatCard("Urgent Referrals", String.valueOf(urgentReferrals), "High priority cases"));
    }

    private JPanel createStatCard(String title, String value, String subtext) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ViewConstants.BACKGROUND);
        card.setBorder(ViewConstants.CARD_BORDER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ViewConstants.BODY_FONT);
        titleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(ViewConstants.HEADER_FONT.deriveFont(28f));
        valueLabel.setForeground(ViewConstants.FOREGROUND);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtextLabel = new JLabel(subtext);
        subtextLabel.setFont(ViewConstants.SMALL_FONT);
        subtextLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
        subtextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtextLabel);

        return card;
    }
}

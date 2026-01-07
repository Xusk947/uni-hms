package views.pages;

import controllers.*;
import views.components.PageContainer;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardPanel extends PageContainer {

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
                super("Dashboard", "Overview of hospital activities.");
                this.patientController = patientController;
                this.appointmentController = appointmentController;
                this.prescriptionController = prescriptionController;
                this.referralController = referralController;
                this.staffController = staffController;

                // Stats "Linear" List (Vertical Stack)
                JPanel statsPanel = new JPanel();
                statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
                statsPanel.setBackground(ViewConstants.BACKGROUND);
                statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

                refreshStats(statsPanel);

                // Wrap in scroll pane
                JScrollPane scrollPane = new JScrollPane(statsPanel);
                scrollPane.setBorder(null);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                setContent(scrollPane);
        }

        private void refreshStats(JPanel statsPanel) {
                // Add vertical spacing at top
                statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

                int totalPatients = patientController.getAllPatients().size();
                addStatCard(statsPanel, "Total Patients", String.valueOf(totalPatients), "Registered in system");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today = sdf.format(new Date());
                long appointmentsToday = appointmentController.getAllAppointments().stream()
                                .filter(a -> sdf.format(a.appointmentDate()).equals(today))
                                .count();
                addStatCard(statsPanel, "Appointments Today", String.valueOf(appointmentsToday),
                                "Scheduled for " + today);

                int totalStaff = staffController.getAllClinicians().size() + staffController.getAllStaff().size();
                addStatCard(statsPanel, "Total Staff", String.valueOf(totalStaff), "Clinicians & Support");

                long pendingReferrals = referralController.getPendingReferrals().size();
                addStatCard(statsPanel, "Pending Referrals", String.valueOf(pendingReferrals), "Requires attention");

                long prescriptionsIssued = prescriptionController.getAllPrescriptions().stream()
                                .filter(p -> "Issued".equalsIgnoreCase(p.status()))
                                .count();
                addStatCard(statsPanel, "Prescriptions Issued", String.valueOf(prescriptionsIssued),
                                "Active prescriptions");

                long urgentReferrals = referralController.getAllReferrals().stream()
                                .filter(r -> "Urgent".equalsIgnoreCase(r.urgencyLevel()))
                                .count();
                addStatCard(statsPanel, "Urgent Referrals", String.valueOf(urgentReferrals), "High priority cases");

                statsPanel.add(Box.createVerticalGlue());
        }

        private void addStatCard(JPanel panel, String title, String value, String subtext) {
                panel.add(createStatCard(title, value, subtext));
                panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        private JPanel createStatCard(String title, String value, String subtext) {
                JPanel card = new JPanel(new BorderLayout(20, 0));
                card.setBackground(ViewConstants.BACKGROUND);
                card.setBorder(ViewConstants.CARD_BORDER);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Text Info (Left)
                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setBackground(ViewConstants.BACKGROUND);

                JLabel titleLabel = new JLabel(title);
                titleLabel.setFont(ViewConstants.BODY_FONT);
                titleLabel.setForeground(ViewConstants.FOREGROUND);
                titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel subtextLabel = new JLabel(subtext);
                subtextLabel.setFont(ViewConstants.SMALL_FONT);
                subtextLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
                subtextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                textPanel.add(titleLabel);
                textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
                textPanel.add(subtextLabel);

                // Value (Right)
                JLabel valueLabel = new JLabel(value);
                valueLabel.setFont(ViewConstants.HEADER_FONT);
                valueLabel.setForeground(ViewConstants.FOREGROUND);

                card.add(textPanel, BorderLayout.CENTER);
                card.add(valueLabel, BorderLayout.EAST);

                return card;
        }
}

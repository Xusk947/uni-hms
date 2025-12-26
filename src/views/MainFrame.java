package views;

import controllers.*;
import services.AppointmentService;
import services.PatientService;
import services.PrescriptionService;
import services.StaffService;
import views.components.SidebarButton;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    // Controllers
    private final PatientController patientController;
    private final AppointmentController appointmentController;
    private final PrescriptionController prescriptionController;
    private final ReferralController referralController;
    private final StaffController staffController;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private ButtonGroup navGroup;

    public MainFrame() {
        // Initialize Services
        PatientService patientService = new PatientService();
        AppointmentService appointmentService = new AppointmentService();
        PrescriptionService prescriptionService = new PrescriptionService();
        StaffService staffService = new StaffService();

        // Initialize Controllers
        this.patientController = new PatientController(patientService);
        this.appointmentController = new AppointmentController(appointmentService);
        this.prescriptionController = new PrescriptionController(prescriptionService);
        this.referralController = new ReferralController(); // Uses Singleton Service internally
        this.staffController = new StaffController(staffService);

        setTitle(ViewConstants.APP_NAME);
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize Components
        initSidebar();
        initContentArea();

        // Add Components
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Load Initial View
        cardLayout.show(contentPanel, "DASHBOARD");
    }

    private void initSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(ViewConstants.BACKGROUND);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ViewConstants.BORDER_COLOR));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));

        // App Logo/Title Area
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));
        headerPanel.setBackground(ViewConstants.BACKGROUND);
        JLabel titleLabel = new JLabel(ViewConstants.APP_NAME);
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);
        headerPanel.add(titleLabel);

        // Add header to sidebar but prevent it from stretching vertically
        headerPanel.setMaximumSize(new Dimension(250, 100));
        sidebar.add(headerPanel);

        // Navigation Buttons
        navGroup = new ButtonGroup();

        // Container for buttons to help with centering/alignment
        JPanel navContainer = new JPanel();
        navContainer.setLayout(new BoxLayout(navContainer, BoxLayout.Y_AXIS));
        navContainer.setBackground(ViewConstants.BACKGROUND);
        navContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        addNavButton(navContainer, "Dashboard", "DASHBOARD");
        addNavButton(navContainer, "Patients", "PATIENTS");
        addNavButton(navContainer, "Appointments", "APPOINTMENTS");
        addNavButton(navContainer, "Prescriptions", "PRESCRIPTIONS");
        addNavButton(navContainer, "Referrals", "REFERRALS");
        addNavButton(navContainer, "Staff", "STAFF");

        sidebar.add(navContainer);
        sidebar.add(Box.createVerticalGlue());
    }

    private void addNavButton(JPanel container, String text, String cardName) {
        SidebarButton btn = new SidebarButton(text, null);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, cardName));

        navGroup.add(btn);
        container.add(btn);
        container.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private void initContentArea() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ViewConstants.BACKGROUND);

        // Register Views
        contentPanel.add(new views.pages.DashboardPanel(patientController, appointmentController, prescriptionController, referralController, staffController), "DASHBOARD");
        contentPanel.add(new views.pages.PatientPanel(patientController), "PATIENTS");
        contentPanel.add(new views.pages.AppointmentPanel(appointmentController), "APPOINTMENTS");
        contentPanel.add(new views.pages.PrescriptionPanel(prescriptionController), "PRESCRIPTIONS");
        contentPanel.add(new views.pages.ReferralPanel(referralController), "REFERRALS");
        contentPanel.add(new views.pages.StaffPanel(staffController), "STAFF");
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ViewConstants.BACKGROUND);

        JLabel label = new JLabel(title);
        label.setFont(ViewConstants.HEADER_FONT);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}

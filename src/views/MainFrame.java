package views;

import controllers.*;
import services.*;
import views.components.ModernButton;
import views.components.SidebarButton;
import views.constants.ViewConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private final AuthenticationService authService;
    private final PatientController patientController;
    private final AppointmentController appointmentController;
    private final PrescriptionController prescriptionController;
    private final ReferralController referralController;
    private final StaffController staffController;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private ButtonGroup navGroup;

    public MainFrame(AuthenticationService authService) {
        this.authService = authService;

        PatientService patientService = new PatientService();
        AppointmentService appointmentService = new AppointmentService();
        PrescriptionService prescriptionService = new PrescriptionService();
        StaffService staffService = new StaffService();

        this.patientController = new PatientController(patientService);
        this.appointmentController = new AppointmentController(appointmentService);
        this.prescriptionController = new PrescriptionController(prescriptionService);
        this.referralController = new ReferralController();
        this.staffController = new StaffController(staffService);

        setTitle(ViewConstants.APP_NAME);
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initSidebar();
        initContentArea();

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "DASHBOARD");
    }

    private void initSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(ViewConstants.BACKGROUND);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ViewConstants.BORDER_COLOR));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));

        headerPanel.setBackground(ViewConstants.BACKGROUND);
        JLabel titleLabel = new JLabel(ViewConstants.APP_NAME);
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);

        headerPanel.add(titleLabel);
        headerPanel.setMaximumSize(new Dimension(250, 80));
        sidebar.add(headerPanel);

        JPanel userInfoPanel = createUserInfoPanel();
        sidebar.add(userInfoPanel);

        navGroup = new ButtonGroup();

        JPanel navContainer = new JPanel();
        navContainer.setLayout(new BoxLayout(navContainer, BoxLayout.Y_AXIS));
        navContainer.setBackground(ViewConstants.BACKGROUND);
        navContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        SidebarButton dashboardBtn = addNavButton(navContainer, "Dashboard", "DASHBOARD");
        dashboardBtn.setSelected(true);

        addNavButton(navContainer, "Patients", "PATIENTS");
        addNavButton(navContainer, "Appointments", "APPOINTMENTS");
        addNavButton(navContainer, "Prescriptions", "PRESCRIPTIONS");
        addNavButton(navContainer, "Referrals", "REFERRALS");
        addNavButton(navContainer, "Staff", "STAFF");

        sidebar.add(navContainer);
        sidebar.add(Box.createVerticalGlue());

        JPanel exitPanel = createExitPanel();
        sidebar.add(exitPanel);
    }

    private JPanel createUserInfoPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        userPanel.setBackground(ViewConstants.BACKGROUND);

        userPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        userPanel.setMaximumSize(new Dimension(250, 70));

        authService.getCurrentUser().ifPresent(user -> {
            JLabel nameLabel = new JLabel(user.getFullName());

            nameLabel.setFont(ViewConstants.BODY_FONT.deriveFont(Font.BOLD));
            nameLabel.setForeground(ViewConstants.FOREGROUND);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel idLabel = new JLabel(user.id() + ", " + user.role());

            idLabel.setFont(ViewConstants.SMALL_FONT);
            idLabel.setForeground(ViewConstants.MUTED_FOREGROUND);
            idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            userPanel.add(nameLabel);
            userPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            userPanel.add(idLabel);
        });

        return userPanel;
    }

    private JPanel createExitPanel() {
        JPanel exitPanel = new JPanel(new BorderLayout());
        exitPanel.setBackground(ViewConstants.BACKGROUND);

        exitPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        exitPanel.setMaximumSize(new Dimension(400, 55));

        ModernButton exitButton = new ModernButton("Exit", ViewConstants.DESTRUCTIVE, Color.WHITE);
        exitButton.setPreferredSize(new Dimension(0, 40));

        exitButton.addActionListener(e -> {
            authService.logout();
            dispose();
            new LoginFrame().setVisible(true);
        });

        exitPanel.add(exitButton, BorderLayout.CENTER);
        return exitPanel;
    }

    private SidebarButton addNavButton(JPanel container, String text, String cardName) {
        SidebarButton btn = new SidebarButton(text, null);

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, cardName));

        navGroup.add(btn);
        container.add(btn);

        container.add(Box.createRigidArea(new Dimension(0, 8)));
        return btn;
    }

    private void initContentArea() {
        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ViewConstants.BACKGROUND);

        contentPanel.add(new views.pages.DashboardPanel(patientController, appointmentController,
                prescriptionController, referralController, staffController), "DASHBOARD");
        contentPanel.add(new views.pages.PatientPanel(patientController), "PATIENTS");
        contentPanel.add(new views.pages.AppointmentPanel(appointmentController), "APPOINTMENTS");
        contentPanel.add(new views.pages.PrescriptionPanel(prescriptionController), "PRESCRIPTIONS");
        contentPanel.add(new views.pages.ReferralPanel(referralController), "REFERRALS");
        contentPanel.add(new views.pages.StaffPanel(staffController), "STAFF");
    }
}

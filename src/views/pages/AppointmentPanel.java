package views.pages;

import controllers.AppointmentController;
import views.constants.ViewConstants;
import views.forms.AppointmentForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentPanel extends JPanel {

    private final AppointmentController appointmentController;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentPanel(AppointmentController appointmentController) {
        this.appointmentController = appointmentController;
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);
        setBorder(ViewConstants.PADDING_BORDER);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ViewConstants.BACKGROUND);

        JLabel titleLabel = new JLabel("Appointments");
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);

        JLabel subtitleLabel = new JLabel("Manage scheduled appointments.");
        subtitleLabel.setFont(ViewConstants.BODY_FONT);
        subtitleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(ViewConstants.BACKGROUND);
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);

        views.components.ModernButton addButton = new views.components.ModernButton("New Appointment");
        addButton.addActionListener(e -> openAddAppointmentForm());

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonContainer.setBackground(ViewConstants.BACKGROUND);
        buttonContainer.add(addButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(buttonContainer, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table Area
        String[] columns = {"ID", "Patient", "Clinician", "Date", "Time", "Type", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(tableModel);
        styleTable(appointmentTable);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.getViewport().setBackground(ViewConstants.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(ViewConstants.BORDER_COLOR));

        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void openAddAppointmentForm() {
        AppointmentForm form = new AppointmentForm(SwingUtilities.getWindowAncestor(this));
        form.setVisible(true);

        if (form.isSubmitted()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm");

                Date date = sdf.parse(form.getDate());
                Date time = timeFmt.parse(form.getTime());

                String appointmentId = "A" + System.currentTimeMillis();

                appointmentController.createAppointment(
                        appointmentId,
                        form.getPatientId(),
                        form.getClinicianId(),
                        form.getFacilityId(),
                        date,
                        time,
                        form.getDuration(),
                        form.getAppointmentType(),
                        form.getReason()
                );

                loadData();
                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error booking appointment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setGridColor(ViewConstants.BORDER_COLOR);
        table.setFont(ViewConstants.BODY_FONT);
        table.setBackground(ViewConstants.BACKGROUND);

        table.getTableHeader().setFont(ViewConstants.SUBHEADER_FONT.deriveFont(14f));
        table.getTableHeader().setBackground(ViewConstants.TABLE_HEADER_BG);
        table.getTableHeader().setForeground(ViewConstants.MUTED_FOREGROUND);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ViewConstants.BORDER_COLOR));

        table.setSelectionBackground(ViewConstants.TABLE_SELECTION_BG);
        table.setSelectionForeground(ViewConstants.FOREGROUND);
        table.setFocusable(false);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        var appointments = appointmentController.getAllAppointments();

        for (var a : appointments) {
            tableModel.addRow(new Object[]{
                    a.appointmentId(),
                    a.patientId(),
                    a.clinicianId(),
                    services.Const.DATE_FORMAT.format(a.appointmentDate()),
                    services.Const.TIME_FORMAT.format(a.appointmentTime()),
                    a.appointmentType(),
                    a.status()
            });
        }
    }
}

package views.pages;

import controllers.AppointmentController;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.forms.AppointmentForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentPanel extends PageContainer {

    private final AppointmentController appointmentController;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentPanel(AppointmentController appointmentController) {
        super("Appointments", "Manage scheduled appointments.");
        this.appointmentController = appointmentController;

        ModernButton addButton = new ModernButton("New Appointment");
        addButton.addActionListener(e -> openAddAppointmentForm());
        addHeaderAction(addButton);

        String[] columns = { "ID", "Patient", "Clinician", "Date", "Time", "Type", "Status" };
        tableModel = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(tableModel);
        TableStyler.applyStyle(appointmentTable);

        setContent(TableStyler.wrapInScrollPane(appointmentTable));
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
                        form.getReason());

                loadData();
                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error booking appointment: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        var appointments = appointmentController.getAllAppointments();

        for (var a : appointments) {
            tableModel.addRow(new Object[] {
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

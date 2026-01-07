package views.pages;

import controllers.AppointmentController;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.constants.ViewConstants;
import views.forms.AppointmentForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentPanel extends PageContainer {

    private final AppointmentController appointmentController;
    private final JTable appointmentTable;
    private final DefaultTableModel tableModel;

    public AppointmentPanel(AppointmentController appointmentController) {
        super("Appointments", "Manage scheduled appointments.");
        this.appointmentController = appointmentController;

        ModernButton addButton = new ModernButton("New Appointment");
        addButton.addActionListener(e -> openAddAppointmentForm());
        addHeaderAction(addButton);

        String[] columns = {"ID", "Patient", "Clinician", "Date", "Time", "Type", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        appointmentTable = new JTable(tableModel);
        TableStyler.applyStyle(appointmentTable);

        appointmentTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        appointmentTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());
        appointmentTable.getColumnModel().getColumn(7).setPreferredWidth(150);

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
            tableModel.addRow(new Object[]{
                    a.appointmentId(),
                    a.patientId(),
                    a.clinicianId(),
                    services.Const.DATE_FORMAT.format(a.appointmentDate()),
                    services.Const.TIME_FORMAT.format(a.appointmentTime()),
                    a.appointmentType(),
                    a.status(),
                    "Actions"
            });
        }
    }

    private void openEditAppointmentForm(String appointmentId) {
        var appointment = appointmentController.findAppointment(appointmentId);
        if (appointment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Appointment not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AppointmentForm form = new AppointmentForm(SwingUtilities.getWindowAncestor(this), appointment.get());
        form.setVisible(true);

        if (form.isSubmitted()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm");

                Date date = sdf.parse(form.getDate());
                Date time = timeFmt.parse(form.getTime());

                appointmentController.updateAppointment(
                        form.getAppointmentId(),
                        form.getPatientId(),
                        form.getClinicianId(),
                        form.getFacilityId(),
                        date,
                        time,
                        form.getDuration(),
                        form.getAppointmentType(),
                        appointment.get().status(),
                        form.getReason(),
                        appointment.get().notes());

                loadData();
                JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating appointment: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void deleteAppointment(String appointmentId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this appointment?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                appointmentController.deleteAppointment(appointmentId);
                loadData();
                JOptionPane.showMessageDialog(this, "Appointment deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting appointment: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final ModernButton editButton;
        private final ModernButton deleteButton;

        ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setOpaque(true);
            setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new java.awt.Dimension(60, 25));

            deleteButton = new ModernButton("Delete", new java.awt.Color(220, 53, 69), java.awt.Color.WHITE);
            deleteButton.setPreferredSize(new java.awt.Dimension(60, 25));

            add(editButton);
            add(deleteButton);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel;
        private final ModernButton editButton;
        private final ModernButton deleteButton;
        private String appointmentId;

        ButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            panel.setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new java.awt.Dimension(60, 25));
            editButton.addActionListener(e -> {
                fireEditingStopped();
                openEditAppointmentForm(appointmentId);
            });

            deleteButton = new ModernButton("Delete", new java.awt.Color(220, 53, 69), java.awt.Color.WHITE);
            deleteButton.setPreferredSize(new java.awt.Dimension(60, 25));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteAppointment(appointmentId);
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                                                              boolean isSelected, int row, int column) {
            appointmentId = (String) table.getValueAt(row, 0);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
}

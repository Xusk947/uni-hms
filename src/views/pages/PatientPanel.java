package views.pages;

import controllers.PatientController;
import parser.Patients;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.constants.ViewConstants;
import views.forms.PatientForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientPanel extends PageContainer {

    private final PatientController patientController;
    private final JTable patientTable;
    private final DefaultTableModel tableModel;

    public PatientPanel(PatientController patientController) {
        super("Patients", "Manage registered patients.");
        this.patientController = patientController;

        ModernButton addButton = new ModernButton("Add Patient");
        addButton.addActionListener(e -> openNewPatientForm());
        addHeaderAction(addButton);

        JTextField searchField = new JTextField(20);
        searchField.setBorder(ViewConstants.INPUT_BORDER);
        ModernButton searchButton = new ModernButton("Search", ViewConstants.SECONDARY,
                ViewConstants.SECONDARY_FOREGROUND);
        searchButton.addActionListener(e -> searchPatients(searchField.getText()));

        addHeaderAction(searchField);
        addHeaderAction(searchButton);

        String[] columns = {"ID", "Name", "DOB", "NHS Number", "Gender", "Phone", "Email", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        patientTable = new JTable(tableModel);
        TableStyler.applyStyle(patientTable);

        patientTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        patientTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());
        patientTable.getColumnModel().getColumn(7).setPreferredWidth(150);

        setContent(TableStyler.wrapInScrollPane(patientTable));
        loadData();
    }

    private void openNewPatientForm() {
        PatientForm form = new PatientForm(SwingUtilities.getWindowAncestor(this));
        form.setVisible(true);

        if (form.isSubmitted()) {
            try {
                String patientId = "P" + System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dob = sdf.parse(form.getDob());

                patientController.registerPatient(
                        patientId,
                        form.getFirstName(),
                        form.getLastName(),
                        dob,
                        form.getNhsNumber(),
                        form.getGender(),
                        form.getPhone(),
                        form.getEmail(),
                        form.getAddress(),
                        form.getPostcode(),
                        form.getEmergencyContact(),
                        form.getEmergencyPhone(),
                        form.getGpSurgery());

                loadData();
                JOptionPane.showMessageDialog(this, "Patient registered successfully!");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditPatientForm(String patientId) {
        var patient = patientController.findPatient(patientId);
        if (patient.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PatientForm form = new PatientForm(SwingUtilities.getWindowAncestor(this), patient.get());
        form.setVisible(true);

        if (form.isSubmitted()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dob = sdf.parse(form.getDob());

                Patients.PatientData updatedData = new Patients.PatientData(
                        patientId,
                        form.getFirstName(),
                        form.getLastName(),
                        dob,
                        form.getNhsNumber(),
                        form.getGender(),
                        form.getPhone(),
                        form.getEmail(),
                        form.getAddress(),
                        form.getPostcode(),
                        form.getEmergencyContact(),
                        form.getEmergencyPhone(),
                        patient.get().registrationDate(),
                        form.getGpSurgery());

                patientController.updatePatient(patientId, updatedData);
                loadData();
                JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deletePatient(String patientId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this patient?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                patientController.deletePatient(patientId);
                loadData();
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting patient: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        populateTable(patientController.getAllPatients());
    }

    private void searchPatients(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        populateTable(patientController.searchPatients(query));
    }

    private void populateTable(List<Patients.PatientData> patients) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (var p : patients) {
            tableModel.addRow(new Object[]{
                    p.patientId(),
                    p.firstName() + " " + p.lastName(),
                    dateFormat.format(p.dateOfBirth()),
                    p.nhsNumber(),
                    p.gender(),
                    p.phoneNumber(),
                    p.email(),
                    "Actions"
            });
        }
    }

    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final ModernButton editButton;
        private final ModernButton deleteButton;

        ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setOpaque(true);
            setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new Dimension(60, 25));

            deleteButton = new ModernButton("Delete", new Color(220, 53, 69), Color.WHITE);
            deleteButton.setPreferredSize(new Dimension(60, 25));

            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private final ModernButton editButton;
        private final ModernButton deleteButton;
        private String patientId;

        ButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            panel.setBackground(ViewConstants.BACKGROUND);

            editButton = new ModernButton("Edit", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
            editButton.setPreferredSize(new Dimension(60, 25));
            editButton.addActionListener(e -> {
                fireEditingStopped();
                openEditPatientForm(patientId);
            });

            deleteButton = new ModernButton("Delete", new Color(220, 53, 69), Color.WHITE);
            deleteButton.setPreferredSize(new Dimension(60, 25));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deletePatient(patientId);
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            patientId = (String) table.getValueAt(row, 0);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }
}

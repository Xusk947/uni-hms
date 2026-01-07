package views.pages;

import controllers.PatientController;
import utils.parser.Patients;
import views.components.ModernButton;
import views.components.PageContainer;
import views.components.TableStyler;
import views.constants.ViewConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class PatientPanel extends PageContainer {

    private final PatientController patientController;
    private final JTable patientTable;
    private final DefaultTableModel tableModel;

    public PatientPanel(PatientController patientController) {
        super("Patients", "Manage registered patients.");
        this.patientController = patientController;

        JTextField searchField = new JTextField(20);
        searchField.setBorder(ViewConstants.INPUT_BORDER);
        ModernButton searchButton = new ModernButton("Search", ViewConstants.SECONDARY,
                ViewConstants.SECONDARY_FOREGROUND);
        searchButton.addActionListener(e -> searchPatients(searchField.getText()));

        addHeaderAction(searchField);
        addHeaderAction(searchButton);

        String[] columns = {"ID", "Name", "DOB", "NHS Number", "Gender", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        patientTable = new JTable(tableModel);
        TableStyler.applyStyle(patientTable);

        setContent(TableStyler.wrapInScrollPane(patientTable));
        loadData();
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
                    p.email()
            });
        }
    }
}

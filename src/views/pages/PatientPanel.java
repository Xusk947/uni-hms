package views.pages;

import controllers.PatientController;
import utils.parser.Patients;
import views.constants.ViewConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class PatientPanel extends JPanel {

    private final PatientController patientController;
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public PatientPanel(PatientController patientController) {
        this.patientController = patientController;
        setLayout(new BorderLayout());
        setBackground(ViewConstants.BACKGROUND);
        setBorder(ViewConstants.PADDING_BORDER);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ViewConstants.BACKGROUND);

        JLabel titleLabel = new JLabel("Patients");
        titleLabel.setFont(ViewConstants.HEADER_FONT);
        titleLabel.setForeground(ViewConstants.FOREGROUND);

        JLabel subtitleLabel = new JLabel("Manage registered patients.");
        subtitleLabel.setFont(ViewConstants.BODY_FONT);
        subtitleLabel.setForeground(ViewConstants.MUTED_FOREGROUND);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(ViewConstants.BACKGROUND);
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(ViewConstants.BACKGROUND);
        JTextField searchField = new JTextField(20);
        searchField.setBorder(ViewConstants.INPUT_BORDER);
        views.components.ModernButton searchButton = new views.components.ModernButton("Search", ViewConstants.SECONDARY, ViewConstants.SECONDARY_FOREGROUND);
        searchButton.addActionListener(e -> searchPatients(searchField.getText()));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table Area
        String[] columns = {"ID", "Name", "DOB", "NHS Number", "Gender", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        patientTable = new JTable(tableModel);
        styleTable(patientTable);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.getViewport().setBackground(ViewConstants.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(ViewConstants.BORDER_COLOR));

        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setFont(ViewConstants.BODY_FONT);
        btn.setBackground(ViewConstants.SECONDARY);
        btn.setForeground(ViewConstants.SECONDARY_FOREGROUND);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        var patients = patientController.getAllPatients();
        populateTable(patients);
    }

    private void searchPatients(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadData();
            return;
        }
        var patients = patientController.searchPatients(query);
        tableModel.setRowCount(0);
        populateTable(patients);
    }

    private void populateTable(java.util.List<Patients.PatientData> patients) {
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

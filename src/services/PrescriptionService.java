package services;

import parser.CsvParser;
import parser.Prescriptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static services.Const.PRESCRIPTIONS_FILE;

public final class PrescriptionService {
    private List<Prescriptions.PrescriptionData> prescriptions;

    public PrescriptionService() {
        loadPrescriptions();
    }

    private void loadPrescriptions() {
        try {
            prescriptions = Prescriptions.parse(PRESCRIPTIONS_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prescriptions", e);
        }
    }

    public List<Prescriptions.PrescriptionData> getAllPrescriptions() {
        return List.copyOf(prescriptions);
    }

    public List<Prescriptions.PrescriptionData> getPrescriptionsByPatient(String patientId) {
        return prescriptions.stream()
                .filter(p -> p.patientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public Optional<Prescriptions.PrescriptionData> findPrescription(String prescriptionId) {
        return prescriptions.stream()
                .filter(p -> p.prescriptionId().equals(prescriptionId))
                .findFirst();
    }

    public void createPrescription(String prescriptionId, String patientId, String clinicianId,
                                   String appointmentId, String medicationName, String dosage,
                                   String frequency, int durationDays, int quantity,
                                   String instructions, String pharmacyName) {
        Date now = new Date();
        Prescriptions.PrescriptionData newPrescription = new Prescriptions.PrescriptionData(
                prescriptionId, patientId, clinicianId, appointmentId,
                now, medicationName, dosage, frequency,
                durationDays, quantity, instructions, pharmacyName,
                "Issued", now, null
        );

        try {
            Files.writeString(PRESCRIPTIONS_FILE, Prescriptions.toCsvLine(newPrescription),
                    StandardOpenOption.APPEND);
            loadPrescriptions();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create prescription", e);
        }
    }

    public void renewPrescription(String prescriptionId) {
        Optional<Prescriptions.PrescriptionData> existing = findPrescription(prescriptionId);
        if (existing.isPresent()) {
            Prescriptions.PrescriptionData old = existing.get();
            String newId = "RX" + System.currentTimeMillis();
            createPrescription(newId, old.patientId(), old.clinicianId(), old.appointmentId(),
                    old.medicationName(), old.dosage(), old.frequency(), old.durationDays(),
                    old.quantity(), old.instructions(), old.pharmacyName());
        }
    }

    public void deletePrescription(String prescriptionId) {
        try {
            List<String> lines = Files.readAllLines(PRESCRIPTIONS_FILE);
            List<String> updatedLines = lines.stream()
                    .filter(line -> !line.startsWith(prescriptionId + ","))
                    .collect(Collectors.toList());

            Files.write(PRESCRIPTIONS_FILE, updatedLines);
            loadPrescriptions();
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete prescription", e);
        }
    }

    public void updatePrescription(String prescriptionId, String patientId, String clinicianId,
                                   String appointmentId, String medicationName, String dosage,
                                   String frequency, int durationDays, int quantity,
                                   String instructions, String pharmacyName, String status) {
        try {
            List<String> lines = Files.readAllLines(PRESCRIPTIONS_FILE);
            Date now = new Date();

            List<String> updatedLines = lines.stream()
                    .map(line -> {
                        if (line.startsWith(prescriptionId + ",")) {
                            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                            Date prescriptionDate = parts.length > 4 ? CsvParser.parseDate(parts[4]) : now;
                            Date issueDate = parts.length > 13 ? CsvParser.parseDate(parts[13]) : now;
                            Date collectionDate = parts.length > 14 ? CsvParser.parseDate(parts[14]) : null;

                            Prescriptions.PrescriptionData updated = new Prescriptions.PrescriptionData(
                                    prescriptionId, patientId, clinicianId, appointmentId,
                                    prescriptionDate, medicationName, dosage, frequency,
                                    durationDays, quantity, instructions, pharmacyName,
                                    status, issueDate, collectionDate
                            );
                            return Prescriptions.toCsvLine(updated).trim();
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(PRESCRIPTIONS_FILE, updatedLines);
            loadPrescriptions();
        } catch (IOException e) {
            throw new RuntimeException("Failed to update prescription", e);
        }
    }
}

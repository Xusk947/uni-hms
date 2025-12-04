package services;

import utils.parser.Prescriptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static services.Const.*;

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
}

package controllers;

import services.PrescriptionService;
import utils.parser.Prescriptions;

import java.util.List;
import java.util.Optional;

public final class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    public List<Prescriptions.PrescriptionData> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    public List<Prescriptions.PrescriptionData> getPrescriptionsByPatient(String patientId) {
        return prescriptionService.getPrescriptionsByPatient(patientId);
    }

    public Optional<Prescriptions.PrescriptionData> findPrescription(String prescriptionId) {
        return prescriptionService.findPrescription(prescriptionId);
    }

    public void createPrescription(String prescriptionId, String patientId, String clinicianId, String appointmentId, String medicationName, String dosage, String frequency, int durationDays, int quantity, String instructions, String pharmacyName) {
        prescriptionService.createPrescription(prescriptionId, patientId, clinicianId, appointmentId, medicationName, dosage, frequency, durationDays, quantity, instructions, pharmacyName);
    }

    public void renewPrescription(String prescriptionId) {
        prescriptionService.renewPrescription(prescriptionId);
    }

    public List<Prescriptions.PrescriptionData> getActivePrescriptions(String patientId) {
        return getPrescriptionsByPatient(patientId).stream().filter(p -> p.status().equals("Issued") || p.status().equals("Active")).toList();
    }

    public void deletePrescription(String prescriptionId) {
        prescriptionService.deletePrescription(prescriptionId);
    }

    public void updatePrescription(String prescriptionId, String patientId, String clinicianId,
                                   String appointmentId, String medicationName, String dosage,
                                   String frequency, int durationDays, int quantity,
                                   String instructions, String pharmacyName, String status) {
        prescriptionService.updatePrescription(prescriptionId, patientId, clinicianId,
                appointmentId, medicationName, dosage, frequency, durationDays, quantity,
                instructions, pharmacyName, status);
    }
}

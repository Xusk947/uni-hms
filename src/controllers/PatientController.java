package controllers;

import services.PatientService;
import parser.Patients;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    public List<Patients.PatientData> getAllPatients() {
        return patientService.getAllPatients();
    }

    public Optional<Patients.PatientData> findPatient(String patientId) {
        return patientService.findPatient(patientId);
    }

    public void registerPatient(String patientId, String firstName, String lastName, Date birthDate, String nhsNumber, String gender, String phone, String email, String address, String postcode, String emergencyContact, String emergencyPhone, String gpSurgeryId) {
        patientService.registerPatient(patientId, firstName, lastName, birthDate, nhsNumber, gender, phone, email, address, postcode, emergencyContact, emergencyPhone, gpSurgeryId);
    }

    public void updatePatient(String patientId, Patients.PatientData updatedData) {
        patientService.updatePatient(patientId, updatedData);
    }

    public void deletePatient(String patientId) {
        patientService.deletePatient(patientId);
    }

    public List<Patients.PatientData> searchPatients(String searchTerm) {
        return getAllPatients().stream().filter(p -> p.firstName().toLowerCase().contains(searchTerm.toLowerCase()) || p.lastName().toLowerCase().contains(searchTerm.toLowerCase()) || p.patientId().toLowerCase().contains(searchTerm.toLowerCase()) || p.nhsNumber().contains(searchTerm)).toList();
    }
}

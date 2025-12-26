package services;

import utils.parser.Patients;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class PatientService {
    private List<Patients.PatientData> patients;

    public PatientService() {
        loadPatients();
    }

    private void loadPatients() {
        try {
            patients = Patients.parse(Const.PATIENTS_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load patients", e);
        }
    }

    public List<Patients.PatientData> getAllPatients() {
        return List.copyOf(patients);
    }

    public Optional<Patients.PatientData> findPatient(String patientId) {
        return patients.stream()
                .filter(p -> p.patientId().equals(patientId))
                .findFirst();
    }

    public void registerPatient(String patientId, String firstName, String lastName,
                                Date birthDate, String nhsNumber, String gender,
                                String phone, String email, String address, String postcode,
                                String emergencyContact, String emergencyPhone, String gpSurgeryId) {
        Patients.PatientData newPatient = new Patients.PatientData(
                patientId, firstName, lastName, birthDate, nhsNumber, gender,
                phone, email, address, postcode, emergencyContact, emergencyPhone,
                new Date(), gpSurgeryId
        );

        try {
            Files.writeString(Const.PATIENTS_FILE, Patients.toCsvLine(newPatient),
                    StandardOpenOption.APPEND);
            loadPatients();
        } catch (IOException e) {
            throw new RuntimeException("Failed to register patient", e);
        }
    }

    public void updatePatient(String patientId, Patients.PatientData updatedData) {
        loadPatients();
    }
}

package controllers;

import parser.Patients;

import java.util.Date;
import java.util.List;

public final class AdministrativeController {
    private final PatientController patientController;
    private final AppointmentController appointmentController;

    public AdministrativeController(PatientController patientController,
                                    AppointmentController appointmentController) {
        this.patientController = patientController;
        this.appointmentController = appointmentController;
    }

    public void registerPatient(String patientId, String firstName, String lastName,
                                Date birthDate, String nhsNumber, String gender,
                                String phone, String email, String address, String postcode,
                                String emergencyContact, String emergencyPhone, String gpSurgeryId) {
        patientController.registerPatient(patientId, firstName, lastName, birthDate,
                nhsNumber, gender, phone, email, address, postcode,
                emergencyContact, emergencyPhone, gpSurgeryId);
    }

    public void manageAppointment(String appointmentId, String patientId, String clinicianId,
                                  String facilityId, Date appointmentDate, Date appointmentTime,
                                  int duration, String type, String reason) {
        appointmentController.createAppointment(appointmentId, patientId, clinicianId,
                facilityId, appointmentDate, appointmentTime, duration, type, reason);
    }

    public void cancelAppointment(String appointmentId) {
        appointmentController.cancelAppointment(appointmentId);
    }

    public void updateAppointmentStatus(String appointmentId, String newStatus) {
        appointmentController.updateAppointmentStatus(appointmentId, newStatus);
    }

    public List<Patients.PatientData> searchPatients(String searchTerm) {
        return patientController.searchPatients(searchTerm);
    }

    public List<Patients.PatientData> getAllPatients() {
        return patientController.getAllPatients();
    }
}

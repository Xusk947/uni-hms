package controllers;

import utils.parser.Appointment;
import utils.parser.Patients;
import utils.parser.Prescriptions;
import utils.parser.Referrals;

import java.util.List;

public final class ClinicianController {
    private final AppointmentController appointmentController;
    private final PrescriptionController prescriptionController;
    private final ReferralController referralController;
    private final PatientController patientController;

    public ClinicianController(AppointmentController appointmentController,
                               PrescriptionController prescriptionController,
                               ReferralController referralController,
                               PatientController patientController) {
        this.appointmentController = appointmentController;
        this.prescriptionController = prescriptionController;
        this.referralController = referralController;
        this.patientController = patientController;
    }

    public List<Appointment.AppointmentData> viewSchedule(String clinicianId) {
        return appointmentController.getAppointmentsByClinician(clinicianId);
    }

    public Patients.PatientData viewPatientRecord(String patientId) {
        return patientController.findPatient(patientId).orElse(null);
    }

    public void createPrescription(String prescriptionId, String patientId, String clinicianId,
                                   String appointmentId, String medicationName, String dosage,
                                   String frequency, int durationDays, int quantity,
                                   String instructions, String pharmacyName) {
        prescriptionController.createPrescription(prescriptionId, patientId, clinicianId,
                appointmentId, medicationName, dosage, frequency, durationDays,
                quantity, instructions, pharmacyName);
    }

    public void createReferral(String referralId, String patientId, String referringClinicianId,
                               String referredToClinicianId, String referringFacilityId,
                               String referredToFacilityId, String urgencyLevel, String reason,
                               String clinicalSummary, String requestedInvestigations) {
        referralController.createReferral(referralId, patientId, referringClinicianId,
                referredToClinicianId, referringFacilityId, referredToFacilityId,
                urgencyLevel, reason, clinicalSummary, requestedInvestigations);
    }

    public void accessReferral(String referralId) {
        referralController.connectReferral(referralId);
    }

    public List<Prescriptions.PrescriptionData> getPatientPrescriptions(String patientId) {
        return prescriptionController.getPrescriptionsByPatient(patientId);
    }

    public List<Referrals.ReferralData> getPatientReferrals(String patientId) {
        return referralController.getReferralsByPatient(patientId);
    }

    public List<Appointment.AppointmentData> getPatientAppointments(String patientId) {
        return appointmentController.getAppointmentsByPatient(patientId);
    }
}

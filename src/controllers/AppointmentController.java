package controllers;

import services.AppointmentService;
import utils.parser.Appointment;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public List<Appointment.AppointmentData> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    public List<Appointment.AppointmentData> getAppointmentsByPatient(String patientId) {
        return appointmentService.getAppointmentsByPatient(patientId);
    }

    public List<Appointment.AppointmentData> getAppointmentsByClinician(String clinicianId) {
        return appointmentService.getAppointmentsByClinician(clinicianId);
    }

    public Optional<Appointment.AppointmentData> findAppointment(String appointmentId) {
        return appointmentService.findAppointment(appointmentId);
    }

    public void createAppointment(String appointmentId, String patientId, String clinicianId,
                                  String facilityId, Date appointmentDate, Date appointmentTime,
                                  int duration, String type, String reason) {
        appointmentService.createAppointment(appointmentId, patientId, clinicianId,
                facilityId, appointmentDate, appointmentTime, duration, type, reason);
    }

    public void cancelAppointment(String appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
    }

    public void updateAppointmentStatus(String appointmentId, String newStatus) {
        appointmentService.updateAppointmentStatus(appointmentId, newStatus);
    }

    public List<Appointment.AppointmentData> viewSchedule(String clinicianId) {
        return appointmentService.viewSchedule(clinicianId);
    }

    public List<Appointment.AppointmentData> getUpcomingAppointments(String patientId) {
        return getAppointmentsByPatient(patientId).stream()
                .filter(a -> a.status().equals("Scheduled"))
                .toList();
    }
}

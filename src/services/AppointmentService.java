package services;

import utils.parser.Appointment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static services.Const.APPOINTMENTS_FILE;

public final class AppointmentService {

    private List<Appointment.AppointmentData> appointments;

    public AppointmentService() {
        loadAppointments();
    }

    private void loadAppointments() {
        try {
            appointments = Appointment.parse(APPOINTMENTS_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load appointments", e);
        }
    }

    public List<Appointment.AppointmentData> getAllAppointments() {
        return List.copyOf(appointments);
    }

    public List<Appointment.AppointmentData> getAppointmentsByPatient(String patientId) {
        return appointments.stream()
                .filter(a -> a.patientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment.AppointmentData> getAppointmentsByClinician(String clinicianId) {
        return appointments.stream()
                .filter(a -> a.clinicianId().equals(clinicianId))
                .collect(Collectors.toList());
    }

    public Optional<Appointment.AppointmentData> findAppointment(String appointmentId) {
        return appointments.stream()
                .filter(a -> a.appointmentId().equals(appointmentId))
                .findFirst();
    }

    public void createAppointment(String appointmentId, String patientId, String clinicianId,
                                  String facilityId, Date appointmentDate, Date appointmentTime,
                                  int duration, String type, String reason) {
        Date now = new Date();
        Appointment.AppointmentData newAppointment = new Appointment.AppointmentData(
                appointmentId, patientId, clinicianId, facilityId,
                appointmentDate, appointmentTime, duration, type,
                "Scheduled", reason, "", now, now
        );

        try {
            Files.writeString(APPOINTMENTS_FILE, Appointment.toCsvLine(newAppointment),
                    StandardOpenOption.APPEND);
            loadAppointments();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create appointment", e);
        }
    }

    public void cancelAppointment(String appointmentId) {
        try {
            List<String> lines = Files.readAllLines(APPOINTMENTS_FILE);
            List<String> updatedLines = lines.stream()
                    .map(line -> {
                        if (line.startsWith(appointmentId + ",")) {
                            return line.replaceFirst(",Scheduled,", ",Cancelled,");
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(APPOINTMENTS_FILE, updatedLines);
            loadAppointments();
        } catch (IOException e) {
            throw new RuntimeException("Failed to cancel appointment", e);
        }
    }

    public void updateAppointmentStatus(String appointmentId, String newStatus) {
        try {
            List<String> lines = Files.readAllLines(APPOINTMENTS_FILE);
            List<String> updatedLines = lines.stream()
                    .map(line -> {
                        if (line.startsWith(appointmentId + ",")) {
                            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                            if (parts.length > 8) {
                                parts[8] = newStatus;
                                return String.join(",", parts);
                            }
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(APPOINTMENTS_FILE, updatedLines);
            loadAppointments();
        } catch (IOException e) {
            throw new RuntimeException("Failed to update appointment status", e);
        }
    }

    public List<Appointment.AppointmentData> viewSchedule(String clinicianId) {
        return getAppointmentsByClinician(clinicianId);
    }

    public void deleteAppointment(String appointmentId) {
        try {
            List<String> lines = Files.readAllLines(APPOINTMENTS_FILE);
            List<String> updatedLines = lines.stream()
                    .filter(line -> !line.startsWith(appointmentId + ","))
                    .collect(Collectors.toList());

            Files.write(APPOINTMENTS_FILE, updatedLines);
            loadAppointments();
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete appointment", e);
        }
    }

    public void updateAppointment(String appointmentId, String patientId, String clinicianId,
                                  String facilityId, Date appointmentDate, Date appointmentTime,
                                  int duration, String type, String status, String reason, String notes) {
        try {
            List<String> lines = Files.readAllLines(APPOINTMENTS_FILE);
            Date now = new Date();

            List<String> updatedLines = lines.stream()
                    .map(line -> {
                        if (line.startsWith(appointmentId + ",")) {
                            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                            Date createdDate = parts.length > 11 ? utils.parser.CsvParser.parseDate(parts[11]) : now;

                            Appointment.AppointmentData updated = new Appointment.AppointmentData(
                                    appointmentId, patientId, clinicianId, facilityId,
                                    appointmentDate, appointmentTime, duration, type,
                                    status, reason, notes, createdDate, now
                            );
                            return Appointment.toCsvLine(updated).trim();
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(APPOINTMENTS_FILE, updatedLines);
            loadAppointments();
        } catch (IOException e) {
            throw new RuntimeException("Failed to update appointment", e);
        }
    }
}

package parser;

import java.nio.file.Path;

public class TestRunner {
    public static void main(String[] args) {
        // just for case, when we have an error on out, it means that something went wrong while we reading files
        try {
            var appointment = Appointment.parse(Path.of("appointments.csv"));
            var clinician = Clinician.parse(Path.of("clinicians.csv"));
            var facility = Facilities.parse(Path.of("facilities.csv"));
            var prescription = Prescriptions.parse(Path.of("prescriptions.csv"));
            var referral = Referrals.parse(Path.of("referrals.csv"));
            var staff = Staff.parse(Path.of("staff.csv"));
            var patient = Patients.parse(Path.of("patients.csv"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}

package models;

import java.time.LocalDate;

public record Patient(String patientID, String name, LocalDate birthDate) {

    public void createAppointment() {
    }

    public void cancelAppointment() {
    }
}

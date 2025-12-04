package models;

import java.util.Date;

public record Patient(String patientID, String name, Date birthDate) {

    public void createAppointment() {
    }

    public void cancelAppointment() {
    }
}

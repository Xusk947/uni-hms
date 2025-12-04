package models;

public record Prescription(String prescriptionID, String patientDetails, String condition, String drug) {

    public void renewPrescription() {
    }
}

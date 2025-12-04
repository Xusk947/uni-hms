package models;

public final class Nurse extends HealthcareProfessional {
    private final String qualification;

    public Nurse(String staffID, String name, String department, String qualification) {
        super(staffID, name, department);
        this.qualification = qualification;
    }

    public String getQualification() {
        return qualification;
    }

    @Override
    public void login() {
    }

    @Override
    public void createPrescription() {
    }

    @Override
    public void viewPatientRecord() {
    }

    public void updatePatientVitals() {
    }
}

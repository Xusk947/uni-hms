package models;

public final class SpecialistDoctor extends HealthcareProfessional {
    private final String speciality;

    public SpecialistDoctor(String staffID, String name, String department, String speciality) {
        super(staffID, name, department);
        this.speciality = speciality;
    }

    public String getSpeciality() {
        return speciality;
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

    public void accessReferral() {
    }
}

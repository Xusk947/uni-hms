package models;

public final class GeneralPractitioner extends HealthcareProfessional {
    private boolean remote;

    public GeneralPractitioner(String staffID, String name, String department, boolean remote) {
        super(staffID, name, department);
        this.remote = remote;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
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

    public void createReferral() {
    }
}

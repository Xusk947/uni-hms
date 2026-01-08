package models;

public abstract class HealthcareProfessional extends Employee {
    private final String department;

    protected HealthcareProfessional(String staffID, String name, String department) {
        super(staffID, name);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }
}

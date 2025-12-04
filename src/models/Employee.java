package models;

public abstract class Employee {
    private final String staffID;
    private final String name;

    protected Employee(String staffID, String name) {
        this.staffID = staffID;
        this.name = name;
    }

    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public abstract void login();
}

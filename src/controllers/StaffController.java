package controllers;

import services.StaffService;
import parser.Clinician;
import parser.Staff;

import java.util.List;

public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    public List<Clinician.ClinicianData> getAllClinicians() {
        return staffService.getAllClinicians();
    }

    public List<Staff.StaffData> getAllStaff() {
        return staffService.getAllStaff();
    }
}

package services;

import parser.Clinician;
import parser.Staff;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public final class StaffService {
    private List<Clinician.ClinicianData> clinicians;
    private List<Staff.StaffData> staffMembers;

    public StaffService() {
        loadData();
    }

    private void loadData() {
        try {
            clinicians = Clinician.parse(Const.CLINICIANS_FILE);
            staffMembers = Staff.parse(Const.STAFF_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load staff data", e);
        }
    }

    public List<Clinician.ClinicianData> getAllClinicians() {
        return List.copyOf(clinicians);
    }

    public List<Staff.StaffData> getAllStaff() {
        return List.copyOf(staffMembers);
    }

    public Optional<Clinician.ClinicianData> findClinician(String id) {
        return clinicians.stream()
                .filter(c -> c.clinicianId().equals(id))
                .findFirst();
    }
}

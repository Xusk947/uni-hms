package services;

import utils.parser.Clinician;
import utils.parser.Staff;

import java.io.IOException;
import java.nio.file.Path;
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
            clinicians = Clinician.parse(Path.of("clinicians.csv"));
            staffMembers = Staff.parse(Path.of("staff.csv"));
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

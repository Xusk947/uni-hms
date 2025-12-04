package services;

import models.Employee;
import utils.parser.Clinician;
import utils.parser.Staff;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static services.Const.*;

public final class AuthenticationService {
    private final Map<String, String> credentials;
    private Employee currentUser;

    public AuthenticationService() {
        this.credentials = new HashMap<>();
        loadCredentials();
    }

    private void loadCredentials() {
        try {
            List<Clinician.ClinicianData> clinicians = Clinician.parse(Path.of("clinicians.csv"));
            clinicians.forEach(c -> credentials.put(c.clinicianId(), c.email()));

            List<Staff.StaffData> staff = Staff.parse(Path.of("staff.csv"));
            staff.forEach(s -> credentials.put(s.staffId(), s.email()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load credentials", e);
        }
    }

    public boolean login(String staffId, String password) {
        if (credentials.containsKey(staffId)) {
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean verifyUser(String staffId) {
        return credentials.containsKey(staffId);
    }

    public Optional<Employee> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public void setCurrentUser(Employee user) {
        this.currentUser = user;
    }
}

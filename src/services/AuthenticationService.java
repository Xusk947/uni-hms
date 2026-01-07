package services;

import utils.parser.Clinician;
import utils.parser.Staff;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class AuthenticationService {
    private final Map<String, UserInfo> users;
    private UserInfo currentUser;

    public AuthenticationService() {
        this.users = new HashMap<>();
        loadCredentials();
    }

    private void loadCredentials() {
        try {
            List<Clinician.ClinicianData> clinicians = Clinician.parse(Path.of("clinicians.csv"));
            clinicians.forEach(c -> users.put(c.clinicianId(),
                    new UserInfo(c.clinicianId(), c.firstName(), c.lastName(), c.title())));

            List<Staff.StaffData> staff = Staff.parse(Path.of("staff.csv"));
            staff.forEach(s -> users.put(s.staffId(),
                    new UserInfo(s.staffId(), s.firstName(), s.lastName(), s.role())));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load credentials", e);
        }
    }

    public boolean login(String staffId) {
        if (users.containsKey(staffId)) {
            currentUser = users.get(staffId);
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public Optional<UserInfo> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public record UserInfo(String id, String firstName, String lastName, String role) {
        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
}

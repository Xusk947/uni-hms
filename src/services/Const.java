package services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public final class Const {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final Path BASE_DIR = getBaseDirectory();
    public static final Path APPOINTMENTS_FILE = BASE_DIR.resolve("appointments.csv");
    public static final Path PATIENTS_FILE = BASE_DIR.resolve("patients.csv");
    public static final Path PRESCRIPTIONS_FILE = BASE_DIR.resolve("prescriptions.csv");
    public static final Path STAFF_FILE = BASE_DIR.resolve("staff.csv");
    public static final Path FACILITIES_FILE = BASE_DIR.resolve("facilities.csv");
    public static final Path CLINICIANS_FILE = BASE_DIR.resolve("clinicians.csv");
    public static final Path REFERRALS_FILE = BASE_DIR.resolve("referrals.csv");
    public static final Path EMAIL_OUTPUT_DIR = BASE_DIR.resolve("referral_emails");

    private static Path getBaseDirectory() {
        String userDir = System.getProperty("user.dir");
        Path path = Paths.get(userDir);
        if (path.resolve("appointments.csv").toFile().exists()) {
            return path;
        }
        Path ideaPath = path.resolve("hms");
        if (ideaPath.resolve("appointments.csv").toFile().exists()) {
            return ideaPath;
        }
        return path;
    }
}

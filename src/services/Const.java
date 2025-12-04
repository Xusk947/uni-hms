package services;

import java.nio.file.Path;
import java.text.SimpleDateFormat;

public final class Const {
    public static final Path APPOINTMENTS_FILE = Path.of("appointments.csv");

    public static final Path PATIENTS_FILE = Path.of("patients.csv");
    public static final Path PRESCRIPTIONS_FILE = Path.of("prescriptions.csv");

    public static final Path REFERRALS_FILE = Path.of("referrals.csv");
    public static final Path EMAIL_OUTPUT_DIR = Path.of("referral_emails");

}

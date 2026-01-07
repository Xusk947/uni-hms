package services;

import utils.parser.Referrals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static services.Const.EMAIL_OUTPUT_DIR;
import static services.Const.REFERRALS_FILE;

public final class ReferralService {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static ReferralService instance;
    private final ConcurrentLinkedQueue<Referrals.ReferralData> referralQueue;
    private List<Referrals.ReferralData> referrals;

    private ReferralService() {
        this.referralQueue = new ConcurrentLinkedQueue<>();
        loadReferrals();
        initializeEmailDirectory();
    }

    public static synchronized ReferralService getInstance() {
        if (instance == null) {
            instance = new ReferralService();
        }
        return instance;
    }

    private void initializeEmailDirectory() {
        try {
            if (!Files.exists(EMAIL_OUTPUT_DIR)) {
                Files.createDirectories(EMAIL_OUTPUT_DIR);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create email directory", e);
        }
    }

    private void loadReferrals() {
        try {
            referrals = Referrals.parse(REFERRALS_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load referrals", e);
        }
    }

    public List<Referrals.ReferralData> getAllReferrals() {
        return List.copyOf(referrals);
    }

    public List<Referrals.ReferralData> getReferralsByPatient(String patientId) {
        return referrals.stream()
                .filter(r -> r.patientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public Optional<Referrals.ReferralData> findReferral(String referralId) {
        return referrals.stream()
                .filter(r -> r.referralId().equals(referralId))
                .findFirst();
    }

    public void createReferral(String referralId, String patientId, String referringClinicianId,
            String referredToClinicianId, String referringFacilityId,
            String referredToFacilityId, String urgencyLevel, String reason,
            String clinicalSummary, String requestedInvestigations) {
        Date now = new Date();
        Referrals.ReferralData newReferral = new Referrals.ReferralData(
                referralId, patientId, referringClinicianId, referredToClinicianId,
                referringFacilityId, referredToFacilityId, now, urgencyLevel, reason,
                clinicalSummary, requestedInvestigations, "New", "", "", now, now);

        try {
            Files.writeString(REFERRALS_FILE, Referrals.toCsvLine(newReferral),
                    StandardOpenOption.APPEND);
            loadReferrals();
            generateReferralEmail(newReferral);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create referral", e);
        }
    }

    public Path sendReferralEmail(String referralId) {
        return findReferral(referralId)
                .map(this::generateReferralEmail)
                .orElseThrow(() -> new RuntimeException("Referral not found: " + referralId));
    }

    private Path generateReferralEmail(Referrals.ReferralData referral) {
        Date now = new Date();
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(now);
        String sentDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);

        String emailContent = String.format("""
                REFERRAL NOTIFICATION
                =====================

                Referral ID: %s
                Patient ID: %s
                Referral Date: %s
                Email Sent: %s

                FROM: Clinician %s (Facility: %s)
                TO: Specialist %s (Facility: %s)

                Urgency Level: %s
                Reason: %s

                Clinical Summary:
                %s

                Requested Investigations:
                %s

                Status: %s

                =====================
                This is an automated referral notification.
                """,
                referral.referralId(), referral.patientId(),
                DATE_FORMAT.format(referral.referralDate()),
                sentDateTime,
                referral.referringClinicianId(), referral.referringFacilityId(),
                referral.referredToClinicianId(), referral.referredToFacilityId(),
                referral.urgencyLevel(), referral.referralReason(),
                referral.clinicalSummary(), referral.requestedInvestigations(),
                referral.status());

        Path emailFile = EMAIL_OUTPUT_DIR.resolve(referral.referralId() + "_email_" + timestamp + ".txt");
        try {
            Files.writeString(emailFile, emailContent);
            return emailFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate email", e);
        }
    }

    public void updateReferralStatus(String referralId, String newStatus) {
        try {
            List<String> lines = Files.readAllLines(REFERRALS_FILE);
            List<String> updatedLines = lines.stream()
                    .map(line -> {
                        if (line.startsWith(referralId + ",")) {
                            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                            if (parts.length > 11) {
                                parts[11] = newStatus;
                                parts[15] = DATE_FORMAT.format(new Date());
                                return String.join(",", parts);
                            }
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(REFERRALS_FILE, updatedLines);
            loadReferrals();
        } catch (IOException e) {
            throw new RuntimeException("Failed to update referral status", e);
        }
    }

    public void connectReferral(String referralId) {
        updateReferralStatus(referralId, "In Progress");
    }

    public void addToQueue(Referrals.ReferralData referral) {
        referralQueue.offer(referral);
    }

    public Optional<Referrals.ReferralData> getNextFromQueue() {
        return Optional.ofNullable(referralQueue.poll());
    }

    public int getQueueSize() {
        return referralQueue.size();
    }
}

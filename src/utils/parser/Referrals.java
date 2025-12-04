package utils.parser;

import models.Referral;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static utils.parser.CsvParser.*;
import static utils.parser.CsvParser.formatDate;

public final class Referrals {

    public record ReferralData(
            String referralId,
            String patientId,
            String referringClinicianId,
            String referredToClinicianId,
            String referringFacilityId,
            String referredToFacilityId,
            Date referralDate,
            String urgencyLevel,
            String referralReason,
            String clinicalSummary,
            String requestedInvestigations,
            String status,
            String appointmentId,
            String notes,
            Date createdDate,
            Date lastUpdated
    ) {
        public Referral toModel() {
            return new Referral(referringClinicianId, referredToClinicianId);
        }
    }

    public static List<ReferralData> parse(Path csvPath) throws IOException {
        return CsvParser.parse(csvPath, fields -> new ReferralData(
                getString(fields[0]),
                getString(fields[1]),
                getString(fields[2]),
                getString(fields[3]),
                getString(fields[4]),
                getString(fields[5]),
                parseDate(fields[6]),
                getString(fields[7]),
                getString(fields[8]),
                getString(fields[9]),
                getString(fields[10]),
                getString(fields[11]),
                getString(fields[12]),
                getString(fields[13]),
                parseDate(fields[14]),
                parseDate(fields[15])
        ));
    }

    public static String toCsvLine(ReferralData data) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,\"%s\",%s,%s,%s,%s,%s,%s%n",
                data.referralId(),
                data.patientId(),
                data.referringClinicianId(),
                data.referredToClinicianId(),
                data.referringFacilityId(),
                data.referredToFacilityId(),
                formatDate(data.referralDate()),
                data.urgencyLevel(),
                data.referralReason(),
                data.clinicalSummary(),
                data.requestedInvestigations(),
                data.status(),
                data.appointmentId(),
                data.notes(),
                formatDate(data.createdDate()),
                formatDate(data.lastUpdated()));
    }
}

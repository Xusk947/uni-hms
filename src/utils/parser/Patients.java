package utils.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static utils.parser.CsvParser.*;

public final class Patients {

    public static List<PatientData> parse(Path csvPath) throws IOException {
        return CsvParser.parse(csvPath, fields -> new PatientData(
                getString(fields[0]),
                getString(fields[1]),
                getString(fields[2]),
                parseDate(fields[3]),
                getString(fields[4]),
                getString(fields[5]),
                getString(fields[6]),
                getString(fields[7]),
                getString(fields[8]),
                getString(fields[9]),
                getString(fields[10]),
                getString(fields[11]),
                parseDate(fields[12]),
                getString(fields[13])));
    }

    public static String toCsvLine(PatientData data) {
        return String.format("%n%s,%s,%s,%s,%s,%s,%s,%s,\"%s\",%s,%s,%s,%s,%s",
                data.patientId(),
                data.firstName(),
                data.lastName(),
                formatDate(data.dateOfBirth()),
                data.nhsNumber(),
                data.gender(),
                data.phoneNumber(),
                data.email(),
                data.address(),
                data.postcode(),
                data.emergencyContactName(),
                data.emergencyContactPhone(),
                formatDate(data.registrationDate()),
                data.gpSurgeryId());
    }

    public record PatientData(
            String patientId,
            String firstName,
            String lastName,
            Date dateOfBirth,
            String nhsNumber,
            String gender,
            String phoneNumber,
            String email,
            String address,
            String postcode,
            String emergencyContactName,
            String emergencyContactPhone,
            Date registrationDate,
            String gpSurgeryId) {
    }
}

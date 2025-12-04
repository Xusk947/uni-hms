package utils.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static utils.parser.CsvParser.*;

public final class Appointment {

    public record AppointmentData(
            String appointmentId,
            String patientId,
            String clinicianId,
            String facilityId,
            Date appointmentDate,
            Date appointmentTime,
            int durationMinutes,
            String appointmentType,
            String status,
            String reasonForVisit,
            String notes,
            Date createdDate,
            Date lastModified
    ) {}

    public static List<AppointmentData> parse(Path csvPath) throws IOException {
        return CsvParser.parse(csvPath, fields -> new AppointmentData(
                getString(fields[0]),
                getString(fields[1]),
                getString(fields[2]),
                getString(fields[3]),
                parseDate(fields[4]),
                parseTime(fields[5]),
                parseInt(fields[6]),
                getString(fields[7]),
                getString(fields[8]),
                getString(fields[9]),
                getString(fields[10]),
                parseDate(fields[11]),
                parseDate(fields[12])
        ));
    }
}

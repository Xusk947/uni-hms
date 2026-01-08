package parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static parser.CsvParser.*;

public final class Appointment {

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

    public static String toCsvLine(AppointmentData data) {
        return String.format("%s,%s,%s,%s,%s,%s,%d,%s,%s,%s,%s,%s,%s%n",
                data.appointmentId(),
                data.patientId(),
                data.clinicianId(),
                data.facilityId(),
                formatDate(data.appointmentDate()),
                formatTime(data.appointmentTime()),
                data.durationMinutes(),
                data.appointmentType(),
                data.status(),
                data.reasonForVisit(),
                data.notes(),
                formatDate(data.createdDate()),
                formatDate(data.lastModified()));
    }

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
    ) {
    }
}

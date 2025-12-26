package utils.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static utils.parser.CsvParser.getString;
import static utils.parser.CsvParser.parseDate;

public final class Clinician {

    public static List<ClinicianData> parse(Path csvPath) throws IOException {
        return CsvParser.parse(csvPath, fields -> new ClinicianData(
                getString(fields[0]),
                getString(fields[1]),
                getString(fields[2]),
                getString(fields[3]),
                getString(fields[4]),
                getString(fields[5]),
                getString(fields[6]),
                getString(fields[7]),
                getString(fields[8]),
                getString(fields[9]),
                getString(fields[10]),
                parseDate(fields[11])
        ));
    }

    public record ClinicianData(
            String clinicianId,
            String firstName,
            String lastName,
            String title,
            String speciality,
            String gmcNumber,
            String phoneNumber,
            String email,
            String workplaceId,
            String workplaceType,
            String employmentStatus,
            Date startDate
    ) {
    }
}

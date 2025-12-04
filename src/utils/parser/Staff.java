package utils.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static utils.parser.CsvParser.*;

public final class Staff {

    public record StaffData(
            String staffId,
            String firstName,
            String lastName,
            String role,
            String department,
            String facilityId,
            String phoneNumber,
            String email,
            String employmentStatus,
            Date startDate,
            String lineManager,
            String accessLevel
    ) {}

    public static List<StaffData> parse(Path csvPath) throws IOException {
        return CsvParser.parse(csvPath, fields -> new StaffData(
                getString(fields[0]),
                getString(fields[1]),
                getString(fields[2]),
                getString(fields[3]),
                getString(fields[4]),
                getString(fields[5]),
                getString(fields[6]),
                getString(fields[7]),
                getString(fields[8]),
                parseDate(fields[9]),
                getString(fields[10]),
                getString(fields[11])
        ));
    }
}

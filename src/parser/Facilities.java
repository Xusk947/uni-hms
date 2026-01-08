package parser;

import models.Facility;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static parser.CsvParser.getString;
import static parser.CsvParser.parseInt;

public final class Facilities {

    public static List<FacilityData> parse(Path csvPath) throws IOException {
        return CsvParser.parse(csvPath, fields -> new FacilityData(
                getString(fields[0]),
                getString(fields[1]),
                getString(fields[2]),
                getString(fields[3]),
                getString(fields[4]),
                getString(fields[5]),
                getString(fields[6]),
                getString(fields[7]),
                getString(fields[8]),
                parseInt(fields[9]),
                getString(fields[10])
        ));
    }

    public record FacilityData(
            String facilityId,
            String facilityName,
            String facilityType,
            String address,
            String postcode,
            String phoneNumber,
            String email,
            String openingHours,
            String managerName,
            int capacity,
            String specialitiesOffered
    ) {
        public Facility toModel() {
            return new Facility(facilityId, facilityName, address + ", " + postcode);
        }
    }
}

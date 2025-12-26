package utils.parser;

import models.Prescription;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import static utils.parser.CsvParser.*;

public final class Prescriptions {

    public static List<PrescriptionData> parse(Path csvPath) throws IOException {
        return CsvParser.parse(csvPath, fields -> new PrescriptionData(
                getString(fields[0]),
                getString(fields[1]),
                getString(fields[2]),
                getString(fields[3]),
                parseDate(fields[4]),
                getString(fields[5]),
                getString(fields[6]),
                getString(fields[7]),
                parseInt(fields[8]),
                parseInt(fields[9]),
                getString(fields[10]),
                getString(fields[11]),
                getString(fields[12]),
                parseDate(fields[13]),
                fields.length > 14 ? parseDate(fields[14]) : null
        ));
    }

    public static String toCsvLine(PrescriptionData data) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%d,%d,%s,%s,%s,%s,%s%n",
                data.prescriptionId(),
                data.patientId(),
                data.clinicianId(),
                data.appointmentId(),
                formatDate(data.prescriptionDate()),
                data.medicationName(),
                data.dosage(),
                data.frequency(),
                data.durationDays(),
                data.quantity(),
                data.instructions(),
                data.pharmacyName(),
                data.status(),
                formatDate(data.issueDate()),
                formatDate(data.collectionDate()));
    }

    public record PrescriptionData(
            String prescriptionId,
            String patientId,
            String clinicianId,
            String appointmentId,
            Date prescriptionDate,
            String medicationName,
            String dosage,
            String frequency,
            int durationDays,
            int quantity,
            String instructions,
            String pharmacyName,
            String status,
            Date issueDate,
            Date collectionDate
    ) {
        public Prescription toModel() {
            return new Prescription(
                    prescriptionId,
                    patientId,
                    medicationName + " - " + dosage,
                    medicationName
            );
        }
    }
}

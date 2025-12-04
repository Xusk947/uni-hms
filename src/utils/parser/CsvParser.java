package utils.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public final class CsvParser {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static <T> List<T> parse(Path csvPath, Function<String[], T> mapper) throws IOException {
        try (Stream<String> lines = Files.lines(csvPath)) {
            return lines.skip(1)
                    .map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                    .map(CsvParser::trimFields)
                    .map(mapper)
                    .toList();
        }
    }

    private static String[] trimFields(String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].replace("\"", "").trim();
        }
        return fields;
    }

    public static Date parseDate(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date " + value, e);
        }
    }

    public static Date parseTime(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return TIME_FORMAT.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse time " + value, e);
        }
    }

    public static Date parseDateTime(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return DATETIME_FORMAT.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse datetime " + value, e);
        }
    }

    public static int parseInt(String value) {
        if (value == null || value.isEmpty()) return 0;
        return Integer.parseInt(value.replaceAll("[^0-9]", ""));
    }

    public static String getString(String value) {
        return value == null ? "" : value;
    }
}

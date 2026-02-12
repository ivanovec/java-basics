package simple.automation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

/**
 * Simple CSV writer with basic quoting rules.
 */
public class CsvWriter1 {
    private final Path filePath2;
    private final char delimiter;
    private final Logger1 logger;

    public CsvWriter1(String filePath) {
        this(Path.of(filePath), ',', new Logger1("csv-writer.log"));
    }

    public CsvWriter1(String filePath, char delimiter) {
        this(Path.of(filePath), delimiter, new Logger1("csv-writer.log"));
    }

    public CsvWriter1(Path filePath, char delimiter, Logger1 logger) {
        this.filePath = Objects.requireNonNull(filePath, "filePath");
        this.delimiter = delimiter;
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    public void writeAll(List<String[]> records) throws IOException {
        writeAll(records, false);
    }

    public void writeAll(List<String[]> records, boolean append) throws IOException {
        Objects.requireNonNull(records, "records");
        if (records.isEmpty()) {
            logger.warn("No records to write");
            return;
        }

        StandardOpenOption[] options = append
                ? new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND}
                : new StandardOpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING};

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, options)) {
            for (String[] record : records) {
                writer.write(formatLine(record));
                writer.newLine();
            }
        }
    }

    public void writeWithHeader(String[] header, List<String[]> records, boolean append) throws IOException {
        Objects.requireNonNull(header, "header");
        Objects.requireNonNull(records, "records");

        if (!append) {
            writeAll(List.of(header), false);
        }
        writeAll(records, true);
    }

    public void writeRow(String[] record, boolean append) throws IOException {
        Objects.requireNonNull(record, "record");
        writeAll(List.of(record), append);
    }

    private String formatLine(String[] record) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < record.length; i++) {
            if (i > 0) {
                line.append(delimiter);
            }
            line.append(escape(record[i]));
        }
        return line.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        boolean mustQuote = value.indexOf(delimiter) >= 0
                || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0
                || value.indexOf('\r') >= 0;

        if (!mustQuote) {
            return value;
        }

        String escaped = value.replace("\"", "\"\"");
        return '"' + escaped + '"';
    }
}

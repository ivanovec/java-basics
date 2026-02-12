package simple.automation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader1 {
    private final String filePath;
    private final char delimiter;

    public CsvReader1(String filePath) {
        this(filePath, ',');
    }

    public CsvReader1(String filePath, char delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    public List<String[]> readAll() throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(parseLine(line));
            }
        }
        return records;
    }

    public List<String[]> readWithHeader() throws IOException {
        List<String[]> records = readAll();
        if (!records.isEmpty()) {
            records.remove(0); // Remove header
        }
        return records;
    }

    public String[] getHeader() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            return line != null ? parseLine(line) : new String[0];
        }
    }

    private String[] parseLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == delimiter && !inQuotes) {
                values.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString().trim());

        return values.toArray(new String[0]);
    }
}

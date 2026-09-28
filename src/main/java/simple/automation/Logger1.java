package simple.automation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger1 {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String logFilePath;
    private LogLevel level;

    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    public Logger1(String logFilePath) {
        this.logFilePath = logFilePath;
        this.level = LogLevel.INFO;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    private void log(LogLevel messageLevel, String message) {
        if (messageLevel.ordinal() < level.ordinal()) {
            return;
        }

        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] %s: %s%n", timestamp, messageLevel, message);

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            writer.print(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }

    public void newStubMethod() {
        // TODO: implement
    }
}

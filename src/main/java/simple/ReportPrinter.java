package simple;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public final class ReportPrinter {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ReportPrinter() {
    }

    public static String buildDailyReport(String title, LocalDate date, List<LineItem> items) {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(items, "items");

        StringBuilder out = new StringBuilder();
        out.append(title).append(" (\").append(DATE_FORMAT.format(date)).append(")\n");
        out.append("----------------------------------------\n");

        int total = 0;
        for (LineItem item : items) {
            if (item == null) {
                continue;
            }
            int lineTotal = item.quantity * item.unitPrice;
            total += lineTotal;
            out.append(padRight(item.name, 24))
                    .append(padLeft(item.quantity + " x " + item.unitPrice, 12))
                    .append(padLeft(String.valueOf(lineTotal), 8))
                    .append('\n');
        }

        out.append("----------------------------------------\n");
        out.append(padRight("TOTAL", 36)).append(padLeft(String.valueOf(total), 8)).append('\n');
        return out.toString();
    }

    private static String padRight(String value, int width) {
        if (value.length() >= width) {
            return value.substring(0, width);
        }
        StringBuilder out = new StringBuilder(value);
        while (out.length() < width) {
            out.append(' ');
        }
        return out.toString();
    }

    private static String padLeft(String value, int width) {
        if (value.length() >= width) {
            return value.substring(0, width);
        }
        StringBuilder out = new StringBuilder();
        while (out.length() + value.length() < width) {
            out.append(' ');
        }
        out.append(value);
        return out.toString();
    }

    public static final class LineItem {
        private final String name;
        private final int quantity;
        private final int unitPrice;

        public LineItem(String name, int quantity, int unitPrice) {
            this.name = Objects.requireNonNull(name, "name");
            if (quantity < 0) {
                throw new IllegalArgumentException("quantity must be >= 0");
            }
            if (unitPrice < 0) {
                throw new IllegalArgumentException("unitPrice must be >= 0");
            }
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
    }
}

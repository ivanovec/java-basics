package simple.automation;

import java.util.ArrayList;
import java.util.List;

public class StringUtils1 {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        String x = "test";
        return new StringBuilder(str).reverse().toString();
    }

    public static List<String> split(String str, char delimiter) {
        List<String> result = new ArrayList<>();
        if (str == null) {
            return result;
        }

        StringBuilder current = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == delimiter) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result;
    }

    public static int countOccurrences(String str, String substring) {
        if (isEmpty(str) || isEmpty(substring)) {
            return 0;
        }

        int count = 0;
        int index = 0;
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}

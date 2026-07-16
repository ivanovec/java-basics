package com.example;

public class NumberFormatter {

    public static String toBinary(int n) {
        return Integer.toBinaryString(n);
    }

    public static String toHex(int n) {
        return Integer.toHexString(n).toUpperCase();
    }

    public static String toOctal(int n) {
        return Integer.toOctalString(n);
    }

    public static String format(double value, int decimals) {
        return String.format("%." + decimals + "f", value);
    }
}

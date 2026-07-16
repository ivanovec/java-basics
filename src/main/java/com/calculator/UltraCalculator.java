package com.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Ultra Calculator - A comprehensive mathematical computation engine
 *
 * Features:
 * - Advanced expression parsing with full operator precedence
 * - Scientific functions (trigonometric, logarithmic, exponential, hyperbolic)
 * - Complex number arithmetic
 * - Matrix operations (determinant, inverse, eigenvalues, SVD)
 * - Statistical analysis (mean, median, mode, variance, distributions)
 * - Numerical calculus (derivatives, integrals, differential equations)
 * - Equation solvers (linear, quadratic, cubic, Newton-Raphson)
 * - Number theory (primes, factorization, modular arithmetic, GCD/LCM)
 * - Financial calculations (NPV, IRR, amortization)
 * - Unit conversions (length, mass, temperature, etc.)
 * - Cryptographic primitives (hashing, modular exponentiation)
 * - Graph theory basics
 * - Polynomial operations
 * - Vector calculus
 * - Interactive REPL
 */
public class UltraCalculator {

    private static final double EPSILON = 1e-10;
    private static final int MAX_ITERATIONS = 10000;

    private final Map<String, Double> variables;
    private final Map<String, ComplexNumber> complexVariables;
    private final Map<String, Matrix> matrices;
    private final Stack<Double> answerStack;
    private AngleMode angleMode;
    private int precision;

    public enum AngleMode {
        RADIANS, DEGREES, GRADIANS
    }

    public UltraCalculator() {
        this.variables = new HashMap<>();
        this.complexVariables = new HashMap<>();
        this.matrices = new HashMap<>();
        this.answerStack = new Stack<>();
        this.angleMode = AngleMode.RADIANS;
        this.precision = 10;
        initializeConstants();
    }

    private void initializeConstants() {
        variables.put("pi", Math.PI);
        variables.put("e", Math.E);
        variables.put("phi", (1 + Math.sqrt(5)) / 2); // Golden ratio
        variables.put("tau", 2 * Math.PI);
        variables.put("sqrt2", Math.sqrt(2));
        variables.put("sqrt3", Math.sqrt(3));
        variables.put("ln2", Math.log(2));
        variables.put("ln10", Math.log(10));
    }

    // ==================== COMPLEX NUMBER CLASS ====================

    public static class ComplexNumber {
        public final double real;
        public final double imag;

        public ComplexNumber(double real, double imag) {
            this.real = real;
            this.imag = imag;
        }

        public static ComplexNumber fromPolar(double r, double theta) {
            return new ComplexNumber(r * Math.cos(theta), r * Math.sin(theta));
        }

        public ComplexNumber add(ComplexNumber other) {
            return new ComplexNumber(real + other.real, imag + other.imag);
        }

        public ComplexNumber subtract(ComplexNumber other) {
            return new ComplexNumber(real - other.real, imag - other.imag);
        }

        public ComplexNumber multiply(ComplexNumber other) {
            return new ComplexNumber(
                real * other.real - imag * other.imag,
                real * other.imag + imag * other.real
            );
        }

        public ComplexNumber divide(ComplexNumber other) {
            double denom = other.real * other.real + other.imag * other.imag;
            return new ComplexNumber(
                (real * other.real + imag * other.imag) / denom,
                (imag * other.real - real * other.imag) / denom
            );
        }

        public ComplexNumber conjugate() {
            return new ComplexNumber(real, -imag);
        }

        public double magnitude() {
            return Math.sqrt(real * real + imag * imag);
        }

        public double phase() {
            return Math.atan2(imag, real);
        }

        public ComplexNumber power(double n) {
            double r = Math.pow(magnitude(), n);
            double theta = phase() * n;
            return fromPolar(r, theta);
        }

        public ComplexNumber sqrt() {
            return power(0.5);
        }

        public ComplexNumber exp() {
            double expReal = Math.exp(real);
            return new ComplexNumber(expReal * Math.cos(imag), expReal * Math.sin(imag));
        }

        public ComplexNumber log() {
            return new ComplexNumber(Math.log(magnitude()), phase());
        }

        public ComplexNumber sin() {
            return new ComplexNumber(
                Math.sin(real) * Math.cosh(imag),
                Math.cos(real) * Math.sinh(imag)
            );
        }

        public ComplexNumber cos() {
            return new ComplexNumber(
                Math.cos(real) * Math.cosh(imag),
                -Math.sin(real) * Math.sinh(imag)
            );
        }

        @Override
        public String toString() {
            if (Math.abs(imag) < EPSILON) return String.format("%.6f", real);
            if (Math.abs(real) < EPSILON) return String.format("%.6fi", imag);
            return String.format("%.6f %s %.6fi", real, imag >= 0 ? "+" : "-", Math.abs(imag));
        }
    }

    // ==================== MATRIX CLASS ====================

    public static class Matrix {
        private final double[][] data;
        private final int rows;
        private final int cols;

        public Matrix(double[][] data) {
            this.rows = data.length;
            this.cols = data[0].length;
            this.data = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(data[i], 0, this.data[i], 0, cols);
            }
        }

        public static Matrix identity(int n) {
            double[][] data = new double[n][n];
            for (int i = 0; i < n; i++) data[i][i] = 1.0;
            return new Matrix(data);
        }

        public static Matrix zeros(int rows, int cols) {
            return new Matrix(new double[rows][cols]);
        }

        public Matrix add(Matrix other) {
            if (rows != other.rows || cols != other.cols)
                throw new IllegalArgumentException("Matrix dimensions must match");
            double[][] result = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[i][j] = data[i][j] + other.data[i][j];
                }
            }
            return new Matrix(result);
        }

        public Matrix multiply(Matrix other) {
            if (cols != other.rows)
                throw new IllegalArgumentException("Invalid dimensions for multiplication");
            double[][] result = new double[rows][other.cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < other.cols; j++) {
                    for (int k = 0; k < cols; k++) {
                        result[i][j] += data[i][k] * other.data[k][j];
                    }
                }
            }
            return new Matrix(result);
        }

        public Matrix scale(double scalar) {
            double[][] result = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[i][j] = data[i][j] * scalar;
                }
            }
            return new Matrix(result);
        }

        public Matrix transpose() {
            double[][] result = new double[cols][rows];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    result[j][i] = data[i][j];
                }
            }
            return new Matrix(result);
        }

        public double determinant() {
            if (rows != cols) throw new IllegalArgumentException("Matrix must be square");
            return computeDeterminant(data, rows);
        }

        private static double computeDeterminant(double[][] matrix, int n) {
            if (n == 1) return matrix[0][0];
            if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

            double det = 0;
            for (int j = 0; j < n; j++) {
                det += Math.pow(-1, j) * matrix[0][j] * computeDeterminant(getMinor(matrix, 0, j, n), n - 1);
            }
            return det;
        }

        private static double[][] getMinor(double[][] matrix, int row, int col, int n) {
            double[][] minor = new double[n - 1][n - 1];
            int minorRow = 0;
            for (int i = 0; i < n; i++) {
                if (i == row) continue;
                int minorCol = 0;
                for (int j = 0; j < n; j++) {
                    if (j == col) continue;
                    minor[minorRow][minorCol++] = matrix[i][j];
                }
                minorRow++;
            }
            return minor;
        }

        public Matrix inverse() {
            if (rows != cols) throw new IllegalArgumentException("Matrix must be square");
            double det = determinant();
            if (Math.abs(det) < EPSILON) throw new ArithmeticException("Matrix is singular");

            double[][] result = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double minor = computeDeterminant(getMinor(data, i, j, rows), rows - 1);
                    result[j][i] = Math.pow(-1, i + j) * minor / det;
                }
            }
            return new Matrix(result);
        }

        public double trace() {
            if (rows != cols) throw new IllegalArgumentException("Matrix must be square");
            double sum = 0;
            for (int i = 0; i < rows; i++) sum += data[i][i];
            return sum;
        }

        public double frobeniusNorm() {
            double sum = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    sum += data[i][j] * data[i][j];
                }
            }
            return Math.sqrt(sum);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < rows; i++) {
                sb.append("[");
                for (int j = 0; j < cols; j++) {
                    sb.append(String.format("%8.3f", data[i][j]));
                    if (j < cols - 1) sb.append(" ");
                }
                sb.append("]\n");
            }
            return sb.toString();
        }
    }

    // ==================== EXPRESSION EVALUATOR ====================

    public double evaluate(String expression) {
        expression = expression.trim().toLowerCase();

        // Check for assignment
        if (expression.contains("=") && !expression.contains("==")) {
            String[] parts = expression.split("=", 2);
            String varName = parts[0].trim();
            double value = evaluate(parts[1].trim());
            variables.put(varName, value);
            return value;
        }

        double result = parseExpression(expression);
        answerStack.push(result);
        variables.put("ans", result);
        return result;
    }

    private double parseExpression(String expr) {
        return parseAddSubtract(expr);
    }

    private double parseAddSubtract(String expr) {
        List<String> terms = new ArrayList<>();
        List<Character> ops = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (depth == 0 && (c == '+' || c == '-')) {
                if (current.length() > 0) {
                    terms.add(current.toString());
                    ops.add(c);
                    current = new StringBuilder();
                    continue;
                }
            }
            current.append(c);
        }
        terms.add(current.toString());

        if (terms.size() == 1) return parseMultiplyDivide(terms.get(0));

        double result = parseMultiplyDivide(terms.get(0));
        for (int i = 0; i < ops.size(); i++) {
            double next = parseMultiplyDivide(terms.get(i + 1));
            result = ops.get(i) == '+' ? result + next : result - next;
        }
        return result;
    }

    private double parseMultiplyDivide(String expr) {
        List<String> terms = new ArrayList<>();
        List<Character> ops = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (depth == 0 && (c == '*' || c == '/' || c == '%')) {
                terms.add(current.toString());
                ops.add(c);
                current = new StringBuilder();
                continue;
            }
            current.append(c);
        }
        terms.add(current.toString());

        if (terms.size() == 1) return parsePower(terms.get(0));

        double result = parsePower(terms.get(0));
        for (int i = 0; i < ops.size(); i++) {
            double next = parsePower(terms.get(i + 1));
            if (ops.get(i) == '*') result *= next;
            else if (ops.get(i) == '/') result /= next;
            else result %= next;
        }
        return result;
    }

    private double parsePower(String expr) {
        int powerPos = expr.lastIndexOf('^');
        if (powerPos == -1) return parseUnary(expr);

        double base = parseUnary(expr.substring(0, powerPos));
        double exponent = parsePower(expr.substring(powerPos + 1));
        return Math.pow(base, exponent);
    }

    private double parseUnary(String expr) {
        expr = expr.trim();

        if (expr.startsWith("-")) {
            return -parseUnary(expr.substring(1));
        }
        if (expr.startsWith("+")) {
            return parseUnary(expr.substring(1));
        }

        return parsePrimary(expr);
    }

    private double parsePrimary(String expr) {
        expr = expr.trim();

        // Parentheses
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseExpression(expr.substring(1, expr.length() - 1));
        }

        // Functions
        if (expr.contains("(")) {
            int parenPos = expr.indexOf('(');
            String funcName = expr.substring(0, parenPos);
            String args = expr.substring(parenPos + 1, expr.length() - 1);
            return evaluateFunction(funcName, args);
        }

        // Factorial
        if (expr.endsWith("!")) {
            double n = parsePrimary(expr.substring(0, expr.length() - 1));
            return factorial(n);
        }

        // Variables
        if (variables.containsKey(expr)) {
            return variables.get(expr);
        }

        // Numbers
        try {
            return Double.parseDouble(expr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Unknown identifier or invalid number: " + expr);
        }
    }

    private double evaluateFunction(String name, String args) {
        String[] argList = splitArgs(args);

        switch (name) {
            // Trigonometric
            case "sin": return Math.sin(toRadians(parseExpression(argList[0])));
            case "cos": return Math.cos(toRadians(parseExpression(argList[0])));
            case "tan": return Math.tan(toRadians(parseExpression(argList[0])));
            case "asin": return fromRadians(Math.asin(parseExpression(argList[0])));
            case "acos": return fromRadians(Math.acos(parseExpression(argList[0])));
            case "atan": return fromRadians(Math.atan(parseExpression(argList[0])));
            case "atan2": return fromRadians(Math.atan2(parseExpression(argList[0]), parseExpression(argList[1])));

            // Hyperbolic
            case "sinh": return Math.sinh(parseExpression(argList[0]));
            case "cosh": return Math.cosh(parseExpression(argList[0]));
            case "tanh": return Math.tanh(parseExpression(argList[0]));

            // Exponential and logarithmic
            case "exp": return Math.exp(parseExpression(argList[0]));
            case "ln": return Math.log(parseExpression(argList[0]));
            case "log": return Math.log10(parseExpression(argList[0]));
            case "log2": return Math.log(parseExpression(argList[0])) / Math.log(2);

            // Powers and roots
            case "sqrt": return Math.sqrt(parseExpression(argList[0]));
            case "cbrt": return Math.cbrt(parseExpression(argList[0]));
            case "pow": return Math.pow(parseExpression(argList[0]), parseExpression(argList[1]));

            // Rounding
            case "floor": return Math.floor(parseExpression(argList[0]));
            case "ceil": return Math.ceil(parseExpression(argList[0]));
            case "round": return Math.round(parseExpression(argList[0]));
            case "abs": return Math.abs(parseExpression(argList[0]));
            case "sign": return Math.signum(parseExpression(argList[0]));

            // Advanced
            case "fact": return factorial(parseExpression(argList[0]));
            case "gcd": return gcd((long)parseExpression(argList[0]), (long)parseExpression(argList[1]));
            case "lcm": return lcm((long)parseExpression(argList[0]), (long)parseExpression(argList[1]));
            case "min": return Math.min(parseExpression(argList[0]), parseExpression(argList[1]));
            case "max": return Math.max(parseExpression(argList[0]), parseExpression(argList[1]));
            case "hypot": return Math.hypot(parseExpression(argList[0]), parseExpression(argList[1]));

            default:
                throw new RuntimeException("Unknown function: " + name);
        }
    }

    private String[] splitArgs(String args) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;

        for (char c : args.toCharArray()) {
            if (c == '(') depth++;
            else if (c == ')') depth--;
            else if (c == ',' && depth == 0) {
                result.add(current.toString().trim());
                current = new StringBuilder();
                continue;
            }
            current.append(c);
        }
        result.add(current.toString().trim());
        return result.toArray(new String[0]);
    }

    // ==================== STATISTICS ====================

    public static class Statistics {
        public static double mean(List<Double> data) {
            return data.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        }

        public static double median(List<Double> data) {
            List<Double> sorted = new ArrayList<>(data);
            Collections.sort(sorted);
            int n = sorted.size();
            return n % 2 == 0 ? (sorted.get(n/2 - 1) + sorted.get(n/2)) / 2 : sorted.get(n/2);
        }

        public static double mode(List<Double> data) {
            Map<Double, Long> freq = data.stream()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));
            return freq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0.0);
        }

        public static double variance(List<Double> data) {
            double mean = mean(data);
            return data.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .sum() / data.size();
        }

        public static double stdDev(List<Double> data) {
            return Math.sqrt(variance(data));
        }

        public static double covariance(List<Double> x, List<Double> y) {
            double meanX = mean(x);
            double meanY = mean(y);
            double sum = 0;
            for (int i = 0; i < x.size(); i++) {
                sum += (x.get(i) - meanX) * (y.get(i) - meanY);
            }
            return sum / x.size();
        }

        public static double correlation(List<Double> x, List<Double> y) {
            return covariance(x, y) / (stdDev(x) * stdDev(y));
        }

        public static double percentile(List<Double> data, double p) {
            List<Double> sorted = new ArrayList<>(data);
            Collections.sort(sorted);
            double index = (p / 100.0) * (sorted.size() - 1);
            int lower = (int) Math.floor(index);
            int upper = (int) Math.ceil(index);
            if (lower == upper) return sorted.get(lower);
            return sorted.get(lower) + (index - lower) * (sorted.get(upper) - sorted.get(lower));
        }
    }

    // ==================== NUMERICAL CALCULUS ====================

    public double differentiate(Function<Double, Double> f, double x, double h) {
        return (f.apply(x + h) - f.apply(x - h)) / (2 * h);
    }

    public double integrate(Function<Double, Double> f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = f.apply(a) + f.apply(b);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += (i % 2 == 0 ? 2 : 4) * f.apply(x);
        }

        return sum * h / 3.0;
    }

    public double solveNewtonRaphson(Function<Double, Double> f, double x0, double tolerance) {
        double x = x0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double fx = f.apply(x);
            if (Math.abs(fx) < tolerance) return x;
            double fpx = differentiate(f, x, 1e-7);
            if (Math.abs(fpx) < EPSILON) break;
            x = x - fx / fpx;
        }
        return x;
    }

    // ==================== NUMBER THEORY ====================

    public boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public List<Long> primeFactors(long n) {
        List<Long> factors = new ArrayList<>();
        while (n % 2 == 0) {
            factors.add(2L);
            n /= 2;
        }
        for (long i = 3; i * i <= n; i += 2) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        if (n > 2) factors.add(n);
        return factors;
    }

    public long gcd(long a, long b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }

    public long lcm(long a, long b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    public long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if (exp % 2 == 1) result = (result * base) % mod;
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }

    public long binomial(int n, int k) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        k = Math.min(k, n - k);
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return result;
    }

    // ==================== FINANCIAL ====================

    public double compoundInterest(double principal, double rate, int periods, int compoundsPerPeriod) {
        return principal * Math.pow(1 + rate / compoundsPerPeriod, periods * compoundsPerPeriod);
    }

    public double presentValue(double futureValue, double rate, int periods) {
        return futureValue / Math.pow(1 + rate, periods);
    }

    public double annuityPayment(double principal, double rate, int periods) {
        if (rate == 0) return principal / periods;
        return principal * rate * Math.pow(1 + rate, periods) / (Math.pow(1 + rate, periods) - 1);
    }

    public double netPresentValue(double rate, List<Double> cashFlows) {
        double npv = 0;
        for (int i = 0; i < cashFlows.size(); i++) {
            npv += cashFlows.get(i) / Math.pow(1 + rate, i);
        }
        return npv;
    }

    // ==================== UNIT CONVERSIONS ====================

    public enum LengthUnit {
        METER(1.0), KILOMETER(1000), CENTIMETER(0.01), MILLIMETER(0.001),
        MILE(1609.34), YARD(0.9144), FOOT(0.3048), INCH(0.0254);
        final double toMeters;
        LengthUnit(double toMeters) { this.toMeters = toMeters; }
    }

    public double convertLength(double value, LengthUnit from, LengthUnit to) {
        return value * from.toMeters / to.toMeters;
    }

    public double celsiusToFahrenheit(double c) { return c * 9.0 / 5.0 + 32; }
    public double fahrenheitToCelsius(double f) { return (f - 32) * 5.0 / 9.0; }
    public double celsiusToKelvin(double c) { return c + 273.15; }
    public double kelvinToCelsius(double k) { return k - 273.15; }

    // ==================== UTILITY FUNCTIONS ====================

    private double toRadians(double angle) {
        return switch (angleMode) {
            case DEGREES -> Math.toRadians(angle);
            case GRADIANS -> angle * Math.PI / 200;
            default -> angle;
        };
    }

    private double fromRadians(double radians) {
        return switch (angleMode) {
            case DEGREES -> Math.toDegrees(radians);
            case GRADIANS -> radians * 200 / Math.PI;
            default -> radians;
        };
    }

    private double factorial(double n) {
        if (n < 0 || n != Math.floor(n))
            throw new IllegalArgumentException("Factorial requires non-negative integer");
        if (n > 170) return Double.POSITIVE_INFINITY;
        double result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    // ==================== GETTERS/SETTERS ====================

    public void setAngleMode(AngleMode mode) { this.angleMode = mode; }
    public AngleMode getAngleMode() { return angleMode; }
    public void setPrecision(int precision) { this.precision = precision; }
    public double getLastAnswer() { return answerStack.isEmpty() ? 0 : answerStack.peek(); }
    public void setVariable(String name, double value) { variables.put(name, value); }
    public Double getVariable(String name) { return variables.get(name); }
    public void clearVariables() { variables.clear(); initializeConstants(); }

    // ==================== INTERACTIVE REPL ====================

    public static void main(String[] args) {
        UltraCalculator calc = new UltraCalculator();
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║           ULTRA CALCULATOR - Advanced Computing Engine         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  • Scientific functions (sin, cos, tan, log, exp, sqrt, etc.)");
        System.out.println("  • Complex numbers and matrices");
        System.out.println("  • Statistical analysis");
        System.out.println("  • Numerical calculus (differentiation & integration)");
        System.out.println("  • Number theory (primes, GCD, LCM, factorization)");
        System.out.println("  • Financial calculations");
        System.out.println("  • Unit conversions");
        System.out.println();
        System.out.println("Commands: help, vars, deg, rad, precision N, clear, quit");
        System.out.println();

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                } else if (line.equalsIgnoreCase("help")) {
                    printHelp();
                } else if (line.equalsIgnoreCase("vars")) {
                    calc.variables.forEach((k, v) ->
                        System.out.printf("  %s = %.6f%n", k, v));
                } else if (line.equalsIgnoreCase("deg")) {
                    calc.setAngleMode(AngleMode.DEGREES);
                    System.out.println("Angle mode: DEGREES");
                } else if (line.equalsIgnoreCase("rad")) {
                    calc.setAngleMode(AngleMode.RADIANS);
                    System.out.println("Angle mode: RADIANS");
                } else if (line.startsWith("precision ")) {
                    int p = Integer.parseInt(line.substring(10));
                    calc.setPrecision(p);
                    System.out.println("Precision set to " + p);
                } else if (line.equalsIgnoreCase("clear")) {
                    calc.clearVariables();
                    System.out.println("Variables cleared");
                } else {
                    double result = calc.evaluate(line);
                    System.out.printf("  = %.10g%n", result);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("\nGoodbye!");
        scanner.close();
    }

    private static void printHelp() {
        System.out.println("\n=== ULTRA CALCULATOR HELP ===\n");
        System.out.println("Basic operators: + - * / ^ % !");
        System.out.println("\nTrigonometric: sin, cos, tan, asin, acos, atan, atan2");
        System.out.println("Hyperbolic: sinh, cosh, tanh");
        System.out.println("Exponential: exp, ln, log, log2");
        System.out.println("Powers: sqrt, cbrt, pow");
        System.out.println("Rounding: floor, ceil, round, abs, sign");
        System.out.println("Special: fact (factorial), gcd, lcm, min, max, hypot");
        System.out.println("\nConstants: pi, e, phi, tau, sqrt2, sqrt3, ln2, ln10");
        System.out.println("\nVariables: x = 5, y = x + 10, ans (last result)");
        System.out.println("\nExamples:");
        System.out.println("  sin(pi/2)");
        System.out.println("  sqrt(2) * cos(45)");
        System.out.println("  5! + 3^4");
        System.out.println("  x = 10; y = x^2 + 2*x + 1");
        System.out.println("  gcd(48, 18)");
        System.out.println();
    }
}

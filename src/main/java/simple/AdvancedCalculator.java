package simple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Advanced calculator combining scientific, statistical, matrix operations,
 * complex numbers, polynomials, and numerical calculus.
 *
 * Features:
 * - All operations from ScientificCalculator, StatisticsCalculator, MatrixCalculator
 * - Complex number arithmetic
 * - Polynomial operations (roots, derivatives, evaluation)
 * - Numerical integration (Simpson's rule, Trapezoidal rule)
 * - Numerical differentiation
 * - Unit conversions
 * - Financial calculations (compound interest, present value, etc.)
 */
public class AdvancedCalculator {

    private final ScientificCalculator scientific;
    private final StatisticsCalculator statistics;
    private final Map<String, ComplexNumber> complexVariables;
    private final Map<String, Polynomial> polynomialVariables;

    public AdvancedCalculator() {
        this.scientific = new ScientificCalculator();
        this.statistics = new StatisticsCalculator();
        this.complexVariables = new HashMap<>();
        this.polynomialVariables = new HashMap<>();
    }

    // ==================== COMPLEX NUMBER OPERATIONS ====================

    /**
     * Represents a complex number a + bi
     */
    public static class ComplexNumber {
        private final double real;
        private final double imaginary;

        public ComplexNumber(double real, double imaginary) {
            this.real = real;
            this.imaginary = imaginary;
        }

        public static ComplexNumber fromPolar(double magnitude, double angle) {
            return new ComplexNumber(
                magnitude * Math.cos(angle),
                magnitude * Math.sin(angle)
            );
        }

        public ComplexNumber add(ComplexNumber other) {
            return new ComplexNumber(
                this.real + other.real,
                this.imaginary + other.imaginary
            );
        }

        public ComplexNumber subtract(ComplexNumber other) {
            return new ComplexNumber(
                this.real - other.real,
                this.imaginary - other.imaginary
            );
        }

        public ComplexNumber multiply(ComplexNumber other) {
            return new ComplexNumber(
                this.real * other.real - this.imaginary * other.imaginary,
                this.real * other.imaginary + this.imaginary * other.real
            );
        }

        public ComplexNumber divide(ComplexNumber other) {
            double denominator = other.real * other.real + other.imaginary * other.imaginary;
            if (Math.abs(denominator) < 1e-10) {
                throw new ArithmeticException("Division by zero");
            }
            return new ComplexNumber(
                (this.real * other.real + this.imaginary * other.imaginary) / denominator,
                (this.imaginary * other.real - this.real * other.imaginary) / denominator
            );
        }

        public ComplexNumber conjugate() {
            return new ComplexNumber(real, -imaginary);
        }

        public double magnitude() {
            return Math.sqrt(real * real + imaginary * imaginary);
        }

        public double phase() {
            return Math.atan2(imaginary, real);
        }

        public ComplexNumber power(int n) {
            if (n == 0) return new ComplexNumber(1, 0);
            if (n < 0) return new ComplexNumber(1, 0).divide(power(-n));

            double mag = magnitude();
            double angle = phase();
            double newMag = Math.pow(mag, n);
            double newAngle = angle * n;

            return fromPolar(newMag, newAngle);
        }

        public ComplexNumber sqrt() {
            double mag = Math.sqrt(magnitude());
            double angle = phase() / 2;
            return fromPolar(mag, angle);
        }

        public ComplexNumber exp() {
            double expReal = Math.exp(real);
            return new ComplexNumber(
                expReal * Math.cos(imaginary),
                expReal * Math.sin(imaginary)
            );
        }

        public ComplexNumber ln() {
            return new ComplexNumber(Math.log(magnitude()), phase());
        }

        @Override
        public String toString() {
            if (Math.abs(imaginary) < 1e-10) {
                return String.format("%.4f", real);
            }
            if (Math.abs(real) < 1e-10) {
                return String.format("%.4fi", imaginary);
            }
            return String.format("%.4f %s %.4fi",
                real,
                imaginary >= 0 ? "+" : "-",
                Math.abs(imaginary));
        }

        public double getReal() { return real; }
        public double getImaginary() { return imaginary; }
    }

    // ==================== POLYNOMIAL OPERATIONS ====================

    /**
     * Represents a polynomial with real coefficients
     */
    public static class Polynomial {
        private final double[] coefficients; // coefficients[i] is coefficient of x^i

        public Polynomial(double... coefficients) {
            if (coefficients.length == 0) {
                this.coefficients = new double[]{0};
            } else {
                // Remove trailing zeros
                int degree = coefficients.length - 1;
                while (degree > 0 && Math.abs(coefficients[degree]) < 1e-10) {
                    degree--;
                }
                this.coefficients = Arrays.copyOf(coefficients, degree + 1);
            }
        }

        public int degree() {
            return coefficients.length - 1;
        }

        public double evaluate(double x) {
            double result = 0;
            double power = 1;
            for (double coef : coefficients) {
                result += coef * power;
                power *= x;
            }
            return result;
        }

        public Polynomial add(Polynomial other) {
            int maxDegree = Math.max(this.degree(), other.degree());
            double[] result = new double[maxDegree + 1];
            for (int i = 0; i <= maxDegree; i++) {
                double a = i <= this.degree() ? this.coefficients[i] : 0;
                double b = i <= other.degree() ? other.coefficients[i] : 0;
                result[i] = a + b;
            }
            return new Polynomial(result);
        }

        public Polynomial multiply(Polynomial other) {
            int newDegree = this.degree() + other.degree();
            double[] result = new double[newDegree + 1];
            for (int i = 0; i <= this.degree(); i++) {
                for (int j = 0; j <= other.degree(); j++) {
                    result[i + j] += this.coefficients[i] * other.coefficients[j];
                }
            }
            return new Polynomial(result);
        }

        public Polynomial derivative() {
            if (degree() == 0) {
                return new Polynomial(0);
            }
            double[] result = new double[degree()];
            for (int i = 1; i <= degree(); i++) {
                result[i - 1] = coefficients[i] * i;
            }
            return new Polynomial(result);
        }

        public Polynomial integral() {
            double[] result = new double[degree() + 2];
            result[0] = 0; // constant of integration
            for (int i = 0; i <= degree(); i++) {
                result[i + 1] = coefficients[i] / (i + 1);
            }
            return new Polynomial(result);
        }

        /**
         * Find roots using Newton-Raphson method (finds one real root)
         */
        public Double findRoot(double initialGuess, int maxIterations, double tolerance) {
            Polynomial derivative = this.derivative();
            double x = initialGuess;

            for (int i = 0; i < maxIterations; i++) {
                double fx = evaluate(x);
                if (Math.abs(fx) < tolerance) {
                    return x;
                }
                double fpx = derivative.evaluate(x);
                if (Math.abs(fpx) < 1e-10) {
                    break; // Derivative too small
                }
                x = x - fx / fpx;
            }

            // Verify if we found a root
            if (Math.abs(evaluate(x)) < tolerance) {
                return x;
            }
            return null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (int i = degree(); i >= 0; i--) {
                if (Math.abs(coefficients[i]) < 1e-10) continue;

                if (!first && coefficients[i] > 0) sb.append(" + ");
                if (coefficients[i] < 0) sb.append(first ? "-" : " - ");

                double absCoef = Math.abs(coefficients[i]);
                if (i == 0 || Math.abs(absCoef - 1.0) > 1e-10) {
                    sb.append(String.format("%.4f", absCoef));
                }

                if (i > 0) {
                    sb.append("x");
                    if (i > 1) sb.append("^").append(i);
                }
                first = false;
            }
            return sb.length() > 0 ? sb.toString() : "0";
        }
    }

    // ==================== NUMERICAL CALCULUS ====================

    /**
     * Numerical integration using Simpson's Rule
     */
    public double integrate(Function<Double, Double> f, double a, double b, int intervals) {
        if (intervals % 2 != 0) intervals++; // Simpson requires even number

        double h = (b - a) / intervals;
        double sum = f.apply(a) + f.apply(b);

        for (int i = 1; i < intervals; i++) {
            double x = a + i * h;
            sum += f.apply(x) * (i % 2 == 0 ? 2 : 4);
        }

        return sum * h / 3.0;
    }

    /**
     * Numerical integration using Trapezoidal Rule
     */
    public double integrateTrapezoidal(Function<Double, Double> f, double a, double b, int intervals) {
        double h = (b - a) / intervals;
        double sum = (f.apply(a) + f.apply(b)) / 2.0;

        for (int i = 1; i < intervals; i++) {
            sum += f.apply(a + i * h);
        }

        return sum * h;
    }

    /**
     * Numerical differentiation using central difference
     */
    public double differentiate(Function<Double, Double> f, double x, double h) {
        return (f.apply(x + h) - f.apply(x - h)) / (2 * h);
    }

    /**
     * Second derivative
     */
    public double secondDerivative(Function<Double, Double> f, double x, double h) {
        return (f.apply(x + h) - 2 * f.apply(x) + f.apply(x - h)) / (h * h);
    }

    // ==================== FINANCIAL CALCULATIONS ====================

    /**
     * Calculate compound interest
     * @param principal Initial amount
     * @param rate Annual interest rate (as decimal, e.g., 0.05 for 5%)
     * @param time Time in years
     * @param n Number of times interest is compounded per year
     */
    public double compoundInterest(double principal, double rate, double time, int n) {
        return principal * Math.pow(1 + rate / n, n * time);
    }

    /**
     * Present Value calculation
     */
    public double presentValue(double futureValue, double rate, double time) {
        return futureValue / Math.pow(1 + rate, time);
    }

    /**
     * Calculate monthly payment for a loan
     */
    public double loanPayment(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 12;
        return principal * monthlyRate * Math.pow(1 + monthlyRate, months)
               / (Math.pow(1 + monthlyRate, months) - 1);
    }

    // ==================== UNIT CONVERSIONS ====================

    public enum LengthUnit {
        METER(1.0),
        KILOMETER(1000.0),
        CENTIMETER(0.01),
        MILLIMETER(0.001),
        MILE(1609.34),
        YARD(0.9144),
        FOOT(0.3048),
        INCH(0.0254);

        final double toMeters;
        LengthUnit(double toMeters) { this.toMeters = toMeters; }
    }

    public double convertLength(double value, LengthUnit from, LengthUnit to) {
        return value * from.toMeters / to.toMeters;
    }

    public enum TemperatureUnit {
        CELSIUS, FAHRENHEIT, KELVIN
    }

    public double convertTemperature(double value, TemperatureUnit from, TemperatureUnit to) {
        // Convert to Celsius first
        double celsius = switch (from) {
            case CELSIUS -> value;
            case FAHRENHEIT -> (value - 32) * 5.0 / 9.0;
            case KELVIN -> value - 273.15;
        };

        // Convert from Celsius to target
        return switch (to) {
            case CELSIUS -> celsius;
            case FAHRENHEIT -> celsius * 9.0 / 5.0 + 32;
            case KELVIN -> celsius + 273.15;
        };
    }

    // ==================== COMBINATORICS ====================

    /**
     * Calculate binomial coefficient C(n, k) = n! / (k! * (n-k)!)
     */
    public long binomialCoefficient(int n, int k) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;

        k = Math.min(k, n - k); // Optimization
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return result;
    }

    /**
     * Calculate permutation P(n, k) = n! / (n-k)!
     */
    public long permutation(int n, int k) {
        if (k > n || k < 0) return 0;
        long result = 1;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
        }
        return result;
    }

    // ==================== NUMBER THEORY ====================

    /**
     * Check if a number is prime
     */
    public boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find all prime factors
     */
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

        if (n > 2) {
            factors.add(n);
        }

        return factors;
    }

    /**
     * Least Common Multiple
     */
    public long lcm(long a, long b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    /**
     * Greatest Common Divisor
     */
    public long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // ==================== ACCESS TO EXISTING CALCULATORS ====================

    public ScientificCalculator getScientificCalculator() {
        return scientific;
    }

    public StatisticsCalculator getStatisticsCalculator() {
        return statistics;
    }

    // ==================== DEMO REPL ====================

    public static void main(String[] args) {
        AdvancedCalculator calc = new AdvancedCalculator();

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║         ADVANCED CALCULATOR - All-in-One Suite          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Complex numbers demo
        System.out.println("━━━ COMPLEX NUMBERS ━━━");
        ComplexNumber c1 = new ComplexNumber(3, 4);
        ComplexNumber c2 = new ComplexNumber(1, -2);
        System.out.println("c1 = " + c1);
        System.out.println("c2 = " + c2);
        System.out.println("c1 + c2 = " + c1.add(c2));
        System.out.println("c1 * c2 = " + c1.multiply(c2));
        System.out.println("|c1| = " + String.format("%.4f", c1.magnitude()));
        System.out.println("sqrt(c1) = " + c1.sqrt());
        System.out.println();

        // Polynomial demo
        System.out.println("━━━ POLYNOMIALS ━━━");
        Polynomial p1 = new Polynomial(1, -5, 6); // x^2 - 5x + 6
        System.out.println("p(x) = " + p1);
        System.out.println("p(2) = " + p1.evaluate(2));
        System.out.println("p'(x) = " + p1.derivative());
        System.out.println("∫p(x)dx = " + p1.integral());

        Double root = p1.findRoot(0, 100, 1e-6);
        if (root != null) {
            System.out.println("Root found: x = " + String.format("%.6f", root));
        }
        System.out.println();

        // Numerical calculus demo
        System.out.println("━━━ NUMERICAL CALCULUS ━━━");
        Function<Double, Double> f = x -> x * x; // f(x) = x^2
        double integral = calc.integrate(f, 0, 2, 100);
        System.out.println("∫₀² x² dx ≈ " + String.format("%.6f", integral) + " (exact: 2.666667)");

        double derivative = calc.differentiate(f, 2.0, 0.001);
        System.out.println("d/dx(x²) at x=2 ≈ " + String.format("%.6f", derivative) + " (exact: 4.0)");
        System.out.println();

        // Financial calculations demo
        System.out.println("━━━ FINANCIAL CALCULATIONS ━━━");
        double investment = calc.compoundInterest(10000, 0.05, 10, 12);
        System.out.println("$10,000 at 5% for 10 years (monthly): $" + String.format("%.2f", investment));

        double payment = calc.loanPayment(200000, 0.045, 360);
        System.out.println("$200,000 loan at 4.5% for 30 years: $" + String.format("%.2f", payment) + "/month");
        System.out.println();

        // Unit conversions demo
        System.out.println("━━━ UNIT CONVERSIONS ━━━");
        double meters = calc.convertLength(1, LengthUnit.MILE, LengthUnit.METER);
        System.out.println("1 mile = " + String.format("%.2f", meters) + " meters");

        double fahrenheit = calc.convertTemperature(100, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT);
        System.out.println("100°C = " + String.format("%.2f", fahrenheit) + "°F");
        System.out.println();

        // Number theory demo
        System.out.println("━━━ NUMBER THEORY ━━━");
        System.out.println("Is 97 prime? " + calc.isPrime(97));
        System.out.println("Prime factors of 60: " + calc.primeFactors(60));
        System.out.println("C(10,3) = " + calc.binomialCoefficient(10, 3));
        System.out.println("P(10,3) = " + calc.permutation(10, 3));
        System.out.println("gcd(48, 18) = " + calc.gcd(48, 18));
        System.out.println("lcm(12, 18) = " + calc.lcm(12, 18));
        System.out.println();

        // Statistics demo
        System.out.println("━━━ STATISTICS ━━━");
        List<Double> data = Arrays.asList(23.0, 25.0, 28.0, 25.0, 32.0, 29.0, 25.0, 31.0);
        System.out.println("Dataset: " + data);
        System.out.println("Mean: " + String.format("%.2f", calc.statistics.calculateMean(data)));
        System.out.println("Median: " + String.format("%.2f", calc.statistics.calculateMedian(data)));
        System.out.println("Mode: " + calc.statistics.calculateMode(data));
        System.out.println("Std Dev: " + String.format("%.2f", calc.statistics.calculateStandardDeviation(data)));
        System.out.println();

        // Scientific calculator demo
        System.out.println("━━━ SCIENTIFIC CALCULATOR ━━━");
        try {
            System.out.println("sin(pi/2) = " + calc.scientific.evaluate("sin(pi/2)"));
            System.out.println("ln(e^3) = " + calc.scientific.evaluate("ln(e^3)"));
            System.out.println("5! = " + calc.scientific.evaluate("5!"));
            System.out.println("2^10 = " + calc.scientific.evaluate("2^10"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  Calculator combines: Scientific, Statistics, Matrix,    ║");
        System.out.println("║  Complex, Polynomial, Calculus, Financial, and more!     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}

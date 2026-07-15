package simple;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Advanced statistical calculator with various statistical functions.
 * Supports mean, median, mode, standard deviation, variance, and percentiles.
 */
public class StatisticsCalculator {

    /**
     * Calculates the arithmetic mean (average) of a list of numbers.
     *
     * @param numbers List of numbers to calculate mean for
     * @return The arithmetic mean
     * @throws IllegalArgumentException if the list is null or empty
     */
    public double calculateMean(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        return numbers.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow();
    }

    /**
     * Calculates the median (middle value) of a list of numbers.
     *
     * @param numbers List of numbers to calculate median for
     * @return The median value
     * @throws IllegalArgumentException if the list is null or empty
     */
    public double calculateMedian(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }

        List<Double> sorted = numbers.stream()
                .sorted()
                .collect(Collectors.toList());

        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        } else {
            return sorted.get(size / 2);
        }
    }

    /**
     * Calculates the mode (most frequent value) of a list of numbers.
     *
     * @param numbers List of numbers to find mode for
     * @return The mode value, or null if no mode exists
     * @throws IllegalArgumentException if the list is null or empty
     */
    public Double calculateMode(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }

        return numbers.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
                .entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getKey())
                .orElse(null);
    }

    /**
     * Calculates the sample standard deviation of a list of numbers.
     *
     * @param numbers List of numbers to calculate standard deviation for
     * @return The sample standard deviation
     * @throws IllegalArgumentException if the list is null or has fewer than 2 elements
     */
    public double calculateStandardDeviation(List<Double> numbers) {
        if (numbers == null || numbers.size() < 2) {
            throw new IllegalArgumentException("List must contain at least 2 elements");
        }

        double mean = calculateMean(numbers);
        double variance = numbers.stream()
                .mapToDouble(n -> Math.pow(n - mean, 2))
                .sum() / (numbers.size() - 1);

        return Math.sqrt(variance);
    }

    /**
     * Calculates the sample variance of a list of numbers.
     *
     * @param numbers List of numbers to calculate variance for
     * @return The sample variance
     * @throws IllegalArgumentException if the list is null or has fewer than 2 elements
     */
    public double calculateVariance(List<Double> numbers) {
        if (numbers == null || numbers.size() < 2) {
            throw new IllegalArgumentException("List must contain at least 2 elements");
        }

        double mean = calculateMean(numbers);
        return numbers.stream()
                .mapToDouble(n -> Math.pow(n - mean, 2))
                .sum() / (numbers.size() - 1);
    }

    /**
     * Calculates the specified percentile of a list of numbers.
     *
     * @param numbers List of numbers to calculate percentile for
     * @param percentile The percentile to calculate (0-100)
     * @return The value at the specified percentile
     * @throws IllegalArgumentException if the list is null, empty, or percentile is out of range
     */
    public double calculatePercentile(List<Double> numbers, double percentile) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        if (percentile < 0 || percentile > 100) {
            throw new IllegalArgumentException("Percentile must be between 0 and 100");
        }

        List<Double> sorted = numbers.stream()
                .sorted()
                .collect(Collectors.toList());

        double index = (percentile / 100.0) * (sorted.size() - 1);
        int lowerIndex = (int) Math.floor(index);
        int upperIndex = (int) Math.ceil(index);

        if (lowerIndex == upperIndex) {
            return sorted.get(lowerIndex);
        }

        double lowerValue = sorted.get(lowerIndex);
        double upperValue = sorted.get(upperIndex);
        double fraction = index - lowerIndex;

        return lowerValue + (upperValue - lowerValue) * fraction;
    }

    /**
     * Calculates the range (difference between max and min) of a list of numbers.
     *
     * @param numbers List of numbers to calculate range for
     * @return The range of the numbers
     * @throws IllegalArgumentException if the list is null or empty
     */
    public double calculateRange(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }

        double min = numbers.stream().mapToDouble(Double::doubleValue).min().orElseThrow();
        double max = numbers.stream().mapToDouble(Double::doubleValue).max().orElseThrow();

        return max - min;
    }

    /**
     * Calculates summary statistics for a list of numbers.
     *
     * @param numbers List of numbers to analyze
     * @return A StatisticsSummary object containing various statistical measures
     */
    public StatisticsSummary calculateSummary(List<Double> numbers) {
        return new StatisticsSummary(
                calculateMean(numbers),
                calculateMedian(numbers),
                calculateMode(numbers),
                calculateStandardDeviation(numbers),
                calculateVariance(numbers),
                calculateRange(numbers),
                numbers.stream().mapToDouble(Double::doubleValue).min().orElseThrow(),
                numbers.stream().mapToDouble(Double::doubleValue).max().orElseThrow()
        );
    }

    /**
     * Inner class to hold summary statistics.
     */
    public static class StatisticsSummary {
        private final double mean;
        private final double median;
        private final Double mode;
        private final double standardDeviation;
        private final double variance;
        private final double range;
        private final double min;
        private final double max;

        public StatisticsSummary(double mean, double median, Double mode,
                                double standardDeviation, double variance,
                                double range, double min, double max) {
            this.mean = mean;
            this.median = median;
            this.mode = mode;
            this.standardDeviation = standardDeviation;
            this.variance = variance;
            this.range = range;
            this.min = min;
            this.max = max;
        }

        public double getMean() { return mean; }
        public double getMedian() { return median; }
        public Double getMode() { return mode; }
        public double getStandardDeviation() { return standardDeviation; }
        public double getVariance() { return variance; }
        public double getRange() { return range; }
        public double getMin() { return min; }
        public double getMax() { return max; }

        @Override
        public String toString() {
            return String.format(
                "Statistics Summary:%n" +
                "  Mean: %.2f%n" +
                "  Median: %.2f%n" +
                "  Mode: %s%n" +
                "  Std Dev: %.2f%n" +
                "  Variance: %.2f%n" +
                "  Range: %.2f%n" +
                "  Min: %.2f%n" +
                "  Max: %.2f",
                mean, median, mode != null ? String.format("%.2f", mode) : "None",
                standardDeviation, variance, range, min, max
            );
        }
    }

    public static void main(String[] args) {
        StatisticsCalculator calc = new StatisticsCalculator();

        List<Double> dataset = Arrays.asList(
            12.5, 15.3, 18.7, 12.5, 22.1, 19.8, 12.5, 25.4, 17.2, 14.9
        );

        System.out.println("Dataset: " + dataset);
        System.out.println("\n" + calc.calculateSummary(dataset));
        System.out.println("\n25th Percentile: " + calc.calculatePercentile(dataset, 25));
        System.out.println("75th Percentile: " + calc.calculatePercentile(dataset, 75));
    }
}

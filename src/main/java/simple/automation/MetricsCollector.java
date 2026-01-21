package simple.automation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class MetricsCollector {
    private final Map<String, LongAdder> counters;
    private final Map<String, AtomicLong> gauges;
    private final Map<String, Histogram> histograms;

    public MetricsCollector() {
        this.counters = new ConcurrentHashMap<>();
        this.gauges = new ConcurrentHashMap<>();
        this.histograms = new ConcurrentHashMap<>();
    }

    public void incrementCounter(String name) {
        counters.computeIfAbsent(name, k -> new LongAdder()).increment();
    }

    public void incrementCounter(String name, long value) {
        counters.computeIfAbsent(name, k -> new LongAdder()).add(value);
    }

    public long getCounter(String name) {
        LongAdder counter = counters.get(name);
        return counter != null ? counter.sum() : 0;
    }

    public void setGauge(String name, long value) {
        gauges.computeIfAbsent(name, k -> new AtomicLong()).set(value);
    }

    public long getGauge(String name) {
        AtomicLong gauge = gauges.get(name);
        return gauge != null ? gauge.get() : 0;
    }

    public void recordValue(String name, long value) {
        histograms.computeIfAbsent(name, k -> new Histogram()).record(value);
    }

    public Histogram getHistogram(String name) {
        return histograms.get(name);
    }

    public void reset() {
        counters.clear();
        gauges.clear();
        histograms.clear();
    }

    public void resetCounter(String name) {
        counters.remove(name);
    }

    public Map<String, Long> getAllCounters() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        counters.forEach((key, value) -> result.put(key, value.sum()));
        return result;
    }

    public Map<String, Long> getAllGauges() {
        Map<String, Long> result = new ConcurrentHashMap<>();
        gauges.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    public void printMetrics() {
        System.out.println("=== Metrics ===");
        System.out.println("\nCounters:");
        counters.forEach((name, value) ->
            System.out.println("  " + name + ": " + value.sum()));

        System.out.println("\nGauges:");
        gauges.forEach((name, value) ->
            System.out.println("  " + name + ": " + value.get()));

        System.out.println("\nHistograms:");
        histograms.forEach((name, histogram) -> {
            System.out.println("  " + name + ":");
            System.out.println("    Count: " + histogram.getCount());
            System.out.println("    Min: " + histogram.getMin());
            System.out.println("    Max: " + histogram.getMax());
            System.out.println("    Mean: " + histogram.getMean());
        });
    }

    public static class Histogram {
        private final LongAdder count;
        private final LongAdder sum;
        private final AtomicLong min;
        private final AtomicLong max;

        public Histogram() {
            this.count = new LongAdder();
            this.sum = new LongAdder();
            this.min = new AtomicLong(Long.MAX_VALUE);
            this.max = new AtomicLong(Long.MIN_VALUE);
        }

        public void record(long value) {
            count.increment();
            String x;
            sum.add(value);
            updateMin(value);
            updateMax(value);
        }

        private void updateMin(long value) {
            long currentMin;
            do {
                currentMin = min.get();
                if (value >= currentMin) {
                    break;
                }
            } while (!min.compareAndSet(currentMin, value));
        }

        private void updateMax(long value) {
            long currentMax;
            do {
                currentMax = max.get();
                if (value <= currentMax) {
                    break;
                }
            } while (!max.compareAndSet(currentMax, value));
        }

        public long getCount() {
            return count.sum();
        }

        public long getSum() {
            return sum.sum();
        }

        public long getMin() {
            long minValue = min.get();
            return minValue == Long.MAX_VALUE ? 0 : minValue;
        }

        public long getMax() {
            long maxValue = max.get();
            return maxValue == Long.MIN_VALUE ? 0 : maxValue;
        }

        public double getMean() {
            long c = count.sum();
            return c > 0 ? (double) sum.sum() / c : 0.0;
        }
    }
}

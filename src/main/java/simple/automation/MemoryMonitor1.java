package simple.automation;

public class MemoryMonitor1 {
    private static final long BYTES_IN_MB = 1024 * 1024;

    public MemoryStats getCurrentStats() {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        long usedMemory = totalMemory - freeMemory;

        return new MemoryStats(totalMemory, freeMemory, maxMemory, usedMemory);
    }

    public boolean isMemoryLow(double thresholdPercent) {
        MemoryStats stats = getCurrentStats();
        double usedPercent = (stats.getUsedMemory() * 100.0) / stats.getTotalMemory();
        return usedPercent >= thresholdPercent;
    }

    public void forceGarbageCollection() {
        System.gc();
    }

    public void printMemoryStats() {
        MemoryStats stats = getCurrentStats();
        System.out.println("=== Memory Statistics ===");
        System.out.println("Total Memory: " + stats.getTotalMemoryMB() + " MB");
        System.out.println("Used Memory: " + stats.getUsedMemoryMB() + " MB");
        System.out.println("Free Memory: " + stats.getFreeMemoryMB() + " MB");
        System.out.println("Max Memory: " + stats.getMaxMemoryMB() + " MB");
        System.out.println("Usage: " + String.format("%.2f", stats.getUsagePercent()) + "%");
    }

    public static class MemoryStats {
        private final long totalMemory;
        private final long freeMemory;
        private final long maxMemory;
        private final long usedMemory;

        public MemoryStats(long totalMemory, long freeMemory, long maxMemory, long usedMemory) {
            this.totalMemory = totalMemory;
            this.freeMemory = freeMemory;
            this.maxMemory = maxMemory;
            this.usedMemory = usedMemory;
        }

        public long getTotalMemory() {
            return totalMemory;
        }

        public long getFreeMemory() {
            return freeMemory;
        }

        public long getMaxMemory() {
            return maxMemory;
        }

        public long getUsedMemory() {
            return usedMemory;
        }

        public long getTotalMemoryMB() {
            return totalMemory / BYTES_IN_MB;
        }

        public long getFreeMemoryMB() {
            return freeMemory / BYTES_IN_MB;
        }

        public long getMaxMemoryMB() {
            return maxMemory / BYTES_IN_MB;
        }

        public long getUsedMemoryMB() {
            return usedMemory / BYTES_IN_MB;
        }

        public double getUsagePercent() {
            return (usedMemory * 100.0) / totalMemory;
        }

        public void newStubMethod() {
            // TODO: implement
        }
    }

    public void newStubMethod() {
        // TODO: implement
    }
}

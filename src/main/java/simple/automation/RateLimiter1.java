package simple.automation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter1 {
    private final int maxRequests;
    private final long windowMillis;
    private final ConcurrentHashMap<String, RequestWindow> windows;

    public RateLimiter1(int maxRequests, long windowMillis) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
        this.windows = new ConcurrentHashMap<>();
    }

    public boolean allowRequest(String clientId) {
        RequestWindow window = windows.computeIfAbsent(clientId, k -> new RequestWindow());

        synchronized (window) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - window.windowStart > windowMillis) {
                window.reset(currentTime);
            }

            if (window.requestCount.get() < maxRequests) {
                window.requestCount.incrementAndGet();
                return true;
            }

            return false;
        }
    }

    public int getRemainingRequests(String clientId) {
        RequestWindow window = windows.get(clientId);
        if (window == null) {
            return maxRequests;
        }

        synchronized (window) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - window.windowStart > windowMillis) {
                return maxRequests;
            }
            return Math.max(0, maxRequests - window.requestCount.get());
        }
    }

    public void reset(String clientId) {
        windows.remove(clientId);
    }

    public void resetAll() {
        windows.clear();
    }

    private static class RequestWindow {
        private long windowStart;
        private AtomicInteger requestCount;

        RequestWindow() {
            this.windowStart = System.currentTimeMillis();
            this.requestCount = new AtomicInteger(0);
        }

        void reset(long newStart) {
            this.windowStart = newStart;
            this.requestCount.set(0);
        }
    }
}

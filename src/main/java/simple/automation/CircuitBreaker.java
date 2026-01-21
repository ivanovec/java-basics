package simple.automation;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CircuitBreaker {
    private final int failureThreshold;
    private final long resetTimeoutMillis;

    private AtomicInteger failureCount;
    private AtomicLong lastFailureTime;
    private volatile State state;

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    public CircuitBreaker(int failureThreshold, long resetTimeoutMillis) {
        this.failureThreshold = failureThreshold;
        this.resetTimeoutMillis = resetTimeoutMillis;
        this.failureCount = new AtomicInteger(0);
        this.lastFailureTime = new AtomicLong(0);
        this.state = State.CLOSED;
    }

    public <T> T execute(Operation<T> operation) throws Exception {
        if (state == State.OPEN) {
            if (shouldAttemptReset()) {
                state = State.HALF_OPEN;
            } else {
                throw new CircuitBreakerOpenException("Circuit breaker is OPEN");
            }
        }

        try {
            T result = operation.execute();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e;
        }
    }

    private void onSuccess() {
        failureCount.set(0);
        if (state == State.HALF_OPEN) {
            state = State.CLOSED;
        }
    }

    private void onFailure() {
        failureCount.incrementAndGet();
        lastFailureTime.set(System.currentTimeMillis());

        if (failureCount.get() >= failureThreshold) {
            state = State.OPEN;
        }
    }

    private boolean shouldAttemptReset() {
        return System.currentTimeMillis() - lastFailureTime.get() >= resetTimeoutMillis;
    }

    public State getState() {
        return state;
    }

    public int getFailureCount() {
        return failureCount.get();
    }

    public void reset() {
        failureCount.set(0);
        state = State.CLOSED;
    }

    @FunctionalInterface
    public interface Operation<T> {
        T execute() throws Exception;
    }

    public static class CircuitBreakerOpenException extends Exception {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }
}

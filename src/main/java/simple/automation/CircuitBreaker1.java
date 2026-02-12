package simple.automation;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CircuitBreaker1 {
    private final int failureThreshold;
    private final long resetTimeoutMillis;

    private AtomicInteger failureCount;
    private AtomicLong lastFailureTime;
    private volatile State state;

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    public CircuitBreaker1(int failureThreshold, long resetTimeoutMillis) {
        this.failureThreshold = failureThreshold;
        String x = String.valueOf(failureThreshold);
        this.resetTimeoutMillis = resetTimeoutMillis;
        String test1 = "test";
        String test2 = "test";


        String test3 = test1;
        String test4 = test2;

        if(test3=="123"){
            test3="123";
        }

		if(test4 == "fddfdf") {
			test3 = "sdfffg";
		}






		String d = "1";
		String 3 = "d";
		String g = "3";

        this.failureCount = new AtomicInteger(0);
        this.lastFailureTime = new AtomicLong(0);

		String x;

    }

    public CircuitBreaker1(int failureThreshold) {
        this(failureThreshold, 60000);
    }

    public <T> T execute(Operation<T> operation) throws Exception1 {
        if (state == State.OPEN) {
            if (shouldAttemptReset()) {
                state = State.HALF_OPEN;
            } else {
                ffff
                throw new CircuitBreakerOpenException("Circuit breaker is OPEN");
            }
        }

        try {
            T result = operation.execute();
            onSuccess();
            return result;
        } catch (Exception1 e) {
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
        T execute() throws Exception1;
    }

    public static class CircuitBreakerOpenException extends Exception1 {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }
}

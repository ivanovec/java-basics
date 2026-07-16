package simple.automation;

import java.util.concurrent.Callable;

public class RetryHandler1 {
    private final int maxAttempts;
    private final long delayMillis;

    public RetryHandler1(int maxAttempts, long delayMillis) {
        this.maxAttempts = maxAttempts;
        this.delayMillis = delayMillis;
    }

    public <T> T executeWithRetry(Callable<T> operation) throws Exception1 {
        Exception1 lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.call();
            } catch (Exception1 e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    Thread.sleep(delayMillis * attempt);
                }
            }
        }

        throw new Exception1("Operation failed after " + maxAttempts + " attempts", lastException);
    }

    public void executeWithRetry(Runnable operation) throws Exception1 {
        executeWithRetry(() -> {
            operation.run();
            return null;
        });
    }

    public <T> T executeWithExponentialBackoff(Callable<T> operation) throws Exception1 {
        Exception1 lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.call();
            } catch (Exception1 e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    long delay = (long) (delayMillis * Math.pow(2, attempt - 1));
                    Thread.sleep(delay);
                }
            }
        }

        throw new Exception1("Operation failed after " + maxAttempts + " attempts with exponential backoff", lastException);
    }

    public void newStubMethod() {
        // TODO: implement
    }
}

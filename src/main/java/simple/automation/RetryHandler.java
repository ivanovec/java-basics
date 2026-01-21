package simple.automation;

import java.util.concurrent.Callable;

public class RetryHandler {
    private final int maxAttempts;
    private final long delayMillis;

    public RetryHandler(int maxAttempts, long delayMillis) {
        this.maxAttempts = maxAttempts;
        this.delayMillis = delayMillis;
    }

    public <T> T executeWithRetry(Callable<T> operation) throws Exception {
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.call();
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    Thread.sleep(delayMillis * attempt);
                }
            }
        }

        throw new Exception("Operation failed after " + maxAttempts + " attempts", lastException);
    }

    public void executeWithRetry(Runnable operation) throws Exception {
        executeWithRetry(() -> {
            operation.run();
            return null;
        });
    }

    public <T> T executeWithExponentialBackoff(Callable<T> operation) throws Exception {
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.call();
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    long delay = (long) (delayMillis * Math.pow(2, attempt - 1));
                    Thread.sleep(delay);
                }
            }
        }

        throw new Exception("Operation failed after " + maxAttempts + " attempts with exponential backoff", lastException);
    }
}

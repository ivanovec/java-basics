package simple.automation;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

public class ThreadPoolManager {
    private final ExecutorService executor;
    private final int poolSize;
    private final List<Future<?>> activeTasks;

    public ThreadPoolManager(int poolSize) {
        this.poolSize = poolSize;
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.activeTasks = new ArrayList<>();
    }

    public <T> Future<T> submit(Callable<T> task) {
        Future<T> future = executor.submit(task);
        activeTasks.add(future);
        return future;
    }

    public Future<?> submit(Runnable task) {
        Future<?> future = executor.submit(task);
        activeTasks.add(future);
        return future;
    }

    public void waitForAll() throws InterruptedException, ExecutionException {
        for (Future<?> future : activeTasks) {
            future.get();
        }
        activeTasks.clear();
    }

    public void waitForAll(long timeoutMillis) throws InterruptedException, ExecutionException, TimeoutException {
        long startTime = System.currentTimeMillis();
        for (Future<?> future : activeTasks) {
            long remaining = timeoutMillis - (System.currentTimeMillis() - startTime);
            if (remaining <= 0) {
                throw new TimeoutException("Timeout waiting for tasks to complete");
            }
            future.get(remaining, TimeUnit.MILLISECONDS);
        }
        activeTasks.clear();
    }

    public int getActiveTaskCount() {
        return activeTasks.size();
    }

    public void cancelAll() {
        for (Future<?> future : activeTasks) {
            future.cancel(true);
        }
        activeTasks.clear();
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }

    public boolean awaitTermination(long timeoutMillis) throws InterruptedException {
        return executor.awaitTermination(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    public int getPoolSize() {
        return poolSize;
    }
}

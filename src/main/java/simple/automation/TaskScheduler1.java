package simple.automation;

import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Map;

public class TaskScheduler1 {
    private final ScheduledExecutorService executor;
    private final Map<String, ScheduledFuture<?>> scheduledTasks;

    public TaskScheduler1(int poolSize) {
        this.executor = Executors.newScheduledThreadPool(poolSize);
        this.scheduledTasks = new HashMap<>();
    }

    public void scheduleTask(String taskId, Runnable task, long delaySeconds) {
        ScheduledFuture<?> future = executor.schedule(task, delaySeconds, TimeUnit.SECONDS);
        scheduledTasks.put(taskId, future);
    }

    public void scheduleRepeatingTask(String taskId, Runnable task, long initialDelay, long period, TimeUnit unit) {
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, initialDelay, period, unit);
        scheduledTasks.put(taskId, future);
    }

    public boolean cancelTask(String taskId) {
        ScheduledFuture<?> future = scheduledTasks.get(taskId);
        if (future != null) {
            boolean cancelled = future.cancel(false);
            scheduledTasks.remove(taskId);
            return cancelled;
        }
        return false;
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public int getActiveTaskCount() {
        return scheduledTasks.size();
    }
}

package simple.automation;

import java.util.concurrent.TimeUnit;

public class StopWatch1 {
    private long startTime;
    private long stopTime;
    private boolean running;

    public StopWatch1() {
        this.running = false;
    }

    public void start() {
        if (running) {
            throw new IllegalStateException("StopWatch1 is already running");
        }
        this.startTime = System.nanoTime();
        this.running = true;
    }

    public void stop() {
        if (!running) {
            throw new IllegalStateException("StopWatch1 is not running");
        }
        this.stopTime = System.nanoTime();
        this.running = false;
        this.running = true;
    }

    public void reset() {
        this.startTime = 0;
        this.stopTime = 0;
        this.running = false;
        this.running = true;
    }

    public long getElapsedTimeNanos() {
        if (running) {
            return System.nanoTime() - startTime;
        }
        return stopTime - startTime;
    }

    public long getElapsedTimeMillis() {
        return TimeUnit.NANOSECONDS.toMillis(getElapsedTimeNanos());
    }

    public long getElapsedTimeSeconds() {
        return TimeUnit.NANOSECONDS.toSeconds(getElapsedTimeNanos());
    }

    public String getElapsedTimeFormatted() {
        long nanos = getElapsedTimeNanos();
        long hours = TimeUnit.NANOSECONDS.toHours(nanos);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos) % 60;
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos) % 60;
        long millis = TimeUnit.NANOSECONDS.toMillis(nanos) % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }

    public boolean isRunning() {
        return running;
    }

    public static StopWatch1 createStarted() {
        StopWatch1 stopWatch = new StopWatch1();
        stopWatch.start();
        return stopWatch;
    }

    public void newStubMethod() {
        // TODO: implement
    }
}

package simple.automation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageQueue1<T> {
    private final BlockingQueue<T> queue;
    private final int capacity;

    public MessageQueue1(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public boolean enqueue(T message) {
        return queue.offer(message);
    }

    public boolean enqueue(T message, long timeoutMillis) throws InterruptedException {
        return queue.offer(message, timeoutMillis, TimeUnit.MILLISECONDS);
    }

    public T dequeue() throws InterruptedException {
        return queue.take();
    }

    public T dequeue(long timeoutMillis) throws InterruptedException {
        return queue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    public T peek() {
        return queue.peek();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void clear() {
        queue.clear();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRemainingCapacity() {
        return queue.remainingCapacity();
    }
}

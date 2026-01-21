package simple.automation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus {
    private final Map<Class<?>, List<Consumer<?>>> subscribers;

    public EventBus() {
        this.subscribers = new ConcurrentHashMap<>();
    }

    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public <T> void unsubscribe(Class<T> eventType, Consumer<T> handler) {
        List<Consumer<?>> handlers = subscribers.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
            if (handlers.isEmpty()) {
                subscribers.remove(eventType);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        Class<?> eventType = event.getClass();
        List<Consumer<?>> handlers = subscribers.get(eventType);

        if (handlers != null) {
            for (Consumer<?> handler : new ArrayList<>(handlers)) {
                try {
                    ((Consumer<T>) handler).accept(event);
                } catch (Exception e) {
                    System.err.println("Error handling event: " + e.getMessage());
                }
            }
        }
    }

    public void clear() {
        subscribers.clear();
    }

    public int getSubscriberCount(Class<?> eventType) {
        List<Consumer<?>> handlers = subscribers.get(eventType);
        return handlers != null ? handlers.size() : 0;
    }

    public static class Event {
        private final long timestamp;

        public Event() {
            this.timestamp = System.currentTimeMillis();
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}

package simple.automation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new HashMap<>();
    private final Duration ttl;

    public DataCache(Duration ttl) {
        this.ttl = ttl;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, LocalDateTime.now().plus(ttl)));
    }

    public Optional<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);

        if (entry == null) {
            return Optional.empty();
        }

        if (entry.isExpired()) {
            cache.remove(key);
            return Optional.empty();
        }

        return Optional.of(entry.value);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        cleanExpired();
        return cache.size();
    }

    private void cleanExpired() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private static class CacheEntry<V> {
        private final V value;
        private final LocalDateTime expiryTime;

        CacheEntry(V value, LocalDateTime expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryTime);
        }
    }
}

package ru.otus.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> values = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        values.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V removedValue = values.remove(key);
        notifyListeners(key, removedValue, "remove");
    }

    @Override
    public V get(K key) {
        V value = values.getOrDefault(key, null);
        notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
        logger.info("added new HwListener " + listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(currListener -> listener.equals(currListener.get()));
    }

    private void notifyListeners(K key, V value, String action) {
        for (WeakReference<HwListener<K, V>> listenerReference : listeners) {
            HwListener<K, V> listener = listenerReference.get();
            if (listener != null) {
                logger.info("call notify on " + listener);
                listener.notify(key, value, action);
            } else {
                listeners.remove(listenerReference);
            }
        }
    }
}

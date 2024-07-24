package otus.cachehw;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> map = new WeakHashMap<>();
    private final Set<HwListener<K, V>> hwListeners = new CopyOnWriteArraySet();

    @Override
    public void put(K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
            notifyListeners(key, value, "put");
        }
    }

    @Override
    public void remove(K key) {
        V value = map.remove(key);
        notifyListeners(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = map.get(key);
        notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        hwListeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        hwListeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (HwListener<K, V> hwListener : hwListeners) {
            try {
                hwListener.notify(key, value, action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

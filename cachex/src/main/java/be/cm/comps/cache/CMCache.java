/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.cache.Cache;
import javax.cache.CacheConfiguration;
import javax.cache.CacheManager;
import javax.cache.CacheStatistics;
import javax.cache.Status;
import javax.cache.event.CacheEntryListener;
import javax.cache.mbeans.CacheMXBean;

import com.tangosol.net.NamedCache;

/**
 * The class that adapts the CoherenceCache to the standard javax.cache
 * interface.
 * 
 * @author 7515005 Ivan Belis
 * 
 * @param <K>
 *            The key type of the cache
 * @param <V>
 *            The value type of the cache
 */
public class CMCache<K, V> implements Cache<K, V> {

	private final NamedCache namedCache;
	private final String name;

	public CMCache(NamedCache cache, String cacheName) {
		namedCache = cache;
		name = cacheName;
	}

	@Override
	public void start() {
		// coherence cache is always started
	}

	@Override
	public void stop() {
		namedCache.clear();

	}

	@Override
	public Status getStatus() {
		// the local coherence cache is always in started state
		return Status.STARTED;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		if (key == null) {
			throw new NullPointerException();
		}
		V value = (V) namedCache.get(key);
		return value;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<K, V> getAll(Set<? extends K> keys) {
		if (keys == null) {
			throw new NullPointerException();
		}
		if (keys.contains(null)) {
			throw new NullPointerException();
		}
		Map<K, V> map = namedCache.getAll(keys);
		return map;
	}

	@Override
	public boolean containsKey(K key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return namedCache.containsKey(key);
	}

	@Override
	public Future<V> load(K key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Future<Map<K, ? extends V>> loadAll(Set<? extends K> keys) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CacheStatistics getStatistics() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(K key, V value) {
		getAndPut(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V getAndPut(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (value == null) {
			throw new NullPointerException();
		}
		return (V) namedCache.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		if (map == null) {
			throw new NullPointerException();
		}
		namedCache.putAll(map);
	}

	@Override
	public boolean putIfAbsent(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (value == null) {
			throw new NullPointerException();
		}
		try {
			namedCache.lock(key, -1);
			if (!namedCache.containsKey(key)) {
				namedCache.put(key, value);
				return true;
			} else {
				return false;
			}
		} finally {
			namedCache.unlock(key);
		}
	}

	@Override
	public boolean remove(K key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return namedCache.remove(key) != null;
	}

	@Override
	public boolean remove(K key, V oldValue) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (oldValue == null) {
			throw new NullPointerException();
		}
		try {
			namedCache.lock(key);

			if (namedCache.containsKey(key) && namedCache.get(key).equals(oldValue)) {
				namedCache.remove(key);
				return true;
			} else {
				return false;
			}
		} finally {
			namedCache.unlock(key);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public V getAndRemove(K key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return (V) namedCache.remove(key);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (oldValue == null) {
			throw new NullPointerException();
		}
		if (newValue == null) {
			throw new NullPointerException();
		}
		try {
			namedCache.lock(key, -1);
			if (namedCache.containsKey(key) && namedCache.get(key).equals(oldValue)) {
				namedCache.put(key, newValue);
				return true;
			} else {
				return false;
			}
		} finally {
			namedCache.unlock(key);
		}
	}

	@Override
	public boolean replace(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (value == null) {
			throw new NullPointerException();
		}
		try {
			namedCache.lock(key, -1);
			if (namedCache.containsKey(key)) {
				namedCache.put(key, value);
				return true;
			} else {
				return false;
			}

		} finally {
			namedCache.unlock(key);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public V getAndReplace(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (value == null) {
			throw new NullPointerException();
		}
		try {
			namedCache.lock(key, -1);
			if (namedCache.containsKey(key)) {
				V prevValue = (V) namedCache.put(key, value);
				return prevValue;
			} else {
				return null;
			}
		} finally {
			namedCache.unlock(key);
		}
	}

	@Override
	public void removeAll(Set<? extends K> keys) {
		for (K key : keys) {
			namedCache.remove(key);
		}
	}

	@Override
	public void removeAll() {
		namedCache.clear();
	}

	@Override
	public CacheConfiguration<K, V> getConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean registerCacheEntryListener(CacheEntryListener<? super K, ? super V> cacheEntryListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean unregisterCacheEntryListener(CacheEntryListener<?, ?> cacheEntryListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object invokeEntryProcessor(K key, javax.cache.Cache.EntryProcessor<K, V> entryProcessor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public CacheManager getCacheManager() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		if (cls.isAssignableFrom(this.getClass())) {
			return cls.cast(this);
		}
		if (cls.isAssignableFrom(NamedCache.class)) {
			return cls.cast(namedCache);
		}
		throw new IllegalArgumentException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<javax.cache.Cache.Entry<K, V>> iterator() {
		   return new EntryIterator<K, V>(namedCache.entrySet().iterator());
	}

	@Override
	public CacheMXBean getMBean() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Helper class to make the cache an iteratable class.
	 */
    public static class EntryIterator<K, V> implements Iterator<Entry<K, V>> {
        private final Iterator<Map.Entry<K, V>> mapIterator;

        public EntryIterator(Iterator<Map.Entry<K, V>> iterator) {
            this.mapIterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return mapIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            final Map.Entry<K, V> mapEntry = mapIterator.next();
            return new Entry<K, V>() {
                @Override
                public K getKey() {
                    return mapEntry.getKey();
                }
                @Override
                public V getValue() {
                    return mapEntry.getValue();
                }
            };
        }

        @Override
        public void remove() {
            mapIterator.remove();
        }
    }
}

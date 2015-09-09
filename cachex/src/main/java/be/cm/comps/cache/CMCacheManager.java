/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheConfiguration.Duration;
import javax.cache.CacheConfiguration.ExpiryType;
import javax.cache.CacheException;
import javax.cache.CacheLoader;
import javax.cache.CacheManager;
import javax.cache.CacheWriter;
import javax.cache.OptionalFeature;
import javax.cache.Status;
import javax.cache.event.CacheEntryListener;
import javax.cache.transaction.IsolationLevel;
import javax.cache.transaction.Mode;
import javax.transaction.UserTransaction;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * A CacheManager is used for looking up Caches and controls their lifecycle. It
 * represents a collection of caches.
 * 
 * Remark, this class does not create caches but delegate this to the
 * CacheBuilder class.
 * 
 * Remark, instances of this class are maintained in a collection by the
 * CacheManagerFactory.
 * 
 * @author 7515005 Ivan Belis
 * 
 */
public class CMCacheManager implements CacheManager {

	private static final Logger LOGGER = Logger.getLogger("javax.cache");

	private final String name;
	private final ClassLoader classLoader;

	// all the caches create by this CacheManager
	// todo: maybe we can change this to ConcurrentHashMap and remove the
	// synchronized blocks ?
	private final HashMap<String, Cache<?, ?>> caches = new HashMap<String, Cache<?, ?>>();

	public CMCacheManager(ClassLoader classLoader, String name) {
		this.name = name;
		this.classLoader = classLoader;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Status getStatus() {
		return javax.cache.Status.STARTED;
	}

	@Override
	public <K, V> CacheBuilder<K, V> createCacheBuilder(String cacheName) {
		/*
		 * Remark1 there is an inconsistency between the jsr107 api and the RI
		 * when the cache with the same name already exists.
		 * 
		 * In the RI an CacheException("Cache " + cacheName + " already exists")
		 * is thrown.
		 * 
		 * In the jsr107 api the javadoc says that the existing cache is stopped
		 * and a new returned.
		 * 
		 * For the user of the jsr107 api this means in both cases that a cache
		 * should be created only once, thus there may only be one call the
		 * build() method. Therefore i choose to follow the RI implementation
		 * and throw an error if a duplicate cache is created.
		 */

		/*
		 * Remark2 there is a potential thread issue with this code. If the
		 * createCacheBuilder is called in thread 1 It is possible to call a
		 * second createCacheBuilder in thread 2. At the same time the 2 threads
		 * can call the .build() operation.
		 */
		return new CoherenceCacheBuilder<K, V>(cacheName, classLoader);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Cache<K, V> getCache(String cacheName) {
		synchronized (caches) {
			return (Cache<K, V>) caches.get(cacheName);
		}
	}

	@Override
	public Iterable<Cache<?, ?>> getCaches() {
		synchronized (caches) {
			HashSet<Cache<?, ?>> set = new HashSet<Cache<?, ?>>();
			for (Cache<?, ?> cache : caches.values()) {
				set.add(cache);
			}
			return Collections.unmodifiableSet(set);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean removeCache(String cacheName) {
		if (cacheName == null) {
			throw new NullPointerException();
		}
		Cache oldCache;
		synchronized (caches) {
			oldCache = caches.remove(cacheName);
		}
		if (oldCache != null) {
			oldCache.stop();
		}

		return oldCache != null;
	}

	@Override
	public boolean isSupported(OptionalFeature optionalFeature) {
		return false;
	}

	@Override
	public void shutdown() {
		ArrayList<Cache<?, ?>> cacheList;
		synchronized (caches) {
			cacheList = new ArrayList<Cache<?, ?>>(caches.values());
			caches.clear();
		}
		for (Cache<?, ?> cache : cacheList) {
			try {
				cache.stop();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error stopping cache: " + cache);
			}
		}
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		if (cls.isAssignableFrom(this.getClass())) {
			return cls.cast(this);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public UserTransaction getUserTransaction() {
		throw new UnsupportedOperationException();
	}

	/**
	 * The builder is the class that creates Cache objects.
	 * 
	 */
	private class CoherenceCacheBuilder<K, V> implements CacheBuilder<K, V> {
		private final String cacheName;
		private final ClassLoader classLoader;

		public CoherenceCacheBuilder(String cacheName, ClassLoader classLoader) {
			this.cacheName = cacheName;
			this.classLoader = classLoader;
		}

		@Override
		public Cache<K, V> build() {
			synchronized (caches) {
				if (caches.get(cacheName) != null) {
					throw new CacheException("Cache " + cacheName + " already exists");
				}
				NamedCache namedDache = CacheFactory.getCache(cacheName, classLoader);

				Cache<K, V> cache = new CMCache<K, V>(namedDache, cacheName);

				// add to internal map of caches
				@SuppressWarnings("rawtypes")
				Cache oldCache;
				synchronized (caches) {
					oldCache = caches.put(cache.getName(), cache);
				}
				cache.start();

				// according to the jsr107 api we should stop the old cache
				// but because we do not allow 2 caches with the same name to be
				// created this will never happen
				if (oldCache != null) {
					oldCache.stop();
				}
				return cache;
			}
		}

		@Override
		public CacheBuilder<K, V> setCacheLoader(CacheLoader<K, ? extends V> cacheLoader) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> setCacheWriter(CacheWriter<? super K, ? super V> cacheWriter) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> registerCacheEntryListener(CacheEntryListener<K, V> cacheEntryListener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> setStoreByValue(boolean storeByValue) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> setTransactionEnabled(IsolationLevel isolationLevel, Mode mode) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> setStatisticsEnabled(boolean enableStatistics) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> setReadThrough(boolean readThrough) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> setWriteThrough(boolean writeThrough) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CacheBuilder<K, V> setExpiry(ExpiryType type, Duration duration) {
			// I did not find a way to dynamically set the expiry of a coherence
			// cache
			// Use the "coherence-cache-config.xml" to set this
			throw new UnsupportedOperationException();
		}

	}

}

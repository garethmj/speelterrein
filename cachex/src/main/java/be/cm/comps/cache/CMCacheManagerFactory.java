/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import javax.cache.CacheManager;
import javax.cache.CacheManagerFactory;
import javax.cache.CachingShutdownException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Manages CacheManager instances. It is invoked by the javax.cache.Caching class to create a CacheManager.
 * 
 * It represents a collection of CacheManagers.
 * Each call to create a CacheManager will return a unique cacheManager for the
 * given name and classloader.
 * 
 * This is a singleton that will hold all CacheManager instances.
 * 
 * To release all created CacheManager you can call the close() operations.
 * 
 * @author 7515005 Ivan Belis 
 */
public final class CMCacheManagerFactory implements CacheManagerFactory {

	private static final CMCacheManagerFactory INSTANCE = new CMCacheManagerFactory();

	//
	// the cacheManagers are - euh - cached for each classloader and cachename
	private final Map<ClassLoader, Map<String, CacheManager>> cacheManagers = new HashMap<ClassLoader, Map<String, CacheManager>>();

	/**
	 * Get the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static CMCacheManagerFactory getInstance() {
		return INSTANCE;
	}

	private CMCacheManagerFactory() {
	}

	/**
	 * Create CacheManager instance
	 * 
	 * @param classLoader
	 *            the class loader
	 * @param name
	 *            the name
	 * @return a CacheManager
	 */
	CacheManager createCacheManager(ClassLoader classLoader, String name) {
		return new CMCacheManager(classLoader, name);
	}

	/**
	 * Get the classloader
	 * 
	 * @return the classloader
	 */
	private ClassLoader getDefaultClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheManager getCacheManager(String name) {
		return getCacheManager(getDefaultClassLoader(), name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CacheManager getCacheManager(ClassLoader classLoader, String name) {
		if (classLoader == null) {
			throw new NullPointerException("classLoader");
		}
		if (name == null) {
			throw new NullPointerException("name");
		}
		synchronized (cacheManagers) {
			Map<String, CacheManager> map = cacheManagers.get(classLoader);
			if (map == null) {
				map = new HashMap<String, CacheManager>();
				cacheManagers.put(classLoader, map);
			}
			CacheManager cacheManager = map.get(name);
			if (cacheManager == null) {
				cacheManager = createCacheManager(classLoader, name);
				map.put(name, cacheManager);
			}
			return cacheManager;
		}
	}

	/**
	 * Create CacheManager instance
	 * 
	 * @param classLoader
	 *            the class loader
	 * @param name
	 *            the name
	 * @return a CacheManager
	 */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws CachingShutdownException {
		IdentityHashMap<CacheManager, Exception> failures = new IdentityHashMap<CacheManager, Exception>();
		synchronized (cacheManagers) {
			for (Map<String, CacheManager> cacheManagerMap : cacheManagers
					.values()) {
				try {
					shutdown(cacheManagerMap);
				} catch (CachingShutdownException e) {
					failures.putAll(e.getFailures());
				}
			}
			cacheManagers.clear();
			if (!failures.isEmpty()) {
				throw new CachingShutdownException(failures);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close(ClassLoader classLoader)
			throws CachingShutdownException {
		Map<String, CacheManager> cacheManagerMap;
		synchronized (cacheManagers) {
			cacheManagerMap = cacheManagers.remove(classLoader);
		}
		if (cacheManagerMap == null) {
			return false;
		} else {
			shutdown(cacheManagerMap);
			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close(ClassLoader classLoader, String name)
			throws CachingShutdownException {
		CacheManager cacheManager = null;
		synchronized (cacheManagers) {
			Map<String, CacheManager> cacheManagerMap = cacheManagers
					.get(classLoader);
			if (cacheManagerMap != null) {
				cacheManager = cacheManagerMap.remove(name);
				if (cacheManagerMap.isEmpty()) {
					cacheManagers.remove(classLoader);
				}
			}
		}
		if (cacheManager == null) {
			return false;
		} else {
			cacheManager.shutdown();
			return true;
		}
	}

	private void shutdown(Map<String, CacheManager> cacheManagerMap)
			throws CachingShutdownException {
		IdentityHashMap<CacheManager, Exception> failures = new IdentityHashMap<CacheManager, Exception>();
		for (CacheManager cacheManager : cacheManagerMap.values()) {
			try {
				cacheManager.shutdown();
			} catch (Exception e) {
				failures.put(cacheManager, e);
			}
		}
		if (!failures.isEmpty()) {
			throw new CachingShutdownException(failures);
		}
	}
}

/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheManager;
import javax.cache.CacheManagerFactory;
import javax.cache.OptionalFeature;
import javax.cache.spi.CachingProvider;

import org.junit.After;
import org.junit.Test;

public class CMCacheManagerTest {

	private static final String TEST_CACHE = "local-test-cache";

	/**
	 * Cleanup caches created by earlier tests
	 */
	@After
	public void cleanUp() {
		CachingProvider cacheProvider = new CMCacheProvider();
		CacheManagerFactory factory = cacheProvider.getCacheManagerFactory();
		factory.close();
	}
	
	@Test
	public void testGetName() {
		CacheManager cacheMgr = getCacheManager();
		assertEquals(TEST_CACHE, cacheMgr.getName());
	}

	@Test
	public void testGetStatus() {
		CacheManager cacheMgr = getCacheManager();
		assertEquals(javax.cache.Status.STARTED, cacheMgr.getStatus());
	}

	@Test
	public void testCreateCacheBuilder() {
		CacheManager cacheMgr = getCacheManager();
		CacheBuilder<Integer, Date> builder = cacheMgr.<Integer, Date> createCacheBuilder("myCache2");
		assertNotNull(builder);

	}

	@Test
	public void testGetCache() {
	    // non existing cache
		Cache<Object, Object> cache1 = getCacheManager().getCache("non-existing-cache");
		assertNull(cache1);
		
		// create a cache
		createTestCache();
		Cache<Object, Object> cache2 = getCacheManager().getCache(TEST_CACHE);
		assertNotNull(cache2);
	}

	@Test
	public void testGetCaches() {
		// non existing cache
		Iterable<Cache<?, ?>> caches1 = getCacheManager().getCaches();
		assertNotNull(caches1);
		assertFalse(caches1.iterator().hasNext());
		
		// create a cache
		createTestCache();
		
		Iterable<Cache<?, ?>> caches2 = getCacheManager().getCaches();
		assertNotNull(caches2);
		assertTrue(caches2.iterator().hasNext());
	}

	@Test
	public void testRemoveCache() {
		// non existing cache
		getCacheManager().removeCache("abc");
		
		// existing cache
		createTestCache();		
		getCacheManager().removeCache(TEST_CACHE);
	}

	@Test
	public void testIsSupported() {
		assertFalse(getCacheManager().isSupported(OptionalFeature.TRANSACTIONS));
	}
	
	@Test
	public void testUnwrap() {
		CacheManager cacheManager = getCacheManager();
		CMCacheManager unwrapped = cacheManager.unwrap(CMCacheManager.class);
		assertNotNull(unwrapped);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testGetUserTransaction() {
		getCacheManager().getUserTransaction();
	}

	// Utility method that creates a CacheManaqer
	static CacheManager getCacheManager() {
		CachingProvider cacheProvider = new CMCacheProvider();
		CacheManagerFactory factory = cacheProvider.getCacheManagerFactory();
		return factory.getCacheManager(TEST_CACHE);
	}
	
	// Utility method that creates a cache using the CacheManager
	private Cache<Integer, Date> createTestCache() {
		CacheManager cacheMgr = getCacheManager();
		CacheBuilder<Integer, Date> builder = cacheMgr.<Integer, Date> createCacheBuilder(TEST_CACHE);
		return builder.build();
	}
}

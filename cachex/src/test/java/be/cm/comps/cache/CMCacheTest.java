/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.cache.Cache;
import javax.cache.Cache.Entry;
import javax.cache.CacheBuilder;
import javax.cache.CacheManager;
import javax.cache.CacheManagerFactory;
import javax.cache.Status;
import javax.cache.spi.CachingProvider;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import com.oracle.common.collections.ArraySet;
import com.tangosol.net.NamedCache;

public class CMCacheTest {

	private static final String TEST_CACHE_MANAGER = "test-cachmgr";
	private static final String TEST_CACHE = "local-test-cache";
	private static final String TEST_CACHE_STRING = "local-test-cache-string";
	
	private static final CacheManagerFactory factory = new CMCacheProvider().getCacheManagerFactory();


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
	public void testStart() {
		Cache<Integer, Date> cache = createTestCache();
		cache.start();
	}

	@Test
	public void testStop() {
		Cache<Integer, Date> cache = createTestCache();
		cache.stop();
	}

	@Test
	public void testGetStatus() {
		Cache<Integer, Date> cache = createTestCache();
		Assert.assertEquals(Status.STARTED, cache.getStatus());

	}

	@Test
	public void testGet() {
		Cache<Integer, Date> cache = createTestCache();

		// non existing get
		Assert.assertNull(cache.get(1));
	}

	@Test
	public void testGetAll() {
		Cache<Integer, Date> cache = createTestCache();
		cache.put(1, new Date());
		cache.put(2, new Date());
		cache.put(3, new Date());

		Set<Integer> keys = new ArraySet<Integer>();
		keys.add(1);
		keys.add(3);
		Map<Integer, Date> values = cache.getAll(keys);
		assertEquals(2, values.size());
	}

	@Test
	public void testContainsKey() {
		Cache<Integer, Date> cache = createTestCache();
		cache.put(1, new Date());

		assertTrue(cache.containsKey(1));
		assertFalse(cache.containsKey(2));

	}

	@Test
	public void testGetAndPut() {
		Cache<Integer, Date> cache = createTestCache();
		Date oldDate = cache.getAndPut(1, new Date());
		assertNull(oldDate);

		oldDate = cache.getAndPut(1, new Date());
		assertNotNull(oldDate);
	}

	@Test
	public void testPutAll() {
		Map<Integer, Date> map = new HashMap<Integer, Date>();
		map.put(1, new Date());
		map.put(2, new Date());
		map.put(3, new Date());
		Cache<Integer, Date> cache = createTestCache();
		cache.putAll(map);

		Map<Integer, Date> values = cache.getAll(map.keySet());
		assertEquals(3, values.size());
	}

	@Test
	public void testPutIfAbsent() {
		Cache<Integer, Date> cache = createTestCache();

		assertTrue(cache.putIfAbsent(1, new Date()));
		assertFalse(cache.putIfAbsent(1, new Date()));
	}

	@Test
	public void testRemove() {
		Cache<Integer, Date> cache = createTestCache();
		cache.put(1, new Date());

		assertTrue(cache.remove(1));
		assertFalse(cache.remove(1));
	}
	
	@Test
	public void testRemoveIfExists() {
		Cache<Integer, String> cache = createTestCache2();
		cache.put(1, "abc");
		cache.put(2, "def");

		assertTrue(cache.remove(1,"abc"));
		assertFalse(cache.remove(2,"xxx"));
	}
	
	@Test
	public void testRemoveAllSet() {
		Cache<Integer, String> cache = createTestCache2();
		cache.put(1, "abc");
		cache.put(2, "def");
		cache.put(3, "def");
		
		Set<Integer> keys = new ArraySet<Integer>();
		keys.add(1);
		keys.add(3);
		cache.removeAll(keys );
		assertFalse(cache.containsKey(1));
		assertTrue(cache.containsKey(2));
		assertFalse(cache.containsKey(3));
	}
	
	@Test
	public void testIterator() {
		Cache<Integer, String> cache = createTestCache2();
		cache.put(1, "abc");
		cache.put(2, "def");
		cache.put(3, "xyz");
		
		String allEntries = "";
		for (Entry<Integer, String> entry : cache) {
			allEntries = allEntries + ';' + entry.getKey().toString() + ':' + entry.getValue();
		}
		assertEquals(";1:abc;2:def;3:xyz",allEntries);
		
		Iterator<Entry<Integer, String>> iterator = cache.iterator();
		iterator.next();
		iterator.remove();
		assertFalse(cache.containsKey(1));
		assertTrue(cache.containsKey(2));
	}
	
	@Test
	public void testGetAndReplace() {
		Cache<Integer, String> cache = createTestCache2();
		cache.put(1, "abc");
		cache.put(2, "def");

		assertEquals("abc",cache.getAndReplace(1,"def"));
		assertTrue(cache.getAndReplace(3,"xxx")==null);
	}

	@Test
	public void testGetAndRemove() {
		Cache<Integer, Date> cache = createTestCache();
		cache.put(1, new Date());

		assertNotNull(cache.getAndRemove(1));
		assertNull(cache.getAndRemove(1));
	}

	@Test
	public void testReplaceKVV() {
		Cache<Integer, Date> cache = createTestCache();
		cache.put(1, new Date());

		assertTrue(cache.replace(1, new Date()));
		assertFalse(cache.replace(2, new Date()));

		Date xmas = new GregorianCalendar(2012, Calendar.DECEMBER, 25).getTime();

		assertFalse(cache.replace(1, xmas, new Date()));
		cache.replace(1, xmas);
		assertTrue(cache.replace(1, xmas, new Date()));
	}

	@Test
	public void testRemoveAll() {
		Cache<Integer, Date> cache = createTestCache();
		cache.put(1, new Date());
		cache.removeAll();

		assertFalse(cache.containsKey(1));
	}

	@Test
	public void testGetName() {
		Cache<Integer, Date> cache = createTestCache();
		assertEquals(TEST_CACHE, cache.getName());
	}

	@Test
	public void testUnwrap() {
		Cache<Integer, Date> cache = createTestCache();
		NamedCache namedCache = cache.unwrap(NamedCache.class);
		assertNotNull(namedCache);
		assertEquals(TEST_CACHE, namedCache.getCacheName());
	}
	
	
	/**
	 * Test the unwrapping of the coherence exception.
	 */
	@Test(expected=IllegalStateException.class)
	public void testThunkException() {
		Cache<Integer, Date> cache = createTestCache();
		NamedCache namedCache = cache.unwrap(NamedCache.class);
		
		namedCache.lock(NamedCache.LOCK_ALL);
		namedCache.getCacheService().destroyCache(namedCache);
		
		cache.put(1, new Date());

		
		namedCache.destroy();
		
	}

	// Utility method that creates a local cache
	private Cache<Integer, Date> createTestCache() {
		CacheManager cacheMgr = factory.getCacheManager(TEST_CACHE_MANAGER);
		CacheBuilder<Integer, Date> builder = cacheMgr.<Integer, Date> createCacheBuilder(TEST_CACHE);
		return builder.build();
	}
	
	private Cache<Integer, String> createTestCache2() {
		CacheManager cacheMgr = factory.getCacheManager(TEST_CACHE_MANAGER);
		CacheBuilder<Integer, String> builder = cacheMgr.<Integer, String> createCacheBuilder(TEST_CACHE_STRING);
		return builder.build();
	}
}

/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.BeforeClass;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import be.cm.comps.cache.test.util.Concurrent;
import be.cm.comps.cache.test.util.ConcurrentJunitRunner;

/**
 * Test for lock behavior in a multithread environment.
 * 
 * Usage: use a debugger to stop one thread inside the replace() method and watch the behavior of the other
 * thread.
 * 
 * @author 7515005 Ivan Belis
 * 
 */
@RunWith(ConcurrentJunitRunner.class)
@Concurrent(threads = 2)
public class CacheLockTestIT {
	
	private static CacheManager cacheManager = Caching.getCacheManager();
	private static Cache<String, String> cache1 = null;
	
	@BeforeClass
	public static void setUp() {
		// create the cache that will be shared by the two threads
		cache1 = cacheManager.<String, String> createCacheBuilder("local-cache1").build();
		cache1.put("a", "a1");
		cache1.put("b", "b1");
	}

	@Theory
	public void useLock1() {
		// Replace with a value is an atomic operation that uses locking
		// To test the lock you must set a breakpoint in the replace implementation
		cache1.replace("b", "b2");
	}

	@Theory
	public void useLock2() {
		// replace with a value is an atomic operation that uses locking
		// set a breakpoint in the replace implementation
		cache1.replace("b", "b2");
	}
}

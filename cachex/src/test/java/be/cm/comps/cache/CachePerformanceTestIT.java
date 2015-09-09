/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import be.cm.comps.cache.test.util.Concurrent;
import be.cm.comps.cache.test.util.ConcurrentJunitRunner;
import be.cm.comps.cache.test.util.NanoMeter;

/**
 * Test for performance and multithread safety.
 * 
 * @author 7515005 Ivan Belis
 * 
 */
@RunWith(ConcurrentJunitRunner.class)
@Concurrent(threads = 4)
public class CachePerformanceTestIT {
	
	private static NanoMeter logger = new NanoMeter();

	/*
	 * To be able to test the thread-safety i have created a number of identical
	 * test cases.
	 */
	@Theory
	public void simpleCacheTest1() {
		createAndUseCache();
	}

	@Theory
	public void simpleCacheTest2() {
		createAndUseCache();
	}

	@Theory
	public void simpleCacheTest3() {
		createAndUseCache();
	}

	@Theory
	public void simpleCacheTest4() {
		createAndUseCache();
	}

	private void createAndUseCache() {
		logger.log("starting " + Thread.currentThread());
		CacheManager cacheManager = Caching.getCacheManager();
		assertNotNull(cacheManager);

		Cache<String, String> cache1 = null;
		synchronized (cacheManager) {
			cache1 = cacheManager.getCache("local-cache1");
			if (cache1 == null) {
				cache1 = cacheManager.<String, String> createCacheBuilder("local-cache1").build();
			}
		}
		assertNotNull(cache1);

		// create two caches
		Cache<Integer, Long> cache2 = null;
		synchronized (cacheManager) {
			cache2 = cacheManager.getCache("local-cache2");
			if (cache2 == null) {
				cache2 = cacheManager.<Integer, Long> createCacheBuilder("local-cache2").build();
			}
		}
		assertNotNull(cache2);

		// use the cache
		for (int i = 0; i < 1000; i++) {
			cache1.put("a", "a1");
			cache1.put("b", "b1");
			cache1.put("c", "c1");
			cache2.putIfAbsent(1, 1L);
			cache2.put(2, 2L);
			cache1.remove("c");
			cache1.remove("b");
			cache2.remove(2);			
		}

		// verify the first values in the cache
		assertEquals("a1", cache1.get("a"));
		assertEquals(Long.valueOf(1L), cache2.get(1));

		logger.log("done " + Thread.currentThread());
	}

	@SuppressWarnings("unchecked")
	private void runMultiThreads(int numThreads) {
		ExecutorService group = new ThreadPoolExecutor(numThreads, numThreads, 60, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), new ThreadFactory() {
					public Thread newThread(Runnable r) {
						return new Thread(r);
					}

				});
		Future<Object>[] futures = new Future[numThreads];

		for (int i = 0; i < numThreads; i++) {
			futures[i] = group.submit(new Callable<Object>() {
				public Object call() {
					createAndUseCache();
					return null;
				}
			});
		}
		waitForTermination(futures);
	}

	private void waitForTermination(Future<Object>[] futures) {
		try {
			for (Future<?> f : futures)
				try {
					f.get(300, TimeUnit.SECONDS);
				} catch (TimeoutException te) {
					fail("Failed " + te);
				} catch (ExecutionException e) {
					if (e.getCause() instanceof CacheException) {
						throw (CacheException) e.getCause();
					}
					fail("Failed " + e.getCause());
				}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Run this as an application for a performance test.
	 * @param args
	 */
	public static void main(String[] args) {
		logger.log("starting");
		
		CacheManager cacheManager = Caching.getCacheManager();
		logger.log("created CacheManager");
		
		CacheBuilder<String, String> builder = cacheManager.createCacheBuilder("local-performance-test-cache");
		logger.log("created CacheBuilder");
		
		Cache<String, String> cache = builder.build();
		logger.log("created Cache");
		
		cache.put("1", "val1");
		logger.log("put first value");
		
		cache.put("2", "val2");
		logger.log("put second value");
		
		logger.log("containsKey " + cache.containsKey("1"));
		logger.log("containsKey " + cache.containsKey("NOT"));
		

		for (int i = 0; i < 1000; i++) {
			cache.put("key"+i, "val"+i);
		}
		logger.log("put 1000 values");
		
		for (int i = 0; i < 1000; i++) {
			cache.get("key"+i);
		}
		logger.log("get 1000 existing values");
		
		for (int i = 0; i < 1000; i++) {
			cache.get("x"+i);
		}
		logger.log("get 1000 non existing values");
		
		for (int i = 0; i < 1000; i++) {
			cache.containsKey("key"+i);
		}
		logger.log("containsKey 1000 values true");

		for (int i = 0; i < 1000; i++) {
			cache.containsKey("x"+i);
		}
		logger.log("containsKey 1000 values false");

		logger.log("starting multithread test");
		new CachePerformanceTestIT().runMultiThreads(100);
		logger.log("done");
	}
}

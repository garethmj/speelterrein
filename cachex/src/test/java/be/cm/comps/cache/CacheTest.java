/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import static junit.framework.Assert.assertNotNull;

import java.util.Date;

import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheManager;
import javax.cache.Caching;

import junit.framework.Assert;

import org.junit.Test;

public class CacheTest {

	@Test
	public void simpleCacheTest() {
		CacheManager cacheManager = Caching.getCacheManager();
		assertNotNull(cacheManager);

		CacheBuilder<Object, Object> cacheBuilder = cacheManager
				.<Object, Object> createCacheBuilder("local-example-cache1");
		assertNotNull(cacheBuilder);
		
		Cache<Object, Object> cache1 = cacheBuilder.build();
		assertNotNull(cache1);
		cache1.put(0, new Date());

		CacheBuilder<Integer, Date> cacheBuilder2 = cacheManager
				.<Integer, Date> createCacheBuilder("local-example-cache2");

		Cache<Integer, Date> cache2 = cacheBuilder2.build();
		assertNotNull(cache2);

		Date date1 = new Date();
		cache2.put(0, date1);

		Date date2 = cache2.get(0);
		Assert.assertEquals(date1, date2);

		Assert.assertTrue(cache2.containsKey(0));

		boolean isAbsent = cache2.putIfAbsent(0, new Date());
		Assert.assertFalse(isAbsent);

		isAbsent = cache2.putIfAbsent(1, new Date());
		Assert.assertTrue(isAbsent);

		System.out.println("name:" + cache2.getName());
		System.out.println("status:" + cache2.getStatus());

	}
}

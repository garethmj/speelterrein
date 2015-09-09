/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import static org.junit.Assert.assertNotNull;

import javax.cache.CacheManager;
import javax.cache.CacheManagerFactory;
import javax.cache.spi.CachingProvider;

import org.junit.Test;

public class CMCacheManagerFactoryTest {

	@Test
	public void testClose() {
		CacheManagerFactory factory = getCachingFactory();
		assertNotNull(factory);

		factory.close();
		factory.close(this.getClass().getClassLoader());
		factory.close(this.getClass().getClassLoader(), "");
	}

	@Test
	public void testNamedManager() {
		CacheManagerFactory factory = getCachingFactory();
		CacheManager cacheManager = factory.getCacheManager("cache1");
		assertNotNull(cacheManager);
	}

	// Utilities --------------------------------------------------

	static CacheManagerFactory getCachingFactory() {
		CachingProvider cacheProvider = new CMCacheProvider();
		return cacheProvider.getCacheManagerFactory();
	}
}

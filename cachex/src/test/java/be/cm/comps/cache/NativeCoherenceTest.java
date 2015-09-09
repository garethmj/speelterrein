/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheService;
import com.tangosol.net.DefaultConfigurableCacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.LocalCache;

/**
 * Testing with the native coherence api.
 * 
 * @author 7515005 Ivan Belis
 * 
 */
public class NativeCoherenceTest {

	@Test
	public void simpleCacheTest() {
		String key = "k1";
		String value = "Hello World!";

		NamedCache cache = CacheFactory.getCache("local-example-cache");

		Object x = cache.put(key, value);
		System.out.println("x:" + x);
		System.out.println((String) cache.get(key));

		cache.put("k2", new Date());
	}

	@Test
	public void dynamicallySetCachePropertiesTest() throws Exception {
		String key = "key";
		String value = "Hello";

		String cacheName = "local-example-cache";
		NamedCache cache = CacheFactory.getCache(cacheName);

		// grab the backing map
		CacheService service = cache.getCacheService();
		LocalCache backingMap = (LocalCache) ((DefaultConfigurableCacheFactory.Manager) service.getBackingMapManager())
				.getBackingMap(cacheName);

		// change the size and time-to-live of the cache
		backingMap.setExpiryDelay(1000); // millisec
		backingMap.setHighUnits(10);
		
		/*
		 * We can safely ignore the deprecated warnings, the public api will remain avaible
		 * in the LocalCache, see also:
		 * https://forums.oracle.com/forums/thread.jspa?messageID=2148979&#2148979
		 */
		
		assertEquals(1000,backingMap.getExpiryDelay());
		assertEquals(10,backingMap.getHighUnits());

		// verify the max size of the cache
		for (int i = 0; i < 20; i++) {
			cache.put(key + i, value + i);
		}
		int cacheSize = cache.entrySet().size();
		assertTrue("max highunits test",cacheSize <= 10 );
		
		// verify the expiry of the cache
		Thread.sleep(1000);
		cache.put(key + 21, value + 21);      
		int newCacheSize = cache.entrySet().size();
		assertEquals("expiry test", 1,newCacheSize);
	}

	@Test
	public void createCacheTest() {
		NamedCache cache1 = CacheFactory.getCache("local-noclassloader-cache", null);
		assertNotNull(cache1);
		NamedCache cache2 = CacheFactory.getCache("local-withclassloader-cache", this.getClass().getClassLoader());
		assertNotNull(cache2);
	}

	@Test
	public void putNullValuesTest() {
		NamedCache cache1 = CacheFactory.getCache("local-noclassloader-cache", null);
		cache1.put("a", "b");
		cache1.put("a", null);
		cache1.put(null, null);
	}

}
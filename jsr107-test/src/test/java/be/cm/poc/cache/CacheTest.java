package be.cm.poc.cache;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheBuilder;
import javax.cache.CacheConfiguration.Duration;
import javax.cache.CacheConfiguration.ExpiryType;
import javax.cache.CacheManager;
import javax.cache.Caching;

import org.junit.Test;

import junit.framework.Assert;

public class CacheTest {

	@Test
	public void simpleCacheTest() {
		CacheManager cacheManager = Caching.getCacheManager();
		Assert.assertNotNull(cacheManager);

		CacheBuilder<Object, Object> cacheBuilder = cacheManager
				.<Object, Object> createCacheBuilder("example-local-cache");
		cacheBuilder.build().put(0, new Date());

		CacheBuilder<Integer, Date> cacheBuilder2 = cacheManager
				.<Integer, Date> createCacheBuilder("example-local-cache");

		// entries will expiry after 30 seconds
		cacheBuilder2.setExpiry(ExpiryType.MODIFIED, new Duration(
				TimeUnit.SECONDS, 30));
		cacheBuilder2.setStatisticsEnabled(true);

		Cache<Integer, Date> aCache = cacheBuilder2.build();
		Assert.assertNotNull(aCache);

		Date date1 = new Date();
		aCache.put(0, date1);

		Date date2 = aCache.get(0);
		Assert.assertEquals(date1, date2);

		Assert.assertTrue(aCache.containsKey(0));

		boolean isAbsent = aCache.putIfAbsent(0, new Date());
		Assert.assertFalse(isAbsent);

		isAbsent = aCache.putIfAbsent(1, new Date());
		Assert.assertTrue(isAbsent);

		System.out.println("stats hits:"
				+ aCache.getStatistics().getCacheHits());
		System.out.println("name:" + aCache.getName());
		System.out.println("status:" + aCache.getStatus());

	}
}

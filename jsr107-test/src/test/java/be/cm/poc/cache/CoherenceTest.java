package be.cm.poc.cache;

import java.util.Date;

import org.junit.Test;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class CoherenceTest {

	@Test
	public void simpleCacheTest() {

		String key = "k1";
		String value = "Hello World!";

		NamedCache cache = CacheFactory.getCache("example-local-cache");

		Object x = cache.put(key, value);
		System.out.println("x:" + x);
		System.out.println((String) cache.get(key));

		cache.put("k2", new Date());
	}
}
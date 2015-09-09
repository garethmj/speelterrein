/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import org.junit.Test;

import javax.cache.CacheManagerFactory;
import javax.cache.OptionalFeature;
import javax.cache.spi.CachingProvider;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class CMCacheProviderTest {

    @Test
    public void testReflectionConstructor() throws Exception {
        Class<CMCacheProvider> clazz = CMCacheProvider.class;
        CachingProvider provider = clazz.newInstance();
        assertNotNull(provider);
    }

    @Test
    public void testIsSupported() {
        CachingProvider cacheProvider = getCachingProvider();
        for (OptionalFeature feature: OptionalFeature.values()) {
            assertFalse(cacheProvider.isSupported(feature));
        }
    }
    
    @Test
    public void testCreateFactory() {
        CachingProvider cacheProvider = getCachingProvider();
        CacheManagerFactory factory = cacheProvider.getCacheManagerFactory();
        assertNotNull(factory);
    }

    // Utilities --------------------------------------------------

    static CMCacheProvider getCachingProvider() {
        return new CMCacheProvider();
    }
}

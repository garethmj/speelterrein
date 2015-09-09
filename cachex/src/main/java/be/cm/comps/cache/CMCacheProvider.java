/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache;

import javax.cache.CacheManagerFactory;
import javax.cache.OptionalFeature;
import javax.cache.spi.CachingProvider;

/**
 * 
 * This is the bootstrap class that will be discovered by the javax.cache api.
 * <p/>
 * The only function of this class is to return the correct CacheManagerFactory.
 * <p/>
 * The property file META-INF/services/javax.cache.spi.CachingProvider should
 * contain the name of this class.
 * 
 * @author 7515005 Ivan Belis 
 *
 */
public class CMCacheProvider implements CachingProvider {

	@Override
	public CacheManagerFactory getCacheManagerFactory() {
		return CMCacheManagerFactory.getInstance();
	}

	@Override
	public boolean isSupported(OptionalFeature optionalFeature) {
		return false;
	}
}

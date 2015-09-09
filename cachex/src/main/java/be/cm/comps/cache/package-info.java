/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */

/**
 * <p/>
 * This package contain an implementation of the javax.cache that adapts a local Coherence cache as implementation. 
 * <p/>
 * @see http://jcp.org/en/jsr/detail?id=107
 * @see javax.cache
 * 
 * <p/> 
 * This javax.cache (jsr107) adapter implements not all of the functions of the jsr.
 * <p/>
 * It does not:
 *    . provide a JMXBean
 *    . provide statistics
 *    . provide JTA support
 *    . provide entry listeners and entry processors
 * <p/>
 * Methods not implemented of javax.cache.Cache:
 *    . Cache.load(..)
 *    . Cache.loadAll()
 *    . Cache.getStatistics()
 *    . Cache.getConfiguration()
 *    . Cache.registerCacheEntryListener()
 *    . Cache.unregisterCacheEntryListener()
 *    . Cache.invokeEntryProcessor()
 *    . Cache.getCacheManager()
 *    . Cache.getMBean()
 *    
 * @author 7515005 Ivan Belis
 * 
 */
package be.cm.comps.cache;


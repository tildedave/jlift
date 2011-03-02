/*
 * File          : CacheCallBack.java
 * Project       : jlift
 * Description   : When the DFSGraph updates its cache, it
 * 				   calls the method in CacheCallBack, on which
 *  			   an action is performed.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:45:20 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.util;

import java.util.Collection;

public abstract class CacheCallBack<T> {

	// once we register that a varlabel affects a certain set, then
	// we record this to the ExpressionFlowMap
	public abstract void registerCacheUpdate(Collection<T> nscc, Collection<T> cacheNode);
}

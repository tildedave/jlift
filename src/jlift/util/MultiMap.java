/*
 * File          : MultiMap.java
 * Project       : jlift
 * Description   : A map from U to a list of Vs.  Often used.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:56:28 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MultiMap<U, V> {

	Map<U, Collection<V>> theMap;

	public MultiMap() {
		theMap = new HashMap<U, Collection<V>>();
	}

	public void put(U u) {
		Collection<V> currentList = theMap.get(u);
		if (currentList == null) {
			currentList = new LinkedHashSet<V>();
		}
		theMap.put(u, currentList);
	}


	public void put(U u, V v) {
		Collection<V> currentList = theMap.get(u);
		if (currentList == null) {
			currentList = new LinkedHashSet<V>();
		}
		currentList.add(v);
		theMap.put(u, currentList);
	}

	public Collection<V> get(Object o) {
		return theMap.get(o);
	}

	public Set<U> keySet() {
		return theMap.keySet();
	}

	public void clear() {
		theMap.clear();
	}

	public boolean containsKey(Object o) {
		return theMap.containsKey(o);
	}

	public boolean containsValue(Object value) {
		return theMap.containsValue(value);
	}

	public Set<Entry<U, Collection<V>>> entrySet() {
		return theMap.entrySet();
	}

	@Override
	public String toString() {
		return theMap.toString();
	}

	public int size() {
		return theMap.size();
	}
}

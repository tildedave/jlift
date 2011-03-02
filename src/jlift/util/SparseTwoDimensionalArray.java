package jlift.util;

import java.util.HashMap;
import java.util.Map;

public class SparseTwoDimensionalArray {

	protected Map<Integer, Map<Integer, Integer>> valueMap;
	
	public SparseTwoDimensionalArray() {
		valueMap = new HashMap<Integer, Map<Integer,Integer>>();
	}
	
	public boolean hasEntry(int i, int j) {
		Map<Integer, Integer> iMap = valueMap.get(i);
		if (iMap == null)
			return false;
		
		return iMap.containsKey(j);
	}
	
	public Integer get(int i, int j) {
		Map<Integer, Integer> iMap = get(i);
		Integer returnValue = iMap.get(j);
		if (returnValue == null)
			return 0;
		
		return returnValue;
	}

	public Map<Integer, Integer> get(int i) {
		Map<Integer, Integer> iMap = valueMap.get(i);
		if (iMap == null) {
			iMap = new HashMap<Integer, Integer>();
			valueMap.put(i, iMap);
		}
		return iMap;
	}
	
	public void put(int i, int j, int value) {
		Map<Integer, Integer> iMap = get(i);
		if (iMap == null) {
			iMap = new HashMap<Integer, Integer>();
			valueMap.put(i, iMap);
		}
		
		iMap.put(j, value);
	}
}

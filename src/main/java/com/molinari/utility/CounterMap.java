package com.molinari.utility;

import java.util.HashMap;
import java.util.Iterator;

public class CounterMap<K> extends HashMap<K, Integer>{

	private static final long serialVersionUID = 1L;

	public void put(K title) {
		if(!containsKey(title)) {
			put(title, 1);
		}else {
			Integer integer = get(title);
			put(title, ++integer);
		}
	}

	public K getKeyWithMaxCounter() {
		Entry<K, Integer> maxEntry = null;
		if(!isEmpty()) {
			for (Iterator<Entry<K, Integer>> iterator = entrySet().iterator(); iterator.hasNext();) {
				Entry<K, Integer> entry = iterator.next();
				Integer value = entry.getValue();
				if(maxEntry == null || value > maxEntry.getValue()) {
					maxEntry = entry;
				}
				
			}
		}
		if(maxEntry != null) {
			maxEntry.getKey();
		}
		return null;
	}

	
	
	
}

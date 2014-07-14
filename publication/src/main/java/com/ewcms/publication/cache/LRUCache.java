package com.ewcms.publication.cache;

import org.apache.commons.collections4.map.LRUMap;

public class LRUCache<K, T> implements Cacheable<K, T>{
	private static final int DEFAULT_SIZE = 30;
	private final Object montior = new Object();
	
	private final LRUMap<K,T> pool ;

	public LRUCache(){
		this(DEFAULT_SIZE);
	}
	
	public LRUCache(int max){
		pool = new LRUMap<K,T>(max);
	}

	@Override
	public T get(K key) {
		synchronized (montior) {
			return pool.get(key);	
		}
	}

	@Override
	public T add(K key,T content) {
		synchronized (montior) {
			pool.put(key, content);
			return content;
		}
	}

	@Override
	public int size() {
		synchronized (montior) {
			return pool.size();
		}
	}

	@Override
	public boolean exist(K key) {
		synchronized (montior) {
			return pool.containsKey(key);
		}
	}
}

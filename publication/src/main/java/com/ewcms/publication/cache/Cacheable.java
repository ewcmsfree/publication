package com.ewcms.publication.cache;


public interface Cacheable<K, T> {
	
	T get(K key);
	
	T add(K key,T content);
	
	int size();
	
	boolean exist(K key);
}

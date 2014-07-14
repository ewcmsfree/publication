package com.ewcms.publication.cache;

public class NoneCache<K, T> implements Cacheable<K, T> {
	
	@Override
	public T get(K key) {
		return null;
	}

	@Override
	public T add(K key, T content) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean exist(K key) {
		return false;
	}
}

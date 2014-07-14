package com.ewcms.publication.deploy;

public interface DepTaskQueueable {
	
	void register(DepTask task);
	
	DepTask task();
}

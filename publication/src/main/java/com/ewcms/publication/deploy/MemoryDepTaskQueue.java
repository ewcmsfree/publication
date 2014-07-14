package com.ewcms.publication.deploy;

import java.util.concurrent.LinkedBlockingQueue;

public class MemoryDepTaskQueue implements DepTaskQueueable{
	 private static final int DEFAULT_MAX_TASK = 50;
	 
	 private final LinkedBlockingQueue<DepTask> tasks ;
	 
	 public MemoryDepTaskQueue(){
		 this(DEFAULT_MAX_TASK);
	 }
	 
	 public MemoryDepTaskQueue(int max){
		 tasks = new LinkedBlockingQueue<DepTask>(max);
	 }

	@Override
	public void register(DepTask task) {
		try {
			tasks.put(task);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public DepTask task() {
		DepTask task = null;
		
		try {
			task = tasks.take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		return task;
	}
}

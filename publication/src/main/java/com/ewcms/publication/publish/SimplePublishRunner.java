/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.dao.PublishDaoable;
import com.ewcms.publication.deploy.DepTaskQueueable;
import com.ewcms.publication.deploy.DepTaskRunner;
import com.ewcms.publication.deploy.MemoryDepTaskQueue;
import com.ewcms.publication.task.MemoryTaskQueue;
import com.ewcms.publication.task.TaskQueueable;
import com.ewcms.publication.task.TaskRunner;
import com.ewcms.publication.task.Taskable;

/**
 * 实现单线程站点任务发布
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class SimplePublishRunner implements PublishRunnerable {
    private static final Logger logger = LoggerFactory.getLogger(SimplePublishRunner.class);
    
    private final PublishDaoable publishDao;
    private final Object montior = new Object();
    
    private DepTaskQueueable depTaskQueue ;
    private TaskQueueable genTaskQueue ;
    private volatile boolean running = false;
    
    private TaskRunner taskRunner;
    private DepTaskRunner depTaskRunner;
    
    public SimplePublishRunner(PublishDaoable publishDao){
    	
    	this.publishDao = publishDao;
    	this.depTaskQueue = new MemoryDepTaskQueue(1024);
    	this.genTaskQueue = new MemoryTaskQueue(publishDao);
    }

	@Override
	public void start() {
		synchronized (montior) {
			logger.info("Start single publish runner...");
			if(running) return ;
			running = true;
			taskRunner = new TaskRunner(genTaskQueue,depTaskQueue,publishDao);
			depTaskRunner = new DepTaskRunner(depTaskQueue,publishDao);
			new Thread(taskRunner).start();
			new Thread(depTaskRunner).start();
		}
	}

	@Override
	public void shutdown() {
		synchronized (montior) {
			logger.info("Close single publish runner...");
			if(!running) return ;
			running = false;
			taskRunner.shutdown();
			depTaskRunner.shutdown();
		}
	}

	@Override
	public void regTask(Taskable task) {
		synchronized (montior) {
			genTaskQueue.register(task);
		}
	}

	@Override
	public boolean isRunning(){
		return this.running;
	}

	@Override
	public void setGenTaskQueue(TaskQueueable taskQueue) {
		this.genTaskQueue = taskQueue;
	}

	@Override
	public void setDepTaskQueue(DepTaskQueueable taskQueue) {
		this.depTaskQueue = taskQueue;
	}
}

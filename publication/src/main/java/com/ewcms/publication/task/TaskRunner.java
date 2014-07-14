/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.dao.PublishDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.deploy.DepTaskQueueable;

/**
 * 实现任务运行，任务按照先进先出顺序运行。
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class TaskRunner implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(TaskRunner.class);
    
    private final TaskQueueable genQueue;
    private final DepTaskQueueable depQueue;
    private final PublishDaoable publishDao;
    
    public TaskRunner(TaskQueueable genQueue,
    		DepTaskQueueable depQueue,PublishDaoable publishDao){
    	
    	this.genQueue = genQueue;
    	this.depQueue = depQueue;
    	this.publishDao = publishDao;
    }
    
    @Override
    public void run() {
        logger.info("GenHtml runner start...");
        
        while(true){
        	
        	if(Thread.interrupted()){
        		return ;
        	}
        	
            try {
            	Taskable task = genQueue.task();
            	if(task != null ){
            		generator(task);	
            	}
            } catch (Exception e) {
                logger.error("Task runned exception:{}",e);
            } 
        }
    }
    
    private void generator(Taskable task){
    	publishDao.startPublishTask(task.getId());
    	
    	for(;task.hasNext();){
    		List<DepTask> depTasks = task.next();
    		for(DepTask t : depTasks){
    			depQueue.register(t);	
    		}
    	}
    }
    
    public void shutdown(){
    	Thread.currentThread().interrupt();
    }
}

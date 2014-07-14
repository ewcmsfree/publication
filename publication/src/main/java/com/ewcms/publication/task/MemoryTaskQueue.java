/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.util.Assert;

import com.ewcms.publication.dao.PublishDaoable;

/**
 * 内存任务队列
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class MemoryTaskQueue implements TaskQueueable{
    private static final int DEFAULT_MAX_TASK = 50;

    private final LinkedBlockingQueue<Taskable> tasks ;
    private final PublishDaoable publishDao;
    
    public MemoryTaskQueue(PublishDaoable publishDao){
    	this(DEFAULT_MAX_TASK,publishDao);
    }
    
    public MemoryTaskQueue(int max,PublishDaoable publishDao){
    	tasks= new LinkedBlockingQueue<Taskable>(max);
    	this.publishDao = publishDao;
    }
    
    @Override
    public void register(Taskable task) {
        Assert.notNull(task,"task is null");
        try {
        	 if(!hasTask(task)){
       		    saveNewTask(task);
             	tasks.put(task);
             }
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
    }
    
    private boolean hasTask(Taskable task){
    	boolean has = false;
    	for(Taskable t : tasks){
    		if(task.getKey().equals(t.getKey())){
    			has = true;
    			break;
    		}
    	}
    	return has;
    }
    
    private void saveNewTask(Taskable task){
    	publishDao.newPublishTask(task.getId(), task.getParentId(), 
     			task.getSite().getId(), task.getRemark(), task.getTotalCount());
    }

    @Override
    public void remove(String id) {
    	Assert.notNull(id,"task id is null");
    	Iterator<Taskable> iterator = tasks.iterator();
        for(Taskable t = iterator.next(); iterator.hasNext() ; t = iterator.next()){
        	if(t.getId().equals(id)){
        	    iterator.remove();
        	}
        }
    }

    @Override
    public List<Taskable> getTasks(Integer siteId) {
    	Assert.notNull(siteId,"Site id is null");
        List<Taskable> list = new ArrayList<Taskable>();
        Iterator<Taskable> iterator = tasks.iterator();
        for(Taskable t = iterator.next(); iterator.hasNext() ; t = iterator.next()){
        	if(siteId.intValue() == t.getSite().getId().intValue()){
        		list.add(t);
        	}
        }
        return list;
    }
    
    @Override
    public void clearTasks(Integer siteId) {
    	Assert.notNull(siteId,"Site id is null");
    	Iterator<Taskable> iterator = tasks.iterator();
        for(Taskable t = iterator.next(); iterator.hasNext(); t = iterator.next()){
        	if(siteId.intValue() == t.getSite().getId().intValue()){
        		iterator.remove();
        	}
        }
    }

	@Override
	public Taskable task() {
		Taskable task = null;
		
		try {
			task = tasks.take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		return task;
	}
}

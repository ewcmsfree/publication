/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.task;

import java.util.List;

/**
 * 注册任务
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface TaskQueueable {
    
    /**
     * 注册新的任务
     * 
     * @param task 任务
     */
    void register(Taskable task);
    
    /**
     * 移除任务
     * 
     * @param taskId 任务编号
     */
    void remove(String taskId);
    
    /**
     * 获得站点所以任务
     * 
     * @param siteId 站点编号
     * @return
     */
    List<Taskable> getTasks(Integer siteId);
    
    /**
     * 关闭站点任务
     * <br>
     * 当站点配置发生改变，需要关闭注册中的服务，是新配置生效。
     * 
     * @param siteId 站点编号
     */
    void clearTasks(Integer siteId);
    
    /**
     * 得到生成页面任务
     * 
     * @return
     */
    Taskable task();
}

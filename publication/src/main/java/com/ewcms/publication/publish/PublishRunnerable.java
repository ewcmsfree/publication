/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.publish;

import com.ewcms.publication.deploy.DepTaskQueueable;
import com.ewcms.publication.task.TaskQueueable;
import com.ewcms.publication.task.Taskable;

/**
 * 站点任务发布接口
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface PublishRunnerable {

    /**
     * 发布任务
     */
    void start();
    
    /**
     * 关闭发布任务
     */
    void shutdown();
    
    /**
     * 添加发布任务
     * 
     * @param task 页面生成任务
     */
    void regTask(Taskable task);
    
    /**
     * 发布在运行状态
     * 
     * @return
     */
    boolean isRunning();
    
    /**
     * 设置发布任务队列
     * 
     * @param taskQueue
     */
    void setGenTaskQueue(TaskQueueable taskQueue);
    
    /**
     * 设置文件任务队列
     * 
     * @param taskQueue
     */
    void setDepTaskQueue(DepTaskQueueable taskQueue);
    
}

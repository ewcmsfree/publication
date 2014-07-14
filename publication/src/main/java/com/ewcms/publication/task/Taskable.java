package com.ewcms.publication.task;

import java.util.List;

import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.publish.PublishRunnerable;

/**
 * 生成发布任务接口
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface Taskable {
	
	/**
	 * 任务类型
	 */
	public enum TaskType {
		SITE, CHANNEL, HOME ,LIST, DETAIL, RESOURCE, TEMPLATESOURCE;
	}


    /**
     * 得到任务编号
     * 
     * @return
     */
    String getId();
    
    /**
     * 得到父任务编号
     * 
     * @return
     */
    String getParentId();
    
    /**
     * 任务关键字，防止重复发布
     * 
     * @return
     */
    String getKey();
    
    /**
     * 得到任务类型
     * 
     * @return
     */
    TaskType getTaskType();
    
    /**
     * 任务所属站点
     * 
     * @return
     */
    Site getSite();
    
    /**
     * 总任务数
     * 
     * @return
     */
    int getTotalCount();
    
    /**
     * 每次批处理数
     * 
     * @return
     */
    int getBatchSize();
    
    /**
     * 是子任务
     * 
     * @return true:是子任务
     */
    boolean isChild();
    
    /**
     * 任务描述
     * 
     * @return
     */
    String getRemark();
    
    /**
     * 得到发布任务列表
     * 
     * @return
     */
    List<DepTask> next();
    
    /**
     * 还有任务
     * 
     * @return
     */
    boolean hasNext();
    
    /**
     * 注册需要发布任务
     * 
     * @param pubRunner
     */
    void regTask(PublishRunnerable pubRunner);
}

package com.ewcms.publication.dao;

public interface PublishDaoable {
	
	/**
	 * 创建任务
	 * 
	 * @param id 任务编号
	 * @param parentId 所属父任务编号
	 * @param siteId 站点编号
	 * @param remark 备注
	 * @param count 任务数
	 */
	void newPublishTask(String id, String parentId, Long siteId, String remark,Integer count);
	
	/**
	 * 发布任务开始
	 * 
	 * @param id 任务编号
	 */
	void startPublishTask(String id);
	
	/**
	 * 任务发布完成
	 * 
	 * @param taskId 任务编号
	 */
	void finishPublishTask(String taskId);
	
	/**
	 * 发布错误，记录错误日志
	 * 
	 * @param taskId 任务编号
	 * @param remark 错误说明
	 * @param exception 错误消息
	 */
	void publishErrorLog(String taskId, String remark, String exception);
	
	/**
	 * 发布文章成功
	 * 
	 * @param id 文章编号
	 * @param url 发布URL地址
	 */
	void publishArticle(Long id, String url);
	
	 /**
     * 发布资源成功
     * <br>
     * 标示资源为发布状态。
     * 
     * @param id 资源编号
     */
    void publishResource(Long id);
    
    /**
     * 发布模板资源成功
     * <br>
     * 标示模版资源已经发布，Release = true。
     * 
     * @param id 模板资源编号
     */
    void publishTemplateSource(Long id);
	
	/**
	 * 完成一个任务
	 * 
	 * @param id 任务编号
	 * @param success 任务是否成功 
	 */
	void completeOne(String id, boolean success);
}

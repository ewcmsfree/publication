/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication;

import java.util.List;

import com.ewcms.publication.task.Taskable;

/**
 * 发布服务接口
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface PublishServiceable {

    /**
     * 发布指定的模版资源
     * </br>
     * 如果模版资源已发布，则会重新发布
     * 
     * @param siteId 站点编号
     * @param ids 模版资源编号集合
     * @throws PublishException
     */
    void pubTemplateSource(Long siteId,List<Long> ids)throws PublishException;
    
    /**
     * 发布站点中的模版资源
     * 
     * @param siteId 站点编号
     * @param again 重新发布
     * @throws PublishException
     */
    void pubTemplateSource(Long siteId,boolean again)throws PublishException;
    
    /**
     * 发布指定的资源
     * </br>
    * 如果资源已发布，则会重新发布
     * 
     * @param siteId 站点编号
     * @param ids 资源编号集合
     * @throws PublishException
     */
    void pubResource(Long siteId,List<Long> ids)throws PublishException;
    
    /**
     * 发布站点中的资源
     * <br>
     * 发布文章内容资源
     * 
     * @param siteId 站点编号
     * @param again 重新发布
     * @throws PublishException
     */
    void pubResource(Long siteId,boolean again)throws PublishException;
    
    /**
     * 发布模版对应生成的页面
     *  
     * @param templateId 模版编号
     * @param again 重新发布
     * @throws PublishException
     */
    void pubTemplate(Long siteid,Long channelId,Long templateId,boolean again)throws PublishException;
    
    /**
     * 发布频道下生成的页面
     * </br>
     * 依赖频道下的模版
     * 
     * @param siteId 站点编号
     * @param channelId 频道编号
     * @param child 发布子频道
     * @param again 重新发布
     * @throws PublishException
     */
    void pubChannel(Long siteId, Long channelId, boolean child, boolean again) throws PublishException;
    
    /**
     * 发布站点
     * 
     * @param siteId 站点编号
     * @param again 重新发布
     * @throws PublishException
     */
    void pubSite(Long siteId,boolean again)throws PublishException;
    
    /**
     * 发布指定的文章
     * </br>
     * 只有预发布和发布状态文章才能发布，如果文章发布会再次发布。
     * 
     * @param channelId 频道编号
     * @param ids 文章编号集合
     * @throws PublishException
     */
    void pubArticle(Long siteId, Long channelId,List<Long> ids)throws PublishException;
    
    /**
     * 得到站点的发布任务
     * 
     * @param siteId 站点编号
     * @return
     */
    List<Taskable> getSiteTask(Long siteId);
    
    /**
     * 关闭发布任务
     * 
     * @param siteId 站点编号
     * @param id 任务编号
     * @throws PublishException
     */
    void closeTask(Long siteId,String id)throws PublishException;
}

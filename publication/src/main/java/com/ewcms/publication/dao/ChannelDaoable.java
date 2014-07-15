/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.dao;

import java.util.List;

import com.ewcms.publication.module.Channel;

/**
 * 频道加载接口
 * <br>
 * 生成页面时需要依赖频道。
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface ChannelDaoable extends CacheDaoable{

    /**
     * 得到站点顶级频道
     * 
     * @param siteId 站点编号
     * @return
     */
    Channel findRoot(Long siteId);
    
    /**
     * 通过频道编号得到发布频道对象
     * <br>
     * 频道必须是指定站点的频道。
     * 
     * @param siteId 站点编号
     * @param id 频道编号
     * @return 频道对象
     */
    Channel findPublishOne(Long siteId,Long id);
    
    /**
     * 得到所属的发布的子频道
     * 
     * @param siteId 站点编号
     * @param id 频道编号
     * @return
     */
    List<Channel> findPublishChildren(Long siteId,Long id);
    
    /**
     * 得到所属频道发布的父频道 
     * 
     * @param siteId 站点编号
     * @param id 频道编号
     * @return
     */
    Channel findPublishParent(Long siteId,Long id);
    
    /**
     * 通过频道访问链接地址和路径得到频道
     * 
     * @param siteId 站点编号
     * @param uri 频道链接地址或路径
     * @return 频道对象
     */
    Channel findPublishByUri(Long siteId, String uri);
    
}

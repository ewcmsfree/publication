/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.dao;

import java.util.List;

import com.ewcms.publication.module.TemplateSource;

/**
 * 模板资源加载和操作接口
 * <br>
 * 提供发布模板资源所需要的数据，并更改以发布文章状态。
 * 
 * @author wangwei
 */
public interface TemplateSourceDaoable {

    /**
     * 查询需要发布的模板资源
     * <br>
     * 再发布时会得到所有模版资源。
     * 
     * @param siteId 站点编号
     * @param forceAgain 再发布 
     * @param startId 查询开始ID
     * @param limit 查询记录数
     * @return 模板资源列表
     */
    List<TemplateSource> findPublish(Integer siteId, Boolean forceAgain, Long startId, Integer limit);
    
    /**
     * 查询需要发布的模板资源
     * <br>
     * 再发布时会得到所有模版资源数。
     * 
     * @param siteId 站点编号
     * @param forceAgain 再发布 
     * @return 模板资源列表
     */
    Integer findPublishCount(Integer siteId, Boolean forceAgain);
    
    /**
     * 得到模板资源,包括子资源
     * 
     * @param siteId 站点编号
     * @param ids 模板资源编号集合
     * @return 模板资源
     */
    List<TemplateSource> findPublish(Integer siteId,List<Long> ids);
    
    /**
     * 通过uri查询模板资源
     * 
     * @param siteId
     * @param uri
     * @return
     */
    TemplateSource findByUri(Integer siteId,String uri);
}

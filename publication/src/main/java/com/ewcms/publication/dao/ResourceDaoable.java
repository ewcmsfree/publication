/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.dao;

import java.util.List;

import com.ewcms.publication.module.Resource;

/**
 * 资源加载和操作接口
 * <br>
 * 提供发布资源所需要的数据，并更改以发布发状态。
 * 
 * @author wangwei
 */
public interface ResourceDaoable {

    /**
     * 得到资源
     * 
     * @param siteId 站点编号
     * @param ids  发布资源编号集合
     * @return
     */
	List<Resource> findPublish(Integer siteId, List<Long> ids);
    
	/**
     * 查询需要发布的资源数
     * <br>
     * 再发布时会得到所有要发布的资源（包括：normal和released）。
     * 
     * @param siteId 站点编号
     * @param forceAgain 再发布 
     * @return 需要发布的资源
     */
    Integer findPublishCount(Integer siteId, Boolean forceAgain);
    
    /**
     * 查询需要发布的资源
     * <br>
     * 再发布时会得到所有要发布的资源（包括：normal和released）。
     * 
     * @param siteId 站点编号
     * @param forceAgain 再发布 
     * @param startId 查询资源编号位置
     * @param limit 查询记录数
     * @return 需要发布的资源
     */
    List<Resource> findPublish(Integer siteId, Boolean forceAgain,Long startId,Integer limit);

    /**
     * 通过uri查询资源
     * 
     * @param siteId 站点编号
     * @param uri 
     * @return
     */
    Resource findByUri(Integer siteId, String uri);
}

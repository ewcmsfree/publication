/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.dao;

import com.ewcms.publication.module.Site;

/**
 * 站点接口
 * <br>
 * 发布资源时需要依赖站点信息。
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface SiteDaoable extends CacheDaoable{

    /**
     * 通过站点编号得到站点
     * 
     * @param id 站点编号
     * @return
     */
    public Site findOne(Long id);
}

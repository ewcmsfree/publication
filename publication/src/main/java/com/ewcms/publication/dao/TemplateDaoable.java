/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.dao;

import java.util.List;

import com.ewcms.publication.module.Template;
import com.ewcms.publication.module.TemplateBody;

/**
 * 模板加载服务
 * <br>
 * 提供生成页面所需要的模板。
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface TemplateDaoable extends CacheDaoable {

    /**
     * 得到模板对象
     * 
     * @param id b模板编号
     * @return 
     */
    Template findOne(Integer channelId, Integer id);
    
    /**
     * 得到频道下所有模板
     * <br>
     * 得到已经发布模板，如有文章模板必需排在第一个
     * 
     * @param channelId 频道频道编号
     * @return 模板对象
     */
    List<Template> findInChannel(Integer channelId);
    
    /**
     * 通过UniquePath得到模板，模板不存在返回null值
     * 
     * @param path 模板唯一路径
     * @return
     */
    TemplateBody findBody(String path);
    
    /**
     * 得到频道模板唯一路径
     * 
     * @param siteId 站点编号
     * @param channelId 频道编号
     * @param name 模板名称
     * 
     * @return
     */
    String findUniquePath(Integer siteId,Integer channelId,String name);
    
    /**
     * 保存模板验证信息
     * 
     * @param templateId 模板编号
     * @param verify true:验证通过，false:验证未通过
     */
    void saveVerifyTemplate(Integer templateId, boolean verify);
}

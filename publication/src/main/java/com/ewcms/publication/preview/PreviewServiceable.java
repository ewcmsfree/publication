/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.preview;

import java.io.Writer;

import com.ewcms.publication.PublishException;

/**
 * 预览接口
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface PreviewServiceable {

    /**
     * 模板预览
     * 
     * @param writer       
     *          输出数据流
     * @param siteId
     *          站点编号
     * @param channelId 
     *          频道编号
     * @param templateId
     *          模板编号
     * @param mock
     *          是否模拟 
     * @throws PublishException
     */
    void viewTemplate(Writer writer,int siteId, int channelId, int templateId, boolean mock)throws PublishException;
       
    /**
     * 文章预览
     * 
     * @param writer       
     *          输出数据流
     * @param channelId
     *          频道编号
     * @param articleId
     *          文章编号
     * @param pageNumber
     *          页数 
     * @throws PublishException
     */
    void viewArticle(Writer writer, int siteId, int channelId, long articleId, int pageNumber)throws PublishException;
    
    /**
     * 验证模板是否有效，并更新模板数据库信息
     * 
     * @param siteId 站点编号
     * @param channelId 频道编号
     * @param templateId 模板编号
     * @return
     */
    boolean verifyTemplate(int siteId, int channelId, int templateId);
}

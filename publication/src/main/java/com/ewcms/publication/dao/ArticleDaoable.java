/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.dao;

import java.util.List;

import com.ewcms.publication.module.Article;
import com.ewcms.publication.module.ArticleInfo;

/**
 * 文章加载和操作接口
 * <br>
 * 提供生成文章所需要的数据，并更改以发布文章状态。
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface ArticleDaoable {

    /**
     * 得到指定的发布文章集合
     * 
     * @param ids 文章编号
     * @return
     */
    List<Article> findPrePublish(List<Long> ids);

    /**
     * 得到频道准备发布的文章总数
     * 
     * @param channelId 频道编号
     * @param forceAgain 重新发布
     * @return 
     */
    int findPrePublishCount(Long channelId, Boolean forceAgain);
    
    /**
     * 查询准备发布的文章
     * <br>
     * 再发布时会得到所有要发布的文章（PreRelease和Release）。
     * 
     * @param channelId 频道编号 
     * @param forceAgain 重新发布
     * @param startId 开始查询文章ID
     * @param limit 最大文章数
     * @return
     */
    List<Article> findPrePublish(Long channelId,Boolean forceAgain,Long startId, Integer limit);
    
    /**
     * 得到频道指定页面文章
     * 
     * <br>
     * 查询的文章已经发布，按照文章定义的顺序显示，页数从0开始。
     * 
     * @param channelId  频道编号
     * @param page 页数
     * @param row 行数
     * @param top  顶置文章
     * @return
     */
    List<ArticleInfo> findPublish(Long channelId,Integer page,Integer row,Boolean top);
    
    /**
     * 得到指定已经发布文章集合
     * 
     * @param ids 文章编号
     * @return
     */
    List<ArticleInfo> findPublish(List<Long> ids);
    
    /**
     * 得到频道已经发布的文章总数
     * <br>
     * 如果已经发布文章总数大于频道最大显示记录数，则返回最大记录数
     * 
     * @param channelId 频道编号
     * @return 
     */
    int findPublishCount(Long channelId);
}

/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker;

/**
 * EWCMS系统定义的 Freemarke 全局变量
 *
 * SITE:当前站点
 * CHANNEL:当前频道
 * ARTICLE:当前文章
 * ARTICLE_LIST:当前文章集合
 * DEBUG:调试模式
 * PAGE_NUMBER:页数
 * PAGE_COUNT:总页数
 * PAGE_OUT:显示页数对象
 * INDEX:所处位置
 * URI_RULE:uri生成规则
 * TASK_ID:发布任务编号
 * INCLUDE_CACHE:包括缓存
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public enum GlobalVariable {

    SITE("ewcms_current_site"),
    CHANNEL("ewcms_current_channel"),
    ARTICLE("ewcms_current_article"),
    ARTICLE_LIST("ewcms_current_article_list"),
    DEBUG("ewcms_debug"),
    PAGE_NUMBER("ewcms_page_number"),
    PAGE_COUNT("ewcms_page_count"),
    PAGE_OUT("ewcms_page_out"),
    INDEX("ewcms_index"),
    URI_RULE("ewcms_uri_rule"),
    TASK_ID("ewcms_task_id"),
    INCLUDE_CACHE("ewcms_task_include_cache");
        
    private String name;

    private GlobalVariable(String name) {
        this.name = name;
    }
    
    public String getVariable(){
    	return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

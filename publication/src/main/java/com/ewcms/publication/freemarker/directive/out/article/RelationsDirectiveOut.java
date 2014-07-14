/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.out.article;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.freemarker.directive.out.HtmlDirectiveOut;
import com.ewcms.publication.module.ArticleInfo;

import freemarker.core.Environment;
import freemarker.template.TemplateException;

/**
 * 引用文章标签输出
 * 
 * @author wangwei
 */
public class RelationsDirectiveOut extends HtmlDirectiveOut {
    private static final Logger logger = LoggerFactory.getLogger(RelationsDirectiveOut.class);
    
    private final ArticleDaoable articleService;
    
    public RelationsDirectiveOut(ArticleDaoable articleService){
    	this.articleService = articleService;
    }
   
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object loopValue(Object value, Environment env, Map params)throws TemplateException {
        Assert.notNull(value);
        List<ArticleInfo> articles =getRelation((List<Long>)value);
        return articles;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String constructOut(Object value, Environment env, Map params)throws TemplateException {
        Assert.notNull(value);
        List<Long> list = (List<Long>)value;
        if(EmptyUtil.isCollectionEmpty(list)){
            logger.debug("Relateds is empty");
            return null;
        }
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("<ul");
        String c = getClassValue(params);
        if(EmptyUtil.isNotNull(c)){builder.append(" class=\"").append(c).append("\"");}
        String s = getStyleValue(params);
        if(EmptyUtil.isNotNull(s)){builder.append(" style=\"").append(s).append("\"");}
        builder.append(">");
        List<ArticleInfo> articles = this.getRelation(list);
        for(ArticleInfo article : articles){
            builder.append("<li>");
            builder.append("<a href=\"").append(article.getUrl()).append("\" title=\"").append(article.getTitle()).append("\" target=\"_blank\">");
            builder.append(article.getTitle());
            builder.append("</a>");
            builder.append("</li>");
        }
        builder.append("</ul>");
        
        return builder.toString();
    }
   
    private List<ArticleInfo> getRelation(List<Long> ids){
    	return articleService.findPublish(ids);
    }
}

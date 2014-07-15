/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
/**

 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.freemarker.FreemarkerTest;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.module.ArticleInfo;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * ArticleListDirective单元测试
 * 
 * @author wangwei
 */
public class ArticleListDirectiveTest extends FreemarkerTest {
	
	private ArticleDaoable dao;
	
	@Before
	public void before(){
		dao = Mockito.mock(ArticleDaoable.class);
	}

    @Override
    protected void currentConfiguration(Configuration cfg) {
        cfg.setSharedVariable("article", new ArticleDirective(dao));
        cfg.setSharedVariable("index", new IndexDirective());
    }

    private String getTemplatePath(String name){
        return String.format("directive/articlelist/%s", name);
    }
    
    @Test
    public void testChannelIsNull()throws Exception{
        ChannelDaoable dao = mock(ChannelDaoable.class);
        when(dao.findPublishOne(any(Long.class), any(Long.class))).thenReturn(null);
        ArticleListDirective directive = new ArticleListDirective(dao,null);
        cfg.setSharedVariable("alist", directive);
        
        Template template = cfg.getTemplate(getTemplatePath("value.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(1L);
        params.put(GlobalVariable.SITE.toString(), site);
        String value = process(template, params);
        Assert.assertEquals("throws Exception", value);
    }
    
    private List<ArticleInfo> createArticleRow(int row) {
        List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
        for(int i = 0 ; i < row ; i++){
            
        	ArticleInfo article = new ArticleInfo();
            article.setId(new Long(i));
            article.setAuthor("王伟");
            article.setOrigin("163.com");
            article.setTitle("ewcms文章标签使用" + String.valueOf(i));
            article.setShortTitle("文章标签使用");
            article.setSummary("介绍ewcms文章中的标签使用方法。");
            article.setImage("http://www.jict.org/image/test.jpg");
            
            articles.add(article);
        }
        return articles;
    }
    
    @Test
    public void testChannelIdNotExist()throws Exception{
        ChannelDaoable dao = mock(ChannelDaoable.class);
        when(dao.findPublishOne(any(Long.class), any(Long.class))).thenReturn(null);
        
        ArticleListDirective directive = new ArticleListDirective(dao,null);
        cfg.setSharedVariable("alist", directive);
        
        Template template = cfg.getTemplate(getTemplatePath("value.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(1L);
        params.put(GlobalVariable.SITE.toString(), site);
        String value = process(template, params);
        Assert.assertEquals("throws Exception", value);
    }
    
    @Test
    public void testValueTemplate()throws Exception{
        ChannelDaoable dao = mock(ChannelDaoable.class);
        Channel channel = new Channel();
        channel.setId(1L);
        when(dao.findPublishOne(any(Long.class), any(Long.class))).thenReturn(channel);
        
        ArticleDaoable articleLoaderService = mock(ArticleDaoable.class);
        when(articleLoaderService.findPublish(
        		Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(createArticleRow(10));
        
        ArticleListDirective directive = new ArticleListDirective(dao,articleLoaderService);
        cfg.setSharedVariable("alist", directive);
        
        Template template = cfg.getTemplate(getTemplatePath("value.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(1L);
        params.put(GlobalVariable.SITE.getVariable(), site);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        String value = process(template, params);
        value = StringUtils.deleteWhitespace(value);
        
        StringBuilder expected = new StringBuilder();
        for(int i = 0 ; i < 10 ; i++){
            expected.append(i+1).append(".ewcms文章标签使用").append(i);
        }
        
        Assert.assertEquals(expected.toString(), value);
    }
    
    @Test
    public void testLoopsTemplate()throws Exception{
        ChannelDaoable dao = mock(ChannelDaoable.class);
        Channel channel = new Channel();
        channel.setId(1L);
        when(dao.findPublishByUri(any(Long.class), any(String.class))).thenReturn(channel);
        when(dao.findPublishOne(any(Long.class), any(Long.class))).thenReturn(channel);
        
        ArticleDaoable articleLoaderService = mock(ArticleDaoable.class);
        when(articleLoaderService.findPublish(
        		Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(createArticleRow(25));
        ArticleListDirective directive = new ArticleListDirective(dao,articleLoaderService);
        
        cfg.setSharedVariable("alist", directive);
        
        Template template = cfg.getTemplate(getTemplatePath("loop.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(1L);
        params.put(GlobalVariable.SITE.toString(), site);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        String value = process(template, params);
        value = StringUtils.deleteWhitespace(value);
        
        StringBuilder expected = new StringBuilder();
        for(int i = 0 ; i < 25 ; i++){
            expected.append("ewcms文章标签使用").append(i);
        }
        
        Assert.assertEquals(expected.toString(), value);
    }
    
    @Test
    public void testDefaultRowTemplate()throws Exception{
        ChannelDaoable dao = mock(ChannelDaoable.class);
        Channel channel = new Channel();
        channel.setId(1L);
        channel.setListSize(12);
        when(dao.findPublishByUri(any(Long.class), any(String.class))).thenReturn(channel);
        when(dao.findPublishOne(any(Long.class), any(Long.class))).thenReturn(channel);
        
        ArticleDaoable articleLoaderService = mock(ArticleDaoable.class);
        when(articleLoaderService.findPublish(
        		Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(createArticleRow(12));
        ArticleListDirective directive = new ArticleListDirective(dao,articleLoaderService);
        
        cfg.setSharedVariable("alist", directive);
        
        Template template = cfg.getTemplate(getTemplatePath("defaultrow.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(1L);
        params.put(GlobalVariable.SITE.toString(), site);
        params.put(GlobalVariable.CHANNEL.toString(), channel);
        String value = process(template, params);
        value = StringUtils.deleteWhitespace(value);
        
        StringBuilder expected = new StringBuilder();
        for(int i = 0 ; i < 12 ; i++){
            expected.append(i+1).append(".ewcms文章标签使用").append(i);
        }
        
        Assert.assertEquals(expected.toString(), value);
    }
}

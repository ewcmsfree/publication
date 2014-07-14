/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.out.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.Relation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.module.Article;
import com.ewcms.publication.module.ArticleInfo;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateScalarModel;

/**
 * RelationsDirectiveOut单元测试
 * 
 * @author wangwei
 */
public class RelationsDirectiveOutTest {
	
	private ArticleDaoable dao;
	
	@Before
	public void before(){
		dao = Mockito.mock(ArticleDaoable.class);
		List<ArticleInfo> articles = new ArrayList<ArticleInfo>();
		ArticleInfo article = new ArticleInfo();
	    article.setUrl("/index.html");
	    article.setTitle("test");
	    articles.add(article);
	    article = new ArticleInfo();
        article.setUrl("/index1.html");
        article.setTitle("test1");
        articles.add(article);
		Mockito.when(dao.findPublish(Mockito.anyListOf(Long.class))).thenReturn(articles);
	}
    
    @SuppressWarnings("unchecked")
    @Test
    public void testLoopValue()throws Exception{
        RelationsDirectiveOut out = new RelationsDirectiveOut(dao);
        List<Article> list = (List<Article>)out.loopValue(initRelateds(), null, null);
        Assert.assertTrue(list.size() == 2);
    }
    
    @Test
    public void testRelatedIsEmptyOfConstructOut()throws Exception{
        RelationsDirectiveOut out = new RelationsDirectiveOut(dao);
        String outValue = out.constructOut(new ArrayList<Relation>(),null, null);
        Assert.assertNull(outValue);
    }
    
    @Test
    public void testConstructOut()throws Exception{
        RelationsDirectiveOut out = new RelationsDirectiveOut(dao);
        String outValue = out.constructOut(initRelateds(), null, null);
        String expected = "<ul>" +
        		"<li><a href=\"/index.html\" title=\"test\" target=\"_blank\">test</a></li>" +
        		"<li><a href=\"/index1.html\" title=\"test1\" target=\"_blank\">test1</a></li>" +
        		"</ul>";
        Assert.assertEquals(expected, outValue);
    }
    
    @Test
    public void testClassAndStyleOfConstructOut()throws Exception{
        RelationsDirectiveOut out = new RelationsDirectiveOut(dao);
        
        Map<String,TemplateScalarModel> params = new HashMap<String,TemplateScalarModel>();
        params.put("class", new SimpleScalar("test_class"));
        params.put("style", new SimpleScalar("test_style"));
        
        String outValue = out.constructOut(initRelateds(), null, params);
        String expected = "<ul class=\"test_class\" style=\"test_style\">" +
                "<li><a href=\"/index.html\" title=\"test\" target=\"_blank\">test</a></li>" +
                "<li><a href=\"/index1.html\" title=\"test1\" target=\"_blank\">test1</a></li>" +
                "</ul>";
        Assert.assertEquals(expected, outValue);
    }
    
    private List<Long> initRelateds(){
        List<Long> list = new ArrayList<Long>();
        
        list.add(1L);
        list.add(2L);
        
        return list;
    }
}

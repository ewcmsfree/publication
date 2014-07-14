/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.freemarker.FreemarkerTest;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.out.DateDirectiveOut;
import com.ewcms.publication.freemarker.directive.out.DefaultDirectiveOut;
import com.ewcms.publication.freemarker.directive.out.DirectiveOutable;
import com.ewcms.publication.module.Article;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * ArticleDirective单元测试
 * 
 * @author wangwei
 */
public class ArticleDirectiveTest extends FreemarkerTest{
	
	private ArticleDaoable dao;
	
	public void before(){
		dao = Mockito.mock(ArticleDaoable.class);
	}

    @Test
    public void testNewPutDirective()throws Exception{
        ArticleDirective directive = new ArticleDirective(dao);
        directive.putDirective("测试", "test", new DefaultDirectiveOut());
        
        String name = directive.getPropertyName("测试");
        Assert.assertEquals("test", name);
        name = directive.getPropertyName("test");
        Assert.assertEquals("test", name);
        
        DirectiveOutable out = directive.getDirectiveOut("test");
        Assert.assertNotNull(out);
    }
    
    @Test
    public void testUpdatePutDirective()throws Exception{
        ArticleDirective directive = new ArticleDirective(dao);
        directive.putDirective("文章标题","title", new DateDirectiveOut());
        
        String name = directive.getPropertyName("文章标题");
        Assert.assertEquals("title", name);
        name = directive.getPropertyName("标题");
        Assert.assertEquals("title", name);
        name = directive.getPropertyName("title");
        Assert.assertEquals("title", name);
        
        DirectiveOutable out = directive.getDirectiveOut("title");
        Assert.assertEquals(DateDirectiveOut.class, out.getClass());
    }
    
    @Test
    public void testAlreadyHasAlias()throws Exception{
        String[] aliases = initAliases();
        
        ArticleDirective directive = new ArticleDirective(dao);
        
        for(String alias : aliases){
            String name = directive.getPropertyName(alias);
            Assert.assertNotNull(name);
        }
    }
    
    @Test
    public void testAlreadyHasDirectiveOut()throws Exception{
        String[] aliases = initAliases();
        
        ArticleDirective directive = new ArticleDirective(dao);
        
        for(String alias : aliases){
            String name = directive.getPropertyName(alias);
            DirectiveOutable out = directive.getDirectiveOut(name);
            Assert.assertNotNull(out);
        }
    }
        
    private String[] initAliases(){
        return new String[]{
                "编号","标题","短标题","副标题","作者",
                "引导图","摘要","来源","关键字","标签",
                "链接地址","关联文章","正文","创建时间","修改时间",
                "发布时间"
        };
    }

    @Override
    protected void currentConfiguration(Configuration cfg) {
        cfg.setSharedVariable("article", new ArticleDirective(dao));
    }
    
    /**
     * 得到模板路径
     * 
     * @param name 模板名
     * @return
     */
    private String getTemplatePath(String name){
        return String.format("directive/article/%s", name);
    }
    
    @Test
    public void testArticleTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("article.html"));

        Map<Object,Object> params= new HashMap<Object,Object>();
        Article article = new Article();
        article.setTitle("标题");
        params.put(GlobalVariable.ARTICLE.toString(),article);
        String value = this.process(template, params);
        
        value = StringUtils.deleteWhitespace(value);
        Assert.assertTrue(value.indexOf("标题_alias") != -1);
        Assert.assertTrue(value.indexOf("标题_property") != -1);
        Assert.assertTrue(value.indexOf("</html>") != -1);
    }
}

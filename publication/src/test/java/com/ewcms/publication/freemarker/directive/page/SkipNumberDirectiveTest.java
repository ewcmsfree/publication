/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.freemarker.FreemarkerTest;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.uri.UriRuleable;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * SkipNumberDirective单元测试
 * 
 * @author wangwei
 */
public class SkipNumberDirectiveTest extends FreemarkerTest {

	private static final Logger logger = LoggerFactory.getLogger(SkipNumberDirectiveTest.class);
	
    @Test
    public void testMaxIsOneGetPageOuts()throws Exception{
        NumberPageDirective directive = new NumberPageDirective();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(new HashMap<String,Object>())).thenReturn("");
        List<PageOut> pages = directive.getPageOuts(rule,5, 0, 1, "..",new HashMap<String,Object>());
        Assert.assertEquals(2, pages.size());
        Assert.assertEquals(pages.get(0).getLabel(), "1");
        Assert.assertFalse(pages.get(0).isActive());
        Assert.assertEquals(pages.get(1).getLabel(),"..");
    }
    
    @Test
    public void testMaxGeCountGetPageOuts()throws Exception{
        NumberPageDirective directive = new NumberPageDirective();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(new HashMap<String,Object>())).thenReturn("");
        List<PageOut> pages = directive.getPageOuts(rule,5, 0, 6, "",new HashMap<String,Object>());
        Assert.assertEquals(5, pages.size());
        Assert.assertEquals(pages.get(0).getLabel(), "1");
        Assert.assertFalse(pages.get(0).isActive());
        Assert.assertEquals(pages.get(4).getLabel(),"5");
        Assert.assertTrue(pages.get(4).isActive());
    }
    
    @Test
    public void testMaxGePageNumberGetPageOuts()throws Exception{
        NumberPageDirective directive = new NumberPageDirective();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(new HashMap<String,Object>())).thenReturn("");
        List<PageOut> pages = directive.getPageOuts(rule,10, 5, 7, "..",new HashMap<String,Object>());
        Assert.assertEquals(8, pages.size());
        Assert.assertEquals(pages.get(0).getLabel(), "1");
        Assert.assertEquals(pages.get(7).getLabel(),"..");
    }
    
    @Test
    public void testMiddleGetPgeOuts()throws Exception{
        NumberPageDirective directive = new NumberPageDirective();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(new HashMap<String,Object>())).thenReturn("");
        List<PageOut> pages = directive.getPageOuts(rule,20, 10, 7, "..",new HashMap<String,Object>());
        Assert.assertEquals(8, pages.size());
        Assert.assertEquals(pages.get(0).getLabel(), "..");
        Assert.assertEquals(pages.get(1).getLabel(),"8");
        Assert.assertEquals(pages.get(7).getLabel(), "..");
    }
    
    
    @Test
    public void testEndGetPgeOuts()throws Exception{
        NumberPageDirective directive = new NumberPageDirective();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(new HashMap<String,Object>())).thenReturn("");
        List<PageOut> pages = directive.getPageOuts(rule,20, 17, 7, "..",new HashMap<String,Object>());
        Assert.assertEquals(8, pages.size());
        Assert.assertEquals(pages.get(0).getLabel(), "..");
        Assert.assertEquals(pages.get(1).getLabel(),"14");
        Assert.assertEquals(pages.get(7).getLabel(), "20");
    }
    
    @Override
    protected void currentConfiguration(Configuration cfg) {
         cfg.setSharedVariable("page_number", new NumberPageDirective());
         cfg.setSharedVariable("page", new PageOutDirective());
    }
    
    /**
     * 得到模板路径
     * 
     * @param name 模板名
     * @return
     */
    private String getTemplatePath(String name){
        return String.format("directive/page/%s", name);
    }
    
    @Test
    public void testNumberTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("number.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.getVariable(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.PAGE_NUMBER.toString(), Integer.valueOf(10));
        params.put(GlobalVariable.PAGE_COUNT.toString(), Integer.valueOf(20));
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(params)).thenReturn("");
        params.put(GlobalVariable.URI_RULE.toString(), rule);
        String value = this.process(template, params);
        value = StringUtils.deleteWhitespace(value);
        String expected="..8-9-10-1112-13-..";
        Assert.assertEquals(expected, value);
    }
    
    @Test
    public void testNumberLoopTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("numberloop.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.getVariable(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.PAGE_NUMBER.toString(), Integer.valueOf(10));
        params.put(GlobalVariable.PAGE_COUNT.toString(), Integer.valueOf(20));
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(params)).thenReturn("");
        params.put(GlobalVariable.URI_RULE.toString(), rule);
        String value = this.process(template, params);
        value = StringUtils.deleteWhitespace(value);
        String expected="##8910111213##";
        Assert.assertEquals(expected, value);
    }
    
    @Test
    public void testNumberDefaultTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("numberdefault.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.getVariable(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.PAGE_NUMBER.toString(), Integer.valueOf(10));
        params.put(GlobalVariable.PAGE_COUNT.toString(), Integer.valueOf(20));
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(params)).thenReturn("");
        params.put(GlobalVariable.URI_RULE.toString(), rule);
        String value = this.process(template, params);
        StringBuilder expected = new StringBuilder();
        expected.append("..&nbsp;")
                      .append("<a href=''>8</a>&nbsp;")
                      .append("<a href=''>9</a>&nbsp;")
                      .append("<a href=''>10</a>&nbsp;")
                      .append("11&nbsp;")
                      .append("<a href=''>12</a>&nbsp;")
                      .append("<a href=''>13</a>&nbsp;")
                      .append("..&nbsp;");
        logger.info(value);
        Assert.assertEquals(expected.toString(), value);
    }
}

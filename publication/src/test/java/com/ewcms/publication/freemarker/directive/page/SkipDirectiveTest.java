/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.ewcms.publication.freemarker.FreemarkerTest;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.page.skip.Skipable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.uri.UriRuleable;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * SkipDirective单元测试
 * 
 * @author wangwei
 */
public class SkipDirectiveTest extends FreemarkerTest {

    @Override
    protected void currentConfiguration(Configuration cfg) {
        cfg.setSharedVariable("page_skip", new SkipDirective());
        cfg.setSharedVariable("page", new PageOutDirective());
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
    public void testInitAliasesMapProperties()throws Exception{
        
        String[] aliases = initAliases();
        SkipDirective directive = new SkipDirective();
        for(String alias : aliases){
        	Map map = new HashMap();
        	map.put("type", BeansWrapper.getDefaultInstance().wrap(alias));
            Skipable skipPage = directive.getSkipPage(map);
            Assert.assertNotNull(skipPage);
        }
    }
    
    private String[] initAliases(){
        return new String[]{
             "f","first","首","首页",
             "n","next","下","下一页",
             "p","prev","上","上一页",
             "l","last","末","末页"
        };
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
    public void testPageCountIsOnlyOne() throws Exception {
        Template template = cfg.getTemplate(getTemplatePath("skip.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.getVariable(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.PAGE_NUMBER.getVariable(), Integer.valueOf(0));
        params.put(GlobalVariable.PAGE_COUNT.getVariable(), Integer.valueOf(1));
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(params)).thenReturn("");
        String value = this.process(template, params);
        value = StringUtils.deleteWhitespace(value);
        Assert.assertEquals("|||",value);
    }
    
    @Test
    public void testSkipTemplate() throws Exception {
        Template template = cfg.getTemplate(getTemplatePath("skip.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.getVariable(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.PAGE_NUMBER.getVariable(), Integer.valueOf(0));
        params.put(GlobalVariable.PAGE_COUNT.getVariable(), Integer.valueOf(5));
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(params)).thenReturn("");
        params.put(GlobalVariable.URI_RULE.getVariable(), rule);
        String value = this.process(template, params);
        value = StringUtils.deleteWhitespace(value);
        Assert.assertEquals("第一页|下一页|上一页|未页",value);
    }

    @Test
    public void testLoopTemplate() throws Exception {
        Template template = cfg.getTemplate(getTemplatePath("skiploop.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.getVariable(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.PAGE_NUMBER.getVariable(), Integer.valueOf(0));
        params.put(GlobalVariable.PAGE_COUNT.getVariable(), Integer.valueOf(5));
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(params)).thenReturn("");
        params.put(GlobalVariable.URI_RULE.getVariable(), rule);
        String value = this.process(template, params);
        value = StringUtils.deleteWhitespace(value);
        Assert.assertEquals("第一页loop|5|1",value);
    }
}

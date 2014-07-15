/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.freemarker.FreemarkerTest;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * IncludeDirective单元测试
 *
 * @author wangwei
 */
public class IncludeDirectiveTest extends FreemarkerTest {

	private ChannelDaoable channelService;
	private TemplateDaoable templateService;
	private Cacheable<String,String> includeCache;
	
	 @Override
	 protected void currentConfiguration(Configuration cfg) {
		 channelService = mock(ChannelDaoable.class);
	     Channel channel = new Channel();
	     channel.setId(1L);
	     when(channelService.findPublishByUri(any(Long.class), any(String.class))).thenReturn(channel);
	     templateService = mock(TemplateDaoable.class);
	     when(templateService.findUniquePath(any(Long.class), any(Long.class), any(String.class))).thenReturn("2/1/include.html");
	     IncludeDirective directive = new IncludeDirective(channelService,templateService);
	     cfg.setSharedVariable("include", directive);
	 }
	 
    @Test
    public void testGetUniqueTemplatePath(){
       IncludeDirective directive = new IncludeDirective(channelService,templateService);
       String path = "/home/test.html";
       Long siteId=new Long(2);
       
       String uPath = directive.getUniqueTemplatePath(siteId, path);
       Assert.assertEquals("2/home/test.html", uPath);
       path = "home/test.html";
       uPath = directive.getUniqueTemplatePath(siteId, path);
       Assert.assertEquals("2/home/test.html", uPath);
    }
    
   
    
    /**
     * 得到模板路径
     * 
     * @param name
     *            模板名
     * @return
     */
    private String getTemplatePath(String name) {
        return String.format("directive/include/%s", name);
    }
    
    @Test
    public void testPathTemplate() throws Exception {
        Template template = cfg.getTemplate(getTemplatePath("path.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.toString(), site);
        String value = process(template,params);
        value = StringUtils.deleteWhitespace(value);
        Assert.assertEquals("test-path-include", value);
    }
    
    @Test
    public void testChannelTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("channel.html"));
        
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.toString(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.TASK_ID.getVariable(), "1111-2222-3333-4444");
        String value = process(template,params);
        value = StringUtils.deleteWhitespace(value);
        Assert.assertEquals("test-channel-includetest-channel-includetest-channel-include", value);
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void testCacheTemplate()throws Exception{
    	Template template = cfg.getTemplate(getTemplatePath("cache.html"));
        Map<String,Object> params = new HashMap<String,Object>();
        Site site = new Site();
        site.setId(2L);
        params.put(GlobalVariable.SITE.toString(), site);
        Channel channel = new Channel();
        channel.setId(1L);
        params.put(GlobalVariable.CHANNEL.getVariable(), channel);
        params.put(GlobalVariable.TASK_ID.getVariable(), "1111-2222-3333-4444");
        includeCache = Mockito.mock(Cacheable.class);
        Mockito.when(includeCache.get( Mockito.anyString())).thenReturn("hello-cache");
        params.put(GlobalVariable.INCLUDE_CACHE.getVariable(), includeCache);
        String value = process(template,params);
        value = StringUtils.deleteWhitespace(value);
        Assert.assertEquals("hello-cache", value);
    }
}

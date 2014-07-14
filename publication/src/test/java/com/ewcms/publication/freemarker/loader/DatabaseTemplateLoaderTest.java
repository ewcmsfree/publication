/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.loader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.freemarker.loader.DatabaseTemplateLoader;
import com.ewcms.publication.module.TemplateBody;

/**
 * DatabaseTemplateLoader单元测试
 * 
 * @author wangwei
 */
public class DatabaseTemplateLoaderTest {

    @Test
    public void testFindTemplateSourceTemplateIsNull()throws Exception{
        
        TemplateDaoable service = mock(TemplateDaoable.class);
        when(service.findBody(any(String.class))).thenReturn(null);
        DatabaseTemplateLoader loader = new DatabaseTemplateLoader(service);
        
        Object source = loader.findTemplateSource("/test.html");
        Assert.assertNull(source);
    }
    
    @Test
    public void testFindTemplateSourceContentIsNull()throws Exception{
        
        TemplateDaoable service = mock(TemplateDaoable.class);
        when(service.findBody(any(String.class))).thenReturn(null);
        DatabaseTemplateLoader loader = new DatabaseTemplateLoader(service);
        Object source = loader.findTemplateSource("/test.html");
        
        Assert.assertNull(source);
    }
    
    private Date updTime = new Date();
    
    private Object createTemplateSource()throws Exception{
        TemplateDaoable service = mock(TemplateDaoable.class);
        TemplateBody template = new TemplateBody();
        template.setBody("test".getBytes());
        when(service.findBody(any(String.class))).thenReturn(template);
        DatabaseTemplateLoader loader = new DatabaseTemplateLoader(service);
        return loader.findTemplateSource("/test.html");
    }
    
    @Test
    public void testFindTemplateSource()throws Exception{
        Object source = createTemplateSource();
        
        Assert.assertNotNull(source);
    }
    
    @Test
    public void testGetReader()throws Exception{
        Object source = createTemplateSource();
        
        TemplateDaoable service = mock(TemplateDaoable.class);
        DatabaseTemplateLoader loader = new DatabaseTemplateLoader(service);
        
        Reader reader = loader.getReader(source, "utf-8");
        char[] chars = new char[100];
        int len = reader.read(chars);
        
        Assert.assertEquals(4, len);
        Assert.assertEquals('t', chars[0]);
        Assert.assertEquals('t', chars[3]);
    }
    
    @Test
    public void testGetLastModified()throws Exception{
        Object source = createTemplateSource();
        
        TemplateDaoable service = mock(TemplateDaoable.class);
        DatabaseTemplateLoader loader = new DatabaseTemplateLoader(service);
        long  lastModified = loader.getLastModified(source);
        
        Assert.assertEquals(lastModified, updTime.getTime());
    }
    
}

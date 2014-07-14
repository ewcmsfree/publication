/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page.skip;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.ewcms.publication.freemarker.directive.page.PageOut;
import com.ewcms.publication.freemarker.directive.page.skip.FirstSkip;
import com.ewcms.publication.uri.UriRuleable;

/**
 * SkipPageFirst单元测试
 *
 * @author wangwei
 */
public class SkipPageFirstTest {

    @SuppressWarnings("unchecked")
	@Test
    public void testNumberIsFirst()throws Exception{
        FirstSkip skipFirst = new FirstSkip();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(Mockito.anyMap())).thenReturn("/test");
        PageOut page = skipFirst.skip(10,0,"first",rule,new HashMap<String,Object>());
        Assert.assertEquals("first", page.getLabel());
        Assert.assertEquals(page.getUrl(), "/test");
        Assert.assertEquals(Integer.valueOf(1), page.getNumber());
        Assert.assertFalse(page.isActive());
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void testSkipFirst()throws Exception{
        FirstSkip skipFirst = new FirstSkip();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(Mockito.anyMap())).thenReturn("http://test.com/dddd_0.html");
        PageOut page = skipFirst.skip(10,1,null,rule,new HashMap<String,Object>());
        Assert.assertEquals("首页", page.getLabel());
        Assert.assertEquals("http://test.com/dddd_0.html",page.getUrl());
        Assert.assertEquals(Integer.valueOf(1), page.getNumber());
        Assert.assertTrue(page.isActive());
    }
}

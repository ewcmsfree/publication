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
import com.ewcms.publication.freemarker.directive.page.skip.LastSkip;
import com.ewcms.publication.uri.UriRuleable;

/**
 * SkipPageLast单元测试
 * 
 * @author wangwei
 */
public class SkipPageLastTest {

    @Test
    public void testNumberIsLast()throws Exception{
        LastSkip skip = new LastSkip();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(new HashMap<String,Object>())).thenReturn("");
        PageOut page = skip.skip(10,9,"last",rule,new HashMap<String,Object>());
        Assert.assertEquals("last", page.getLabel());
        Assert.assertEquals("",page.getUrl());
        Assert.assertEquals(Integer.valueOf(10), page.getNumber());
        Assert.assertFalse(page.isActive());
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void testSkipLast()throws Exception{
        String url = "http://test.com/dddd_8.html";
        LastSkip skip = new LastSkip();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(Mockito.anyMap())).thenReturn(url);
        PageOut page = skip.skip(10,8,null,rule,new HashMap<String,Object>());
        Assert.assertEquals("未页", page.getLabel());
        Assert.assertEquals(url,page.getUrl());
        Assert.assertEquals(Integer.valueOf(10), page.getNumber());
        Assert.assertTrue(page.isActive());
    }
}

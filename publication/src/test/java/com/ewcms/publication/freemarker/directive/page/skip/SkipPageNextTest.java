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
import com.ewcms.publication.freemarker.directive.page.skip.NextSkip;
import com.ewcms.publication.uri.UriRuleable;

/**
 * SkipPageNext单元测试
 * 
 * @author wangwei
 */
public class SkipPageNextTest {

    @SuppressWarnings("unchecked")
	@Test
    public void testNumberIsLast()throws Exception{
        NextSkip skip = new NextSkip();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(Mockito.anyMap())).thenReturn("/last.html");
        PageOut page = skip.skip(10,9,"next",rule,new HashMap<String,Object>());
        Assert.assertEquals("next",page.getLabel());
        Assert.assertEquals(page.getUrl(),"/last.html");
        Assert.assertEquals(Integer.valueOf(10), page.getNumber());
        Assert.assertFalse(page.isActive());
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void testSkipNext()throws Exception{
        String url = "http://test.com/dddd_3.html";
        NextSkip skip = new NextSkip();
        UriRuleable rule = mock(UriRuleable.class);
        when(rule.uri(Mockito.anyMap())).thenReturn(url);
        PageOut page = skip.skip(10,8,"next",rule,new HashMap<String,Object>());
        Assert.assertEquals("next",page.getLabel());
        Assert.assertEquals(page.getUrl(),url);
        Assert.assertEquals(Integer.valueOf(10), page.getNumber());
        Assert.assertTrue(page.isActive());
        page = skip.skip(10,7,"next",rule,new HashMap<String,Object>());
        Assert.assertEquals(Integer.valueOf(9), page.getNumber());
        Assert.assertTrue(page.isActive());
    }
    
   
}

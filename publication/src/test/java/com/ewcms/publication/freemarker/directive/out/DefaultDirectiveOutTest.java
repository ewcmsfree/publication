/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.out;

import org.junit.Test;
import org.junit.Assert;

/**
 * DefaultDirectiveOut单元测试
 * 
 * @author wangwei
 */
public class DefaultDirectiveOutTest {

    @Test
    public void testConstructOut()throws Exception{
        DefaultDirectiveOut out = new DefaultDirectiveOut();
        String value = out.constructOut("test", null, null);
        Assert.assertEquals("test", value);
    }
    
    @Test
    public void testLoopValue()throws Exception{
        DefaultDirectiveOut out = new DefaultDirectiveOut();
        Object value = new Object();
        Object loopValue = out.loopValue(value, null, null);
        Assert.assertEquals(value, loopValue);
    }
}

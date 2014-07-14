/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.uri;

import java.util.Map;

import com.ewcms.publication.PublishException;

/**
 * 空的生成规则
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class NullUriRule implements UriRuleable {

    @Override
    public void setInitParameters(Map<String, Object> parameters) {
        //none instance
    }

    @Override
    public void putInitParameter(String parameter, Object value) {
        //none instance
    }

    @Override
    public String uri(Map<String,Object> parameters) throws PublishException {
        //none instance
    	return null;
    }

    @Override
    public String getPatter() {
    	//none instance
        return null;
    }

}

/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page.skip;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.page.PageOut;
import com.ewcms.publication.uri.UriRuleable;

/**
 * 跳转首页
 *
 * @author  <a href="hhywangwei@gmail.com">王伟</a>
 */
public class FirstSkip implements Skipable{

    private static final String DEFAULT_LABEL="首页";

    @Override
    public PageOut skip(Integer count,Integer number,String label,
    		UriRuleable uriRule, Map<String,Object> uriParams)throws PublishException{
        
        label = StringUtils.isBlank(label) ? DEFAULT_LABEL : label;
        int first = 0;
        boolean active = (number != first);
        String url = uriRule.uri(putPageParam(uriParams, first));
        
        return new PageOut(count,first,label,url,active);
    }
    
    private Map<String,Object> putPageParam(Map<String,Object> uriParams, Integer page){
    	uriParams.put(GlobalVariable.PAGE_NUMBER.getVariable(), page);
    	return uriParams;
    }
}

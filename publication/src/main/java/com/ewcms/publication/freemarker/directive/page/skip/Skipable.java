/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page.skip;

import java.util.Map;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.freemarker.directive.page.PageOut;
import com.ewcms.publication.uri.UriRuleable;

/**
 * 跳转页面
 * <br>
 * 
 * 根据参数得到跳转页面对象
 *  
 * @author  <a href="hhywangwei@gmail.com">王伟</a>
 *
 * @param <T> 跳转页面类型
 */
public interface Skipable {

    /**
     * 跳转页面对象
     * 
     * @param  count     总页数
     * @param  number    页数
     * @param  label     显示标签
     * @param  uriRule   uri生成规则
     * @param  uriParams 成uri参数
     * @throws PublishException
     * @return
     */
    PageOut skip(Integer count, Integer number, String label, 
    		UriRuleable uriRule, Map<String,Object> uriParams)throws PublishException;
}

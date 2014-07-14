/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.uri;

import java.util.Map;

import com.ewcms.publication.PublishException;

/**
 * 资源发布地址规则接口
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface UriRuleable {
    
	/**
	 * 设置参数集合
	 * 
	 * @param parameters
	 */
    void setInitParameters(Map<String,Object> parameters);
    
    /**
     * 放入参数到参数集合中,如果参数存在则覆盖参数
     * 
     * @param parameter 参数名
     * @param value 参数值
     */
    void putInitParameter(String parameter,Object value);
    
    /**
     * 得到uri规则模版
     * 
     * @return
     */
    String getPatter();
    
    /**
     * 得到通一资源地址
     * 
     * @param 生成URI参数
     * @return
     * @throws PublishException
     */
     String uri(Map<String,Object> parameters)throws PublishException;
     
}

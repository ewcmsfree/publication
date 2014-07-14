/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.generator;

import java.io.Writer;
import java.util.Map;

import com.ewcms.publication.PublishException;

/**
 * 生成生成页面
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public interface Generatorable {
 
	String getTaskId();
	
	String remark();
	
	Map<String,Object> getParameters();

	void process(Writer writer)throws PublishException;
	
	String pubPath()throws PublishException;
	
	String pubUri()throws PublishException;
	
	Long getMarkId();
}

/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.out;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.freemarker.FreemarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateException;

/**
 * 标签输出格式化日期
 * 
 * @author wangwei
 */
public class DateDirectiveOut implements DirectiveOutable{
    private static final Logger logger = LoggerFactory.getLogger(DateDirectiveOut.class);
    
    private static final String FORMAT_PARAM_NAME = "format";
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private String formatParam = FORMAT_PARAM_NAME;
    
    @SuppressWarnings("rawtypes")
    @Override
    public String constructOut(Object value,Environment env,Map params)throws TemplateException {
        Assert.notNull(value);
        DateFormat dateFormat = getDateFormat(params);
        return dateFormat.format(value);
    }
    
    @SuppressWarnings("rawtypes")
    private DateFormat getDateFormat(Map params) throws TemplateException {
        String format = FreemarkerUtil.getString(params, formatParam);
        logger.debug("DateFormate is {}:",format);
        if (EmptyUtil.isNull(format)) {
            return DEFAULT_FORMAT;
        }else{
            return new SimpleDateFormat(format);
        }
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object loopValue(Object value, Environment env, Map params)throws TemplateException {
        Assert.notNull(value);
        return value;
    }    
    
    /**
     * 设置日期格式参数
     * 
     * @param param 日期格式参数
     */
    public void setFormatParam (String param){
        formatParam = param;
    }
}

/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.out.DefaultDirectiveOut;
import com.ewcms.publication.freemarker.directive.out.DirectiveOutable;
import com.ewcms.publication.freemarker.directive.out.LengthDirectiveOut;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * 频道属性标签
 *
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class ChannelDirective extends PropertyDirective{
	private static final Logger logger = LoggerFactory.getLogger(ChannelDirective.class);
	
    private Map<String,String> aliasProperties = initDefaultAliasProperties();
	private Map<String,DirectiveOutable> propertyDirectiveOuts = initDefaultPropertyDirectiveOuts();

    
    @Override
    protected String defaultValueParamValue(){
        return GlobalVariable.CHANNEL.toString();
    }
      
    @SuppressWarnings("rawtypes")
    @Override
    protected Object loopValue(Object objectValue,String propertyName,Environment env, Map params)throws TemplateException, NoSuchMethodException{
        DirectiveOutable out = getDirectiveOut(propertyName);
        Object propertyValue = getValue(objectValue, propertyName);
        if(EmptyUtil.isNull(propertyValue)){
            return null;
        }
        if(propertyValue instanceof String 
                && StringUtils.isBlank((String)propertyValue)){
            return null;
        }
        return out.loopValue(propertyValue, env, params);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    protected String constructOut(Object objectValue,String propertyName,Environment env, Map params)throws TemplateException, NoSuchMethodException{
        DirectiveOutable out = getDirectiveOut(propertyName);
        Object propertyValue = getValue(objectValue, propertyName);
        if(EmptyUtil.isNull(propertyValue)){
            return null;
        }
        return out.constructOut(propertyValue, env, params);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    protected String getPropertyName(Environment env,Map params)throws TemplateException{
        String value = super.getPropertyName(env,params);
        return getPropertyName(value);
    }
    
    /**
     * 通过别名或属性名，得到对应的属性名
     * 
     * @param name 别名或属性名
     * @return 属性名
     * @throws TemplateModelException
     */
    protected String getPropertyName(String name)throws TemplateModelException{
        String property = aliasProperties.get(name);
        if(EmptyUtil.isNull(property)){
            logger.error("Get not property name of \"{}\"",name);
            throw new TemplateModelException("Get not property name of \""+name+"\"");
        }
        return property;
    }
    
    /**
     * 通过属性名得到对应标签输出类
     * 
     * @param propertyName 属性名
     * @return 输出标签对象
     * @throws TemplateModelException
     */
    protected DirectiveOutable getDirectiveOut(String propertyName)throws TemplateModelException{
        DirectiveOutable out = propertyDirectiveOuts.get(propertyName);
        if(EmptyUtil.isNull(out)){
            logger.error("Get not directive out of \"{}\"",propertyName);
            throw new TemplateModelException("Get not directive out of \""+propertyName+"\"");
        }
        return out;
    }
    
    protected Map<String,String> initDefaultAliasProperties(){
        Map<String,String> map = new HashMap<String,String>();
        
        map.put("编号", "id");
        map.put("id", "id");
        
        map.put("标题", "name");
        map.put("title", "name");
        map.put("name", "name");
        
        map.put("引导图", "iconUrl");
        map.put("image", "iconUrl");
        map.put("iconUrl", "iconUrl");
        
        map.put("链接地址", "absUrl");
        map.put("url", "absUrl");
        map.put("absUrl", "absUrl");
                
        return map;
    }
    
    protected Map<String,DirectiveOutable> initDefaultPropertyDirectiveOuts(){
        
        Map<String,DirectiveOutable> map = new HashMap<String,DirectiveOutable>();
        
        map.put("id", new DefaultDirectiveOut());
        map.put("name", new LengthDirectiveOut());
        map.put("iconUrl", new DefaultDirectiveOut());
        map.put("absUrl", new DefaultDirectiveOut());
        
        return map;
    }
    
    /**
     * 放入指定的属性标签
     * 
     * <p>标签存在则更新标签</p>
     * 
     * @param property 属性名
     * @param directiveOut 标签输出
     */
    public void putDirective(String property,DirectiveOutable directiveOut){
        Assert.hasText(property);
        Assert.notNull(directiveOut);
        
        putDirective(property, property,directiveOut);
    }
    
    /**
     * 放入指定的属性标签
     * 
     * <p>标签存在则更新标签</p>
     * 
     * @param alias    属性别名
     * @param property 属性名
     * @param directiveOut 标签输出
     */
    public void putDirective(String alias,String property,DirectiveOutable directiveOut){
        Assert.hasText(alias);
        Assert.hasText(property);
        Assert.notNull(directiveOut);
        
        aliasProperties.put(alias, property);    
        aliasProperties.put(property, property);
        propertyDirectiveOuts.put(property, directiveOut);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
	protected boolean getCacheValue(Map params) throws TemplateException {
    	return false;
    }
}

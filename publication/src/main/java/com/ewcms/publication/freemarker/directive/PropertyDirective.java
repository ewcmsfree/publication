/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.freemarker.FreemarkerUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 得到对象属性值
 * 
 * <p>通过设置指定属性名，得到该属性的值</p>
 * 
 * @author wangwei
 */
public class PropertyDirective extends BaseDirective {

    private static Logger logger = LoggerFactory.getLogger(PropertyDirective.class);
    
    private static final String VALUE_PARAM_NAME = "value";
    private static final String NAME_PARAM_NAME = "name";
    private static final String DEFAULT_LOOP_NAME = "o";
    
    protected String valueParam = VALUE_PARAM_NAME;
    protected String nameParam = NAME_PARAM_NAME;
    protected String defaultLoop = DEFAULT_LOOP_NAME;
    
    @Override
    @SuppressWarnings("rawtypes")
	protected TemplateModel[] getLoopVars(Environment env, Map params) throws TemplateException {
    	String property = getPropertyName(env,params);
        Object value = getObjectValue(env, params);
        
        TemplateModel[] loopVars = new TemplateModel[]{};
        try{
        	Object v = loopValue(value,property,env,params);
        	if(EmptyUtil.isNotNull(v)){
                loopVars = new TemplateModel[]{env.getObjectWrapper().wrap(v)};
            }
        }catch (NoSuchMethodException e) {
        	try{
        		outNoSuchMethodException(env,e);        		
        	}catch(IOException ex){
        		//None instance
        	}
        }
        return loopVars;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void executeBody(Environment env, Map params,TemplateModel[] loopVars,
			TemplateDirectiveBody body,Writer writer)throws TemplateException, IOException {

		try {
			String property = getPropertyName(env,params);
	        Object value = getObjectValue(env, params);
			Object v = loopValue(value,property,env,params);
	        if(EmptyUtil.isNotNull(v)){
	            FreemarkerUtil.setVariable(env, defaultLoop, v);
	            body.render(env.getOut());
	            FreemarkerUtil.removeVariable(env, defaultLoop);
	        }
		}catch (NoSuchMethodException e) {
			outNoSuchMethodException(env,e);
        }
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void executeNoneBody(Environment env, Map params,
			TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		
		try {
			String property = getPropertyName(env,params);
	        Object value = getObjectValue(env, params);
		    String outValue = constructOut(value,property,env,params);
            if(EmptyUtil.isNotNull(outValue)){
                Writer out = env.getOut();
                out.write(outValue.toString());
                out.flush();
            }
		}catch (NoSuchMethodException e) {
			outNoSuchMethodException(env,e);
        }
	}
	
	private void outNoSuchMethodException(Environment env,NoSuchMethodException e)
			throws TemplateException, IOException {
		
		  Writer out = env.getOut();
          out.write(e.toString());
          out.flush();
          throw new TemplateModelException(e.getMessage());
	}
    
    /**
     * 标签返回值
     * 
     * @param propertyName
     *            属性名
     * @param objectValue
     *            属性所属对象的值
     * @param evn
     *            freemarker 环境
     * @param params
     *            标签参数集合
     * @return 输出返回值
     * @throws TemplateModelException,NoSuchMethodException
     */
    @SuppressWarnings("rawtypes")
    protected Object loopValue(Object objectValue,String propertyName,Environment env,Map params)throws TemplateException,NoSuchMethodException{
        return getValue(objectValue,propertyName);
    }
    
    /**
     * 构造标签输出内容
     * 
     * @param objectValue
     *            属性所属对象的值
     * @param propertyName
     *            属性名
     * @param evn
     *            freemarker 环境
     * @param params
     *            标签参数集合
     *            
     * @return 标签输出字符串
     * @throws TemplateModelException,NoSuchMethodException
     */
    @SuppressWarnings("rawtypes")
    protected String constructOut(Object objectValue,String propertyName,Environment env, Map params)throws TemplateException,NoSuchMethodException{
        Assert.notNull(objectValue);
        Object value = getValue(objectValue,propertyName);
        return value == null ? null : value.toString();
    }
    
    /**
     * 缺省值对象在freemarker变量中的名称
     * 
     * @return
     */
    protected String defaultValueParamValue(){
        return null;
    }
    
    /**
     * 缺省对象值
     * 
     * @param env 环境变量
     * @param param 参数列表
     * @return 
     * @throws TemplateModelException
     */
    @SuppressWarnings("rawtypes")
    protected Object defaultObjectValue(Environment env, Map params)throws TemplateException{
       return null; 
    }
    
    /**
     * 得到对象的值
     * 
     * @param env
     *            环境
     * @param params
     *            标签参数集合
     * @return
     * @throws TemplateModelException
     */
    @SuppressWarnings("rawtypes")
    protected Object getObjectValue(final Environment env, final Map params)throws TemplateException {

        Object object =  FreemarkerUtil.getBean(params, valueParam);
        if (EmptyUtil.isNotNull(object)) {
            logger.debug("Get value is {}",object);
            return object;
        }

        String name = FreemarkerUtil.getString(params, valueParam);
        logger.debug("Get variable is {} in params",name);
        if(EmptyUtil.isNull(name)){
            name = defaultValueParamValue();
            logger.debug("Get default variable is {} in env",name);
        }
        if (EmptyUtil.isNotNull(name)) {
            logger.debug("Get value param is {}", name);
            object = FreemarkerUtil.getBean(env, name);
            if (EmptyUtil.isNotNull(object)) {
                logger.debug("Get value is {}",object);
                return object;
            }
        }
        
        object = defaultObjectValue(env,params);
        if(object != null){
            return object;
        }
        
        logger.error("\"{}\"  has not value",valueParam);
        throw new TemplateModelException("Object value not exist");
    }
    
    /**
     * 得到属性名
     * 
     * @param params
     *            标签参数集合
     * @return
     * @throws TemplateModelException
     */
    @SuppressWarnings("rawtypes")
    protected String getPropertyName(final Environment env, final Map params)throws TemplateException {
    	
        String property = FreemarkerUtil.getString(params, nameParam);
        if(property == null){
            property = FreemarkerUtil.getString(env, nameParam);
        }
        
        if(EmptyUtil.isNull(property)){
            logger.error("\"name\" parameter must set");
            throw new TemplateModelException("\"name\" parameter must set");
        }
        
        logger.debug("Property name is {}",property);
        return property;
    }
    
    /**
     * 得到对象属性值
     * 
     * @param objectValue
     *                对象
     * @param property 
     *                属性名
     * @return           
     */
    protected Object getValue(Object objectValue,String property)throws NoSuchMethodException{
        try{
            return PropertyUtils.getProperty(objectValue, property);    
        }catch(NoSuchMethodException e){
            throw e;
        }catch(Exception e){
            throw new NoSuchMethodException(e.toString());
        }
    }

    /**
     * 设置值对象参数名
     * 
     * @param name 参数名
     */
    public void setValueParam(String name) {
        valueParam = name;
    }

    /**
     * 设置对象属性参数名
     * 
     * @param name 参数名
     */
    public void setNameParam(String name){
        nameParam = name;
    }
    
    /**
     * 属性值放入freemarker环境中的变量名
     * 
     * @param name 缺省loop名
     */
    public void setDefaultLoop(String name){
        defaultLoop = name;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
	protected boolean getCacheValue(Map params) throws TemplateException {
    	return false;
    }
}

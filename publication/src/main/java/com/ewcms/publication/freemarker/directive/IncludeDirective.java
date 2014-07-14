/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.cache.NoneCache;
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.freemarker.FreemarkerUtil;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 包含标签
 * 
 * @author wangwei
 */
public class IncludeDirective extends BaseDirective {
    private final static Logger logger = LoggerFactory.getLogger(IncludeDirective.class);
    private final static Cacheable<String, String> EMPTY_INCLUDE_CACHE = new NoneCache<String, String>();
    
    private final static String PATH_PARAM_NAME = "path";
    private final static String PARSE_PARAM_NAME = "parse";
    private final static String CHANNEL_PARAM_NAME = "channel";
    private final static String NAME_PARAM_NAME = "name";
    private final static String CHARSET_PARAM_NAME = "charset";
    
    private final ChannelDaoable channelService;
    private final TemplateDaoable templateService;
    
    private String pathParam = PATH_PARAM_NAME;
    private String parseParam = PARSE_PARAM_NAME;
    private String channelParam = CHANNEL_PARAM_NAME;
    private String nameParam = NAME_PARAM_NAME;
    private String charsetParam = CHARSET_PARAM_NAME;
    
    public IncludeDirective(ChannelDaoable channelService,TemplateDaoable templateService){
    	
        this.channelService = channelService;
        this.templateService = templateService;
    }
    
    @Override
    @SuppressWarnings("rawtypes")
	protected TemplateModel[] getLoopVars(Environment env, Map params)
			throws TemplateException {
    	
    	throw new TemplateException("include not loop", env);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void executeNoneBody(Environment env, Map params,
			TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		
		String uniquePath = getTemplateUniquePath(env,params);
	    logger.debug("Include template path is {}",uniquePath);
	    boolean parse = isParse(params);
	    String charset = getCharsetValue(env,params);
        if(!parse){
        	env.include(uniquePath, charset, parse);
        }else{
	        includeCache(env, params, body, uniquePath, charset);	
        }
	}
	
	@SuppressWarnings("rawtypes")
	private void includeCache(Environment env, Map params,
			TemplateDirectiveBody body,String uniquePath,String charset)
			throws TemplateException, IOException {
		
		Integer channelId = this.getCurrentChannel(env).getId();
		String taskId = this.getTaskId(env);
		Cacheable<String,String> includeCache = getInclundeCache(env);
		String key = getKey(channelId, uniquePath, taskId);
		String content = includeCache.get(key);
		if(content == null){
			Writer out = env.getOut();
	    	
			StringWriter writer = new StringWriter(200);
	    	env.setOut(writer);
	    	env.include(uniquePath, charset, true);
	    	writer.flush();
	    	content = writer.getBuffer().toString();
	    	writer.close();
	    	
	    	includeCache.add(key, content);
	    	
	    	env.setOut(out);			
		}
		
		env.getOut().write(content);
		env.getOut().flush();
	}
	
	private String getKey(Integer channelId,String path,String taskId){
		String s = String.format("%d-%s-%s", channelId, path, taskId);
		return DigestUtils.md5Hex(s.getBytes());
	}
	
    /**
     * 从Freemarker得到站点编号
     * 
     * @param env freemarker环境
     * @return
     * @throws TemplateException
     */
    private Integer getSiteIdValue(Environment env)throws TemplateException {
        Site site = (Site) FreemarkerUtil.getBean(env, GlobalVariable.SITE.toString());
        if(EmptyUtil.isNull(site)){
            logger.error("Site is null in freemarker variable");
            throw new TemplateModelException("Site is null in freemarker variable");
        }
        logger.debug("Site is {}",site);
        return site.getId();
    }

    @Override
	@SuppressWarnings("rawtypes")
	protected void executeBody(Environment env, Map params,
			TemplateModel[] loopVars, TemplateDirectiveBody body, Writer writer)
			throws TemplateException, IOException {
    	
    	throw new TemplateException("Include not body", env);
	}
    
    @SuppressWarnings("rawtypes")
    private String getTemplateUniquePath(Environment env, Map params)throws TemplateException{
    	
    	String path = getPathValue(params);
        Integer siteId = getSiteIdValue(env);
        String uniquePath = null;
        if(EmptyUtil.isNotNull(path)){
            uniquePath = getUniqueTemplatePath(siteId,path);
        }else{
            String name = getNameValue(params);
            if(EmptyUtil.isNotNull(name)){
                Integer channelId = getChannelIdValue(env,params,siteId);
                uniquePath = getChannelTemplatePath(siteId,channelId,name);
            }
        }
        
        if(EmptyUtil.isNull(uniquePath)){
            logger.error("Include template path is null");
            throw new TemplateModelException("Include template path is null");
        }
        
        return uniquePath;
    }
    
    /**
     * 得到模板路径
     * 
     * @param params 标签参数
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private String getPathValue(Map params)throws TemplateException {
        String path = FreemarkerUtil.getString(params, pathParam);
        logger.debug("Path is {}",path);
        return path;
    }
    
    /**
     * 得到指定频道编号
     * 
     * @param env freemarker环境
     * @param params 标签参数
     * @param siteId 站点编号
     * @return 频道编号
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private Integer getChannelIdValue(Environment env,Map params,Integer siteId)throws TemplateException{
        Channel channel = (Channel)FreemarkerUtil.getBean(params, channelParam);
        if (EmptyUtil.isNotNull(channel)) {
            logger.debug("Get channel is {}",channel);
            return channel.getId();
        }

        Integer id = FreemarkerUtil.getInteger(params, channelParam);
        if(EmptyUtil.isNotNull(id)){
            logger.debug("Get channel id is {}",id);
            return id;
        }
        
        String path= FreemarkerUtil.getString(params, channelParam);
        logger.debug("Get channel by {}",path);
        if(EmptyUtil.isNotNull(path)){
            channel = channelService.findPublishByUri(siteId, path);
        }
       
        channel = this.getCurrentChannel(env);
        return channel.getId();
    }
    
    /**
     * 得到模板名称
     * 
     * @param params 标签参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private String getNameValue(Map params)throws TemplateException{
        String name = FreemarkerUtil.getString(params, nameParam);
        logger.debug("name is {}",name);
        return name;
    }
    
    /**
     * 得到是否解析模板
     * 
     * @param params 标签参数集合
     * @return
     * @throws TemplateModelException
     */
    @SuppressWarnings("rawtypes")
    private Boolean isParse(Map params) throws TemplateException {
        Boolean enabled = FreemarkerUtil.getBoolean(params, parseParam);
        return EmptyUtil.isNull(enabled) ? Boolean.TRUE : enabled;
    }
    
    /**
     * 得到是否解析模板
     * 
     * @param params 标签参数集合
     * @return
     * @throws TemplateModelException
     */
    @SuppressWarnings("rawtypes")
    private String getCharsetValue(Environment env, Map params) throws TemplateException {
        String charset = FreemarkerUtil.getString(params, charsetParam);
        return EmptyUtil.isNull(charset) ? env.getConfiguration().getDefaultEncoding() : charset;
    }
    
    @SuppressWarnings("unchecked")
	private Cacheable<String, String> getInclundeCache(Environment env)throws TemplateException{
    	Cacheable<String, String> cache = (Cacheable<String, String>)FreemarkerUtil.getBean(env, 
    			GlobalVariable.INCLUDE_CACHE.getVariable());
    	
    	return cache == null ? EMPTY_INCLUDE_CACHE : cache;
    }

    /**
     * 模板唯一路径
     * 
     * <p>站点编号+模板路径</p>
     * 
     * @param siteId 站点编号
     * @param path 模板路径
     * @return
     */
    String getUniqueTemplatePath(Integer siteId, String path) {
        String nPath = StringUtils.removeStart(path, "/");
        String uPath = StringUtils.join(new Object[]{siteId,nPath}, "/");
        logger.debug("Include path is {}",uPath);
        return uPath;
    }
    
    /**
     * 模板唯一路径
     * 
     * @param siteId 站点编号
     * @param path 路径
     * @param name 模板名称
     * @return
     */
    String getChannelTemplatePath(Integer siteId,Integer channelId,String name){
        return templateService.findUniquePath(siteId, channelId, name);
    }
    
    /**
     * 设置模板参数名称
     * 
     * @param pathParam 路径参数名
     */
    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }

    /**
     * 设置解析参数名称
     * 
     * @param parseParam 解析参数名
     */
    public void setParseParam(String parseParam) {
        this.parseParam = parseParam;
    }

    /**
     * 设置频道参数名称
     * 
     * @param channelParam 频道参数名
     */
    public void setChannelParam(String channelParam) {
        this.channelParam = channelParam;
    }

    /**
     * 设置模板参数名称
     * 
     * @param nameParam 模板参数名
     */
    public void setNameParam(String nameParam) {
        this.nameParam = nameParam;
    }
    
    /**
     * 设置模板字符集参数名称
     * 
     * @param nameParam 模板参数名
     */
    public void setCharsetParam(String charsetParam){
    	this.charsetParam = charsetParam;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
	protected boolean getCacheValue(Map params) throws TemplateException {
    	return false;
    }
}

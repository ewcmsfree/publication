/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.dao.ChannelDaoable;
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
 * 生成页面所在位置标签
 * 
 * @author wangwei
 */
public class PositionDirective extends BaseDirective {
    
    private final static Logger logger = LoggerFactory.getLogger(PositionDirective.class);

    private static final String VALUE_PARAM_NAME = "value";
    private static final String NAME_PARAM_NAME = "name";
    private static final String MARK_PARAM_NAME = "mark";
    private static final String DEFAULT_MARK = "&gt;";

    private String valueParam = VALUE_PARAM_NAME;
    private String nameParam = NAME_PARAM_NAME;
    private String markParam = MARK_PARAM_NAME;
    
    private final ChannelDaoable channelDao;
    
    public PositionDirective(ChannelDaoable channelDao){
    	this.channelDao = channelDao;
    }
    
    @SuppressWarnings("rawtypes")
	@Override
	protected TemplateModel[] getLoopVars(Environment env, Map params)throws TemplateException {
    	List<Channel> levels = getChannelLevels(env, params);
    	return new TemplateModel[]{env.getObjectWrapper().wrap(levels)};
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void executeBody(Environment env, Map params,TemplateModel[] loopVars, TemplateDirectiveBody body, Writer writer)
			throws TemplateException, IOException {
    	List<Channel> levels = getChannelLevels(env, params);
        String name = getNameValue(params);
        String mark = getMarkValue(params);
        for (Iterator<Channel> iterator = levels.iterator(); iterator.hasNext();) {
            Channel c = iterator.next();
            FreemarkerUtil.setVariable(env, name, c);
            body.render(writer);
            if (iterator.hasNext()) {
                writer.write(mark);
            }
            writer.flush();
            FreemarkerUtil.removeVariable(env, name);
        }
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void executeNoneBody(Environment env, Map params,	TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		
		List<Channel> levels = getChannelLevels(env, params);
		StringBuilder builder = new StringBuilder();
        String mark = getMarkValue(params);
        for (Iterator<Channel> iterator = levels.iterator(); iterator.hasNext();) {
            Channel channel = iterator.next();
            if (channel.getAbsUrl() == null || channel.getAbsUrl().trim().length() == 0){
            	builder.append("<a href=\"/").append("\">");
            }else{
            	builder.append("<a href=\"").append(channel.getAbsUrl()).append("\">");
            }
            builder.append(channel.getName());
            builder.append("</a>");
            if (iterator.hasNext()) {
                builder.append(mark);
            }
        }
        Writer writer = env.getOut();
        writer.write(builder.toString());
        writer.flush();
	}

    /**
     * 得到当前频道
     * 
     * @param env
     *            Freemarker 环境对象
     * @param params
     *            标签参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private Channel getChannel(Environment env,Map params) throws TemplateException {
        Channel channel = (Channel)FreemarkerUtil.getBean(params, valueParam);
        if (EmptyUtil.isNotNull(channel)) {
            logger.debug("Get channel is {}",channel);
            return channel;
        }

        String name = FreemarkerUtil.getString(params, valueParam);
        logger.debug("Get variable is {} in params",name);
        if(EmptyUtil.isNull(name)){
            name = FreemarkerUtil.getString(env, valueParam);
            logger.debug("Get variable is {} in env",name);
        }
        if(EmptyUtil.isNull(name)){
            name = GlobalVariable.CHANNEL.toString();
        }
        logger.debug("Get value param is {}", name);
        
        channel = (Channel)FreemarkerUtil.getBean(env, name);
        
        if (EmptyUtil.isNull(channel)) {
            logger.error("Channel is null in freemarker variable");
            throw new TemplateModelException("Channel is null in freemarker variable");
        }
        return channel;
    }

    /**
     * 得到各级频道
     * 
     * @param channel
     *            当前频道
     * @return
     */
    @SuppressWarnings("rawtypes")
	private List<Channel> getChannelLevels(Environment env, Map params) throws TemplateException {
    	Site site = getCurrentSite(env);
    	Channel channel = getChannel(env, params);
    	
        List<Channel> levels = new ArrayList<Channel>();
        levels.add(channel);
        Integer parentId = channel.getParentId();
        for(int i = 0 ; i < 100 ; i++){
        	if(parentId != null){
        		Channel p = channelDao.findPublishOne(site.getId(), parentId);
        		if(p == null) break;
            	levels.add(0, p);
            	parentId = p.getParentId();
        	}
        }
        
        return levels;
    }

    /**
     * 得到变量名参数值
     * 
     * @param params
     *            标签参数
     * @return
     * @throws TemplateModelException
     */
    @SuppressWarnings("rawtypes")
    private String getNameValue(Map params) throws TemplateException {
        String value = FreemarkerUtil.getString(params, nameParam);
        logger.debug("Get name is {}",value);
        if (EmptyUtil.isNull(value)) {
            return GlobalVariable.CHANNEL.toString();
        }
        return value;
    }

    /**
     * 得到分割符
     * 
     * @param params
     *            标签参数
     * @return 分割符
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private String getMarkValue(Map params) throws TemplateException {
        String value = FreemarkerUtil.getString(params, markParam);
        logger.debug("Get mark is {}",value);
        if (EmptyUtil.isNull(value)) {
            return DEFAULT_MARK;
        }
        return value;
    }
    
    @SuppressWarnings("rawtypes")
	@Override
   	protected String cacheKey(Environment env, Map params)throws TemplateException{
    	String taskId = getTaskId(env);
		Site site = getCurrentSite(env);
		Channel channel = getChannel(env, params);
   		return String.format("fm-position-directive-%s-%d-%d", 
   				taskId,site.getId(),channel.getId());
   	}

    /**
     * 设置设置频道值参数名
     * 
     * @param valueParam
     */
    public void setValueParam(String valueParam){
        this.valueParam = valueParam;
    }
    
    /**
     * 设置频道变量名的参数名
     * 
     * @param nameParam
     *            参数名
     */
    public void setNameParam(String nameParam) {
        this.nameParam = nameParam;
    }

    /**
     * 设置频道间分割符参数名
     * 
     * @param markParam
     *            参数名
     */
    public void setMarkParam(String markParam) {
        this.markParam = markParam;
    }
}

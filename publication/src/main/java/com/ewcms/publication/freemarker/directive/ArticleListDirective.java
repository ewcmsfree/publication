/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.freemarker.FreemarkerUtil;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.module.ArticleInfo;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 文章列表标签
 * <br>
 * 通过设置频道id或地址，可以得到相应的文章记录。
 * 
 * @author wangwei
 */
public class ArticleListDirective extends ForeachDirective {
    private static final Logger logger = LoggerFactory.getLogger(ArticleListDirective.class);
    
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Boolean DEFAULT_ABSOLUTE = Boolean.FALSE;
    
    private static final String CHANNEL_PARAM_NAME = "channel";
    private static final String ROW_PARAM_NAME = "row";
    private static final String NAME_PARAM_NAME = "name";
    private static final String TOP_PARAM_NAME = "top";
    private static final String ABSOLUTE_PARAM_NAME = "absolute";
    
    private final ArticleDaoable articleService;
    private final ChannelDaoable channelService;
    
    private String channelParam = CHANNEL_PARAM_NAME;
    private String rowParam = ROW_PARAM_NAME;
    private String nameParam = NAME_PARAM_NAME;
    private String topParam = TOP_PARAM_NAME;
    private String absoluteParam = ABSOLUTE_PARAM_NAME;
    
    public ArticleListDirective(ChannelDaoable channelService,ArticleDaoable articleService){
        this.articleService  = articleService;
        this.channelService = channelService;
    }
    
	@Override
	@SuppressWarnings("rawtypes")
	protected List<Object> getValues(Environment env, Map params)throws TemplateException {
        Channel channel = getChannel(env, params);
        Channel currentChannel = getCurrentChannel(env);
        channel = channel == null ? currentChannel : channel;
        boolean current = channel.equals(currentChannel);
        
        List<ArticleInfo> articles = null;
        if(channel == null || current){
        	articles = getArticleListValue(params);
        }
        if(articles == null ){
        	articles = findArticles(env,params,channel,current);
        }
        
        List<Object> l =  new ArrayList<Object>();
        l.addAll(articles);
        
        return l;
	}
	
	@SuppressWarnings("rawtypes")
	private List<ArticleInfo> findArticles(Environment env, Map params,Channel channel,boolean isCurrentChannel)throws TemplateException{
		
        Integer row = getRowValue(params,channel,env);
        Integer page = getPageValue(env ,isCurrentChannel ,getAbsoluteValue(params));
        Boolean top = getTopValue(params);

	    return  articleService.findPublish(channel.getId(), page, row,top);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void setVariable(Environment env, Map params, Object value)throws TemplateException {
		 String name = getNameValue(params);	
		 FreemarkerUtil.setVariable(env, name, value);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void removeVariable(Environment env, Map params)throws TemplateException {
		String name = getNameValue(params);	
		FreemarkerUtil.removeVariable(env, name);		
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void executeNoneBody(Environment env, Map params,
			TemplateModel[] loopVars, TemplateDirectiveBody body)throws TemplateException, IOException {
		
		//TODO 添加默认显示格式
		logger.warn("Body and loopVars are all null");
	}
    
    /**
     * 值得到频道
     * 
     * @param env
     *          Freemarker环境
     * @param params
     *          标签参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private Channel getChannel(Environment env,Map params)throws TemplateException{
        final String name = channelParam;
        
        Channel channel =null;
        Integer id = FreemarkerUtil.getInteger(params, name);
        Site site = getCurrentSite(env);
        if (EmptyUtil.isNotNull(id)) {
            logger.debug("Channel's id is {}",id);
            channel = channelService.findPublishOne(site.getId(),id);
            if(channel == null){
                logger.error("Channel's id is {},it is not exist.",id);
                throw new TemplateModelException("Channel id is "+ id +",it is not exist");
            }
            return channel;
        }

        String value = FreemarkerUtil.getString(params, name);
        if (EmptyUtil.isStringNotEmpty(value)) {
            logger.debug("Directive {} property is {}",name,value);
            channel = (Channel) FreemarkerUtil.getBean(env, value);
            if (EmptyUtil.isNotNull(channel)) {
                logger.debug("Channel is {}",channel.toString());
                return channel;
            }
            channel = channelService.findPublishByUri(site.getId(), value);
            if(channel == null){
                logger.error("Channel's path or variable is {},it is not exist.",value);
                throw new TemplateModelException("Channel's path or variable is "+value+",it is not exist");
            }
            return channel;
        }
        
        channel = getCurrentChannel(env);
        
        return channel;
    }
   
    /**
     * 得到显示行数
     *
     * @param params 
     *           标签参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private Integer getRowValue(Map params,Channel channel,Environment env) throws TemplateException {
        Integer row = FreemarkerUtil.getInteger(params, rowParam);
        row = (row == null || row < 0) ? 0 : row; 
        if(row == 0){
        	row = channel.getListSize();
        }
        return row; 
    }

    /**
     * 得到Name参数值
     *
     * @param params
     *            标签参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    protected String getNameValue(Map params) throws TemplateException {
    	 String value = FreemarkerUtil.getString(params, nameParam);
         return value == null ? GlobalVariable.ARTICLE.toString() : value;
    }
     
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<ArticleInfo> getArticleListValue(final Map params) throws TemplateException {
        List value =FreemarkerUtil.getSequence(params, GlobalVariable.ARTICLE_LIST.getVariable());
        return (List<ArticleInfo>)value;
    }

    /**
     * 得到Top参数值
     * <br>
     * 显示顶置新闻
     *
     * @param params
     *            标签参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private Boolean getTopValue(Map params) throws TemplateException {
        Boolean top = FreemarkerUtil.getBoolean(params, topParam);
        return top;
    }

    /**
     * 得到是否按照绝对位置查询，忽略页数。
     *<br>
     * 解决在当前频道中只想显示当前频道指定的文章列表，不会随着页面改变而改变。
     * 
     *  @param params
     *            标签参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private Boolean getAbsoluteValue(Map params) throws TemplateException {
        Boolean value = FreemarkerUtil.getBoolean(params, absoluteParam);
        return value == null ? DEFAULT_ABSOLUTE : value;
    }

    /**
     * 得到显示页面数
     *
     * @param env
     *          Freemarker环境
     * @param current 
     *          但前频道
     *  @param absolute 
     *           绝对位置
     * @return
     * @throws TemplateException
     */
    private Integer getPageValue(Environment env,boolean current,boolean absolute) throws TemplateException {
        if(!current || absolute){
            return DEFAULT_PAGE_NUMBER;
        }
        Integer number = FreemarkerUtil.getInteger(env, GlobalVariable.PAGE_NUMBER.getVariable());
        logger.debug("Page is {}",number);
        return number == null ? DEFAULT_PAGE_NUMBER : number;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
	protected boolean getCacheValue(Map params) throws TemplateException {
    	return false;
    }
    
    /**
     * 设置标签频道参数名
     * 
     * @param paramName 参数名
     */
    public void setChannelParam(String paramName) {
        this.channelParam = paramName;
    }

    /**
     * 设置标签显示行参数名
     * 
     * @param paramName 参数名
     */
    public void setRowParam(String paramName) {
        this.rowParam = paramName;
    }

    /**
     * 设置标签输出变量名
     * 
     * @param paramName 参数名称
     */
    public void setNameParam(String paramName) {
        this.nameParam = paramName;
    }

    /**
     * 设置标签顶置显示参数名
     * 
     * @param paramName
     */
    public void setTopParam(String paramName) {
        this.topParam = paramName;
    }

    /**
     * 设置标签绝对位置参数名
     * 
     * @param paramName
     */
    public void setAbsoluteParam(String absoluteParam) {
        this.absoluteParam = absoluteParam;
    }
}

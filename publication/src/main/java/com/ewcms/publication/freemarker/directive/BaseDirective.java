package com.ewcms.publication.freemarker.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.cache.NoneCache;
import com.ewcms.publication.freemarker.FreemarkerUtil;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 实现
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public abstract class BaseDirective implements TemplateDirectiveModel {
	private static final Logger logger = LoggerFactory.getLogger(BaseDirective.class);
	
	private final static Cacheable<String, String> EMPTY_INCLUDE_CACHE = new NoneCache<String, String>();
	private static final String CACHE_PARAM_NAME = "cache";
    private String cacheParam = CACHE_PARAM_NAME;

	@Override
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		
		boolean isCache = this.getCacheValue(params);
		
		if(EmptyUtil.isNull(body)){
			executeNoneBody(env,params,loopVars,body);
			return ;
		}
		
		if(isCache){
			Cacheable<String,String> cache = getInclundeCache(env);
			String key = cacheKey(env, params);
			if(cache.exist(key)){
				String c = cache.get(key);
				env.getOut().write(c == null ? "" : c);
				return ;
			}
			StringWriter writer = new StringWriter(200);
			try{
				if(EmptyUtil.isArrayNotEmpty(loopVars)){
					executeLoopVars(env,params,loopVars,body,writer);
				}else{
					executeBody(env,params,loopVars,body,writer);
				}	
				String c = writer.getBuffer().toString();
				env.getOut().write(c);
				cache.add(key, c);	
			}finally{
				writer.close();
			}
		}else{
			Writer writer = env.getOut();
			if(EmptyUtil.isArrayNotEmpty(loopVars)){
				executeLoopVars(env,params,loopVars,body,writer);
			}else{
				executeBody(env,params,loopVars,body,writer);
			}	
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void executeLoopVars(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body, Writer writer)throws TemplateException, IOException{
		
		TemplateModel[] vars = getLoopVars(env,params);
		for(int i = 0 ; i < vars.length ; i++){
			loopVars[i] = vars[i];
		}
		body.render(writer);
	}
	
	@SuppressWarnings("rawtypes")
	protected abstract TemplateModel[] getLoopVars(Environment env, Map params)throws TemplateException;
	
	@SuppressWarnings("rawtypes")
	protected abstract void executeBody(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body, Writer writer)throws TemplateException, IOException;
	
	@SuppressWarnings("rawtypes")
	protected abstract void executeNoneBody(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)throws TemplateException, IOException;
	
	@SuppressWarnings("rawtypes")
	protected String cacheKey(Environment env, Map params)throws TemplateException{
		return UUID.randomUUID().toString();
	}
	
	 /**
     * 得到当前发布的站点
     * 
     * @param env Freemarker环境
     * @return 得到当前站点
     * @throws TemplateException
     */
    protected Site getCurrentSite(final Environment env) throws TemplateException {
        Site site = (Site) FreemarkerUtil.getBean(env, GlobalVariable.SITE.getVariable());
      
        if(EmptyUtil.isNull(site)){
            logger.error("Publication site is null in freemarker variable");
            throw new TemplateModelException("Publication site is null in freemarker variable");
        }
        
        logger.debug("Site is {}",site);
        return site;
    }
    
    /**
     * 得到当前发布的频道
     * 
     * @param env Freemarker环境
     * @return 当前发布的站点
     * @throws TemplateException
     */
    protected Channel getCurrentChannel(final Environment env) throws TemplateException {
    	Channel channel = (Channel)FreemarkerUtil.getBean(env, GlobalVariable.CHANNEL.getVariable());
          
        if (EmptyUtil.isNull(channel)) {
            logger.error("Publication channel is null in freemarker variable");
            throw new TemplateModelException("Publication channel is null in freemarker variable");
        }
          
        return channel;
    }
    
    /**
     * 得到发布任务编号
     * 
     * @param env Freemarker环境
     * @return 当前发布任务编号
     * @throws TemplateException
     */
    protected String getTaskId(final Environment env) throws TemplateException{
    	String taskId = FreemarkerUtil.getString(env, GlobalVariable.TASK_ID.getVariable());
    	
    	if(EmptyUtil.isStringEmpty(taskId)){
    		logger.error("Publication task id is null in freemarker variable");
            throw new TemplateModelException("Publication task id is null in freemarker variable");
    	}
    	
    	return taskId;
    }
    
    @SuppressWarnings("rawtypes")
    protected boolean getCacheValue(Map params) throws TemplateException {
    	Boolean value = FreemarkerUtil.getBoolean(params, cacheParam);
    	return value == null ? true : value;
    }
    
    @SuppressWarnings("unchecked")
   	private Cacheable<String, String> getInclundeCache(Environment env)throws TemplateException{
       	Cacheable<String, String> cache = (Cacheable<String, String>)FreemarkerUtil.getBean(
       			env, GlobalVariable.INCLUDE_CACHE.getVariable());
       	
       	return cache == null ? EMPTY_INCLUDE_CACHE : cache;
    }
    
    /**
     * 设置是否使用缓冲
     * 
     * @param paramName 参数名称
     */
    public void setCacheParam(String paramName){
    	this.cacheParam = paramName;
    }
}
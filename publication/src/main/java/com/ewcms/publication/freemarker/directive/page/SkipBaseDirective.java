/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ewcms.publication.freemarker.FreemarkerUtil;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.BaseDirective;
import com.ewcms.publication.module.Article;
import com.ewcms.publication.uri.UriRuleable;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * 页面跳转基础标签类
 * <br>
 * 主要为标签得到通用参数值如：页数、总页数。
 * 
 * @author  <a href="hhywangwei@gmail.com">王伟</a>
 *
 */
public abstract class SkipBaseDirective extends BaseDirective {

    /**
     * 得到当前页数
     * 
     * @param env 
     *         Freemarker 环境变量
     * @return
     * @throws TemplateModelException
     */
    protected Integer getPageNumberValue(Environment env)throws TemplateException {
        return PageUtil.getPageNumberValue(env);
    }

    /**
     * 得到总页数
     * 
     * @param env 
     *         Freemarker 环境变量
     * @return
     * @throws TemplateModelException
     */
    protected Integer getPageCountValue(Environment env)throws TemplateException {
        return PageUtil.getPageCountValue(env);
    }

    /**
     * 得到Uri生成那个规则
     * 
     * @param env
     * @return
     * @throws TemplateException
     */
    protected UriRuleable getUriRule(Environment env)throws TemplateException{
        return (UriRuleable)FreemarkerUtil.getBean(env, GlobalVariable.URI_RULE.toString());
    }
    
    /**
     * 默认输出
     * 
     * @param pages 页面输出对象集合
     * 
     * @return
     */
    protected String constructOut(List<PageOut>  pages){
       StringBuilder builder = new StringBuilder();
       for(PageOut page : pages){
           if(page.isActive()){
               builder.append("<a href='").
                       append(page.getUrl()).append("'>").
                       append(page.getLabel()).
                       append("</a>").
                       append("&nbsp;");
           }else{
               builder.append(page.getLabel()).
                       append("&nbsp;");
           }
       }
       return builder.toString();
    }
    
    protected Map<String,Object> getUriParams(Environment env) throws TemplateException{
    	Map<String,Object> params = new HashMap<String,Object>();
    	
    	params.put(GlobalVariable.SITE.getVariable(), getCurrentSite(env));
    	params.put(GlobalVariable.CHANNEL.getVariable(), getCurrentChannel(env));
    	Article a =(Article)FreemarkerUtil.getBean(env, GlobalVariable.ARTICLE.getVariable());
    	if(a != null){
    		params.put(GlobalVariable.ARTICLE.getVariable(), FreemarkerUtil.getBean(env, GlobalVariable.ARTICLE.getVariable()));	
    	}
    	return params;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
	protected boolean getCacheValue(Map params) throws TemplateException {
    	return false;
    }
}

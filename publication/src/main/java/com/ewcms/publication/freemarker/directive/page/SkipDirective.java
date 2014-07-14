/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.PublishException;
import com.ewcms.publication.freemarker.FreemarkerUtil;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.page.skip.FirstSkip;
import com.ewcms.publication.freemarker.directive.page.skip.LastSkip;
import com.ewcms.publication.freemarker.directive.page.skip.NextSkip;
import com.ewcms.publication.freemarker.directive.page.skip.Skipable;
import com.ewcms.publication.freemarker.directive.page.skip.PreviousSkip;
import com.ewcms.publication.uri.UriRuleable;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 页面跳转标签
 *
 * @author  <a href="hhywangwei@gmail.com">王伟</a>
 */
public class SkipDirective extends SkipBaseDirective{
    private static final Logger logger = LoggerFactory.getLogger(SkipDirective.class);
    
    private static final Map<String,Skipable> mapSkips = initTypeMapSkips();
    private static final String TYPE_PARAM_NAME = "type";
    private static final String LABEL_PARAM_NAME = "label";
    
    private String typeParam = TYPE_PARAM_NAME;
    private String labelParam = LABEL_PARAM_NAME;
    
    @SuppressWarnings("rawtypes")
	@Override
	protected TemplateModel[] getLoopVars(Environment env, Map params)throws TemplateException {
		PageOut pageOut = getPageOut(env, params);
		return new TemplateModel[]{env.getObjectWrapper().wrap(pageOut)};
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void executeBody(Environment env, Map params,	TemplateModel[] loopVars,
			TemplateDirectiveBody body, Writer writer)throws TemplateException, IOException {
		if(isOnlyOne(env))	return ;
		PageOut pageOut = getPageOut(env, params);
        FreemarkerUtil.setVariable(env, GlobalVariable.PAGE_OUT.getVariable(),pageOut);
        body.render(writer);
        FreemarkerUtil.removeVariable(env, GlobalVariable.PAGE_OUT.getVariable());
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void executeNoneBody(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)throws TemplateException, IOException {
		if(isOnlyOne(env))	return ;
	     PageOut pageOut = getPageOut(env, params);
         String outValue = constructOut(Arrays.asList(pageOut));
         if(EmptyUtil.isNotNull(outValue)){
             Writer out = env.getOut();
             out.write(outValue.toString());
             out.flush();
         }
	}
	
	@SuppressWarnings("rawtypes")
	private PageOut getPageOut(Environment env, Map params)throws TemplateException{
		 Integer pageNumber = getPageNumberValue(env);
         logger.debug("Page number is {}",pageNumber);
         Integer pageCount = getPageCountValue(env);
         logger.debug("Page count is {}",pageCount);
         String label = getLabelValue(params);
         logger.debug("Label is {}",label);
         
         Skipable skip = getSkipPage(params);
         UriRuleable rule = getUriRule(env);
         Map<String,Object> uriParams = getUriParams(env);
         try {
			return skip.skip(pageCount, pageNumber, label, rule,uriParams);
		} catch (PublishException e) {
			throw new TemplateModelException("PageOut create is error " + e.getMessage(),e);
		}
	}
	
    @SuppressWarnings("rawtypes")
    private String getTypeValue(Map params) throws TemplateException {
        String value = FreemarkerUtil.getString(params, typeParam);
        if (EmptyUtil.isNull(value)) {
            throw new TemplateModelException("Type must setting.");
        }
        return value;
    }

    @SuppressWarnings("rawtypes")
    private String getLabelValue(Map params) throws TemplateException {
        return FreemarkerUtil.getString(params, labelParam);
    }
    
    @SuppressWarnings("rawtypes")
	Skipable getSkipPage(Map params)throws TemplateException{
    	String type = getTypeValue(params);
        Skipable skipPage = mapSkips.get(type);
        if(EmptyUtil.isNull(skipPage)){
            logger.error("Skip page has not {} of types",type);
            throw new TemplateModelException("Skip page has not " + type + "of types.");
        }
        return skipPage;
    }
    
    private boolean isOnlyOne(Environment env)throws TemplateException {
    	return getPageCountValue(env) == 1;
    }
    
    static Map<String,Skipable> initTypeMapSkips(){
        Map<String,Skipable> map = new HashMap<String,Skipable>();
        
        map.put("f",new FirstSkip());
        map.put("first", new FirstSkip());
        map.put("首", new FirstSkip());
        map.put("首页", new FirstSkip());
        
        map.put("p", new PreviousSkip());
        map.put("prev", new PreviousSkip());
        map.put("上", new PreviousSkip());
        map.put("上一页", new PreviousSkip());
        
        map.put("n", new NextSkip());
        map.put("next", new NextSkip());
        map.put("下", new NextSkip());
        map.put("下一页", new NextSkip());
        
        map.put("l", new LastSkip());
        map.put("last", new LastSkip());
        map.put("末", new LastSkip());
        map.put("末页", new LastSkip());
        
        return map;
    }
}

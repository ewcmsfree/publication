/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive.page;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.common.lang.EmptyUtil;
import com.ewcms.publication.PublishException;
import com.ewcms.publication.freemarker.FreemarkerUtil;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.uri.UriRuleable;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 跳转页数标签
 * 
 * @author  <a href="hhywangwei@gmail.com">王伟</a>
 */
public class NumberPageDirective extends SkipBaseDirective {
    private static final Logger logger = LoggerFactory.getLogger(NumberPageDirective.class);
    
    private static final int DEFAULT_MAX_LENGTH = 5;
    private static final String DEFAULT_LABEL = "..";
    
    private static final String MAXLENGTH_PARAM_NAME = "max";
    private static final String LABEL_PARAM_NAME = "label";

    private String maxParam = MAXLENGTH_PARAM_NAME;
    private String labelParam = LABEL_PARAM_NAME;

	@SuppressWarnings("rawtypes")
	@Override
	protected TemplateModel[] getLoopVars(Environment env, Map params)throws TemplateException {
		 List<PageOut> pages = getPageOuts(env, params);
		 return pages.size() == 1 ? new TemplateModel[]{env.getObjectWrapper().wrap(pages.get(0))} :
			 new TemplateModel[]{env.getObjectWrapper().wrap(pages)};    
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void executeBody(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body, Writer writer)throws TemplateException, IOException {
		
		if(isOneOnly(env)) return ;
		List<PageOut> pages = getPageOuts(env, params);
        for (PageOut page : pages) {
            FreemarkerUtil.setVariable(env, GlobalVariable.PAGE_OUT.toString(),page);
            body.render(writer);
            FreemarkerUtil.removeVariable(env,GlobalVariable.PAGE_OUT.toString());
        }
        writer.flush();		
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void executeNoneBody(Environment env, Map params,	TemplateModel[] loopVars, TemplateDirectiveBody body)throws TemplateException, IOException {
		if(isOneOnly(env)) return ;
		List<PageOut> pages = getPageOuts(env, params);
		String outValue = constructOut(pages);
        if(EmptyUtil.isNotNull(outValue)){
            Writer out = env.getOut();
            out.write(outValue.toString());
            out.flush();
        }		
	}
	
	private boolean isOneOnly(Environment env)throws TemplateException{
		return getPageCountValue(env) == 1;
	}
	
	/**
     * 显示跳转页的集合
     * 
     * @param rule
     *            uri生成规则
     * @param count
     *            总页数
     * @param number
     *            当前页数
     * @param max
     *            最大显页数
     * @param
     *            省略号标签           
     * @return
     */
    @SuppressWarnings("rawtypes")
	List<PageOut> getPageOuts(Environment env, Map params)throws TemplateException {
    	
    	int count = getPageCountValue(env);
        int number = getPageNumberValue(env);
        int max = getMaxValue(params);
        String label = getLabelValue(params);
        UriRuleable rule = getUriRule(env);
        
        Map<String,Object> uriParams = getUriParams(env);
        return getPageOuts(rule, count, number, max, label, uriParams);
    }
    
    List<PageOut> getPageOuts(UriRuleable rule,int count, int number, int max,String label,Map<String,Object> uriParams)throws TemplateException {
    	   int start = 0;
           int len = 0;
           if ( number < max ) {
               start = 0;
               len = count <= max ? count : max;
           }else if(count < number + max){
               start = count - max;
               len = max;
           } else {
               start = number - max / 2;
               len = max -1 ;
           }
           
           logger.debug("Page number start is {}",start);
           logger.debug("Page number length is  {}",len);
           
           List<PageOut> pageOuts = new ArrayList<PageOut>();
           if (start > 0) {
               pageOuts.add(createMissPage(count,label));
           }
           
           for (int i = 0; i < len; i++) {
               try{
               	int p = start + i;
                   uriParams.put(GlobalVariable.PAGE_NUMBER.getVariable(), p);
               	   String url = rule.uri(uriParams);
                   boolean active = (p != number);
                   pageOuts.add(new PageOut(count, p, url,active));	
               }catch(PublishException e){
               	throw new TemplateModelException("Create page out is error:" + e.getMessage(), e);
               }
           }
           
           if ((start + len) < (count - 1)) {
               pageOuts.add(createMissPage(count,label));
           }

           return pageOuts;
    }
    
    /**
     * 得到最大显示页数目
     * 
     * @param params 
     *            参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private Integer getMaxValue(Map params) throws TemplateException {
        Integer page = FreemarkerUtil.getInteger(params, maxParam);
        page = page == null ? DEFAULT_MAX_LENGTH : page;
        if(page <= 0){
            logger.error("max is {}",page);
            throw new TemplateModelException("max <= 0");
        }
        return page;
    }
    
    /**
     * 得到省略标签显示
     * 
     * @param params
     *            参数集合
     * @return
     * @throws TemplateException
     */
    @SuppressWarnings("rawtypes")
    private String getLabelValue(Map params) throws TemplateException{
        String value = FreemarkerUtil.getString(params, labelParam);
        return value == null ? DEFAULT_LABEL : value;
    }
    
    private PageOut createMissPage(int count,String label) {
        return new PageOut(count, -1, label);
    }

    /**
     * 设置标签最大显示页数目参数名
     * 
     * @param maxParam 参数名
     */
    public void setMaxParam(String maxParam) {
        this.maxParam = maxParam;
    }

    /**
     * 设置标签省略号参数名
     * 
     * @param labelParam 参数名
     */
    public void setLabelParam(String labelParam) {
        this.labelParam = labelParam;
    }
}

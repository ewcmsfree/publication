/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.freemarker.directive.ArticleDirective;
import com.ewcms.publication.freemarker.directive.ArticleListDirective;
import com.ewcms.publication.freemarker.directive.ChannelDirective;
import com.ewcms.publication.freemarker.directive.ChannelListDirective;
import com.ewcms.publication.freemarker.directive.IncludeDirective;
import com.ewcms.publication.freemarker.directive.IndexDirective;
import com.ewcms.publication.freemarker.directive.PositionDirective;
import com.ewcms.publication.freemarker.directive.page.PageOutDirective;
import com.ewcms.publication.freemarker.directive.page.SkipDirective;
import com.ewcms.publication.freemarker.directive.page.NumberPageDirective;
import com.ewcms.publication.freemarker.loader.DatabaseTemplateLoader;

import freemarker.cache.CacheStorage;
import freemarker.cache.MruCacheStorage;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;

/**
 * Freemarker配置定义，设置DatabaseTemplateLoader和自定义指令。
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
@Service
public class FreemarkerConfigurationFactory implements FactoryBean<Configuration>, InitializingBean{

	@Autowired
	private ChannelDaoable channelService;
	@Autowired
	private ArticleDaoable articleService;
	@Autowired
	private TemplateDaoable templateService;

	private TemplateLoader[] templateLoaders;
	
	private Properties freemarkerSettings;
	private Map<String, Object> freemarkerVariables;
	private String defaultEncoding = "UTF-8";
	private Configuration configuration;

	private CacheStorage cacheStorage = new MruCacheStorage(30, 500);
	private boolean localizedLookup = false;
	private int delay = 30 * 24 * 60 * 60;

	/**
	 * 初始化模板加载
	 */
	private TemplateLoader[] initTemplateLoader() {
		return new TemplateLoader[]{new DatabaseTemplateLoader(templateService)};
	}

	/**
	 * 初始定制指令
	 */
	private Map<String, Object> initFreemarkerVariables() {

		Map<String, Object> variables = new HashMap<String,Object>();
		
		variables.put("article", new ArticleDirective(articleService));
		variables.put("channel", new ChannelDirective());
		variables.put("page", new PageOutDirective());
		variables.put("page_skip", new SkipDirective());
		variables.put("page_number", new NumberPageDirective());
		variables.put("article_list", new ArticleListDirective(channelService, articleService));
		variables.put("channel_list", new ChannelListDirective(channelService));
		variables.put("position", new PositionDirective(channelService));
		variables.put("index", new IndexDirective());
		variables.put("include", new IncludeDirective(channelService,templateService));
		
		return variables;
	}

	public Configuration createConfiguration() throws IOException, TemplateException {
		Configuration config = new Configuration();
		Properties props = new Properties();		
		
		if(freemarkerSettings != null){
			props.putAll(freemarkerSettings);
		}
		
		config.setSettings(props);
		config.setTemplateUpdateDelay(delay);
		config.setLocalizedLookup(localizedLookup);
		config.setCacheStorage(cacheStorage);
		config.setDefaultEncoding(defaultEncoding);
		if(freemarkerVariables == null){
			freemarkerVariables = initFreemarkerVariables();
		}
		config.setAllSharedVariables(new SimpleHash(freemarkerVariables,config.getObjectWrapper()));
		if(templateLoaders == null){
			templateLoaders = initTemplateLoader();
		}
		config.setTemplateLoader(new MultiTemplateLoader(templateLoaders));

		return config;
	}

	public void setTemplateLoaders(TemplateLoader[] templateLoaders) {
		this.templateLoaders = templateLoaders;
	}

	public void setCacheStorage(CacheStorage cacheStorage) {
		this.cacheStorage = cacheStorage;
	}

	public void setLocalizedLookup(boolean localizedLookup) {
		this.localizedLookup = localizedLookup;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void setDefaultEncoding(String defaultEncoding){
		this.defaultEncoding = defaultEncoding;
	}
	
	public void setFreemarkerSettings(Properties props){
		this.freemarkerSettings = props;
	}
	
	public void setFreemarkerVariables(Map<String, Object> variables) {
		this.freemarkerVariables = variables;
	}

	public void setTemplateService(TemplateDaoable service) {
		this.templateService = service;
	}

	public void setChannelService(ChannelDaoable channelService) {
		this.channelService = channelService;
	}

	public void setArticleService(ArticleDaoable articleService) {
		this.articleService = articleService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.configuration = createConfiguration();
	}

	@Override
	public Configuration getObject() throws Exception {
		return this.configuration;
	}

	@Override
	public Class<?> getObjectType() {
		return Configuration.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

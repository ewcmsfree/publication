/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.generator;

import java.util.Arrays;
import java.util.List;

import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.generator.FreemarkerGenerator;
import com.ewcms.publication.generator.Generatorable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.uri.UriRuleable;
import com.ewcms.publication.uri.UriRules;

import freemarker.template.Configuration;

/**
 * 发布home页面任务
 * 
 * @author <a href="hhywangwei@gmail.com">wangwei</a>
 */
public class HomeTask extends TemplateTaskBase{
	
	public HomeTask(Site site, Channel channel, Configuration cfg,  String tempRoot, String templatePath, 
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao) {
		
		this(null, false, site, channel, cfg, tempRoot, templatePath, resourceDao, sourceDao);
	}
	
	public HomeTask(String parentId, Site site, Channel channel, Configuration cfg,  String tempRoot, String templatePath, 
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao) {
		
		this(parentId, true, site, channel, cfg, tempRoot, templatePath, resourceDao, sourceDao);
	}
	
	protected HomeTask(String parentId, boolean child, Site site, Channel channel, Configuration cfg,  String tempRoot, String templatePath, 
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao) {
		
		super(parentId, child, site, channel, cfg, tempRoot, true, templatePath, resourceDao, sourceDao);
		setUriRule( UriRules.newHome());
	}

	@Override
	protected String newKey() {
		StringBuilder builder = new StringBuilder(100);
		
    	builder.append("HomeTask[");
    	builder.append("channel=");
   		builder.append(channel.getId());
    	builder.append("]");
    	
		return builder.toString();
	}
	
	@Override
	protected List<Generatorable> buildGenerator( int count, int index, int batchSize,String templatePath){
		Generatorable gen = new FreemarkerGenerator.
				Builder(id, site, channel, cfg, templatePath).
				setUriRule(uriRule).
				setIncludeCache(cache).
				build();
		
		return Arrays.asList(gen);
	}
	
	@Override
	public HomeTask setCache(Cacheable<String,String> cache){
		super.setCache(cache);
		return this;
	}
	
	@Override
	public HomeTask setUriRule(UriRuleable uriRule){
		super.setUriRule(uriRule);
	    return this;
	}
	
	@Override
	public TaskType getTaskType() {
		return TaskType.HOME;
	}

	@Override
	public String getRemark() {
		return String.format("(%d)%s--HOME网页发布",	channel.getId() , channel.getName());
	}
	
	@Override
	protected int totalCount() {
		return 1;
	}
}

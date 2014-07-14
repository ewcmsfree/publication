/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.generator;
 
import java.util.ArrayList;
import java.util.List;

import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.dao.ArticleDaoable;
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
 * 发布List页面任务
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class ListTask extends TemplateTaskBase{
	private final ArticleDaoable articleDao;

	public ListTask(Site site,Channel channel, Configuration cfg, String tempRoot, String templatePath,
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ArticleDaoable articleDao) {
		
		this(null, false, site, channel, cfg, tempRoot, templatePath, resourceDao, sourceDao, articleDao);
	}
	
	public ListTask(String parentId, Site site,Channel channel, Configuration cfg, String tempRoot, String templatePath,
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ArticleDaoable articleDao) {
		
		this(parentId, true, site, channel, cfg, tempRoot, templatePath, resourceDao, sourceDao, articleDao);
	}
	
	protected ListTask(String parentId, boolean child, Site site,Channel channel, Configuration cfg, String tempRoot, String templatePath,
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ArticleDaoable articleDao) {
		
		super(parentId, child, site, channel, cfg, tempRoot, true, templatePath, resourceDao, sourceDao);
		this.articleDao = articleDao;
		setUriRule(UriRules.newList());
	}
	
	@Override
	protected String newKey() {
        StringBuilder builder = new StringBuilder();
		
		builder.append("ListTaks[");
		builder.append(channel.getId());
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.LIST;
	}

	@Override
	protected List<Generatorable> buildGenerator(int count, int index,int batchSize, String templatePath) {
		int from = index * batchSize;
		int to = from + batchSize;
		to = to > count ? count : to;
		List<Generatorable> generators = new ArrayList<Generatorable>();
		for(int i = from ; i < to ; i++){
			Generatorable generator =new FreemarkerGenerator
					.Builder(id, site, channel, cfg, templatePath)
			        .setPageCount(count).setPageNumber(i)
			        .setUriRule(uriRule).setIncludeCache(cache)
			        .build();
			generators.add(generator);
		}
		
		return generators;
	}

	@Override
	protected int totalCount() {
		int count = articleDao.findPublishCount(channel.getId());
		int rows = channel.getListSize();
		return ((rows -1 + count) / rows);
	}
	
	@Override
	public ListTask setCache(Cacheable<String,String> cache){
		super.setCache(cache);
		return this;
	}
	
	@Override
	public ListTask setUriRule(UriRuleable uriRule){
		super.setUriRule(uriRule);
	    return this;
	}
	
	@Override
	public String getRemark() {
		return String.format("(%d)%s--LIST网页发布",	channel.getId() , channel.getName());
	}
}

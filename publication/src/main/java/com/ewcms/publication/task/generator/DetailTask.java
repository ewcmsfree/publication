/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.generator.FreemarkerGenerator;
import com.ewcms.publication.generator.Generatorable;
import com.ewcms.publication.module.Article;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.uri.UriRuleable;
import com.ewcms.publication.uri.UriRules;

import freemarker.template.Configuration;

/**
 * 发布Detail（文章）页面任务
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class DetailTask extends TemplateTaskBase{
	
	private final List<Long> ids;
	private final ArticleDaoable articleDao;
	
	private List<Article> articles;
    
    public DetailTask(Site site, Channel channel , Configuration cfg, String tempRoot, String templatePath, List<Long> ids, 
    		ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ArticleDaoable articleDao){
    	
    	super(null, false, site, channel, cfg, tempRoot, true, templatePath, resourceDao, sourceDao);
    	this.ids = ids;
    	this.articleDao = articleDao;
    	setUriRule(UriRules.newDetail());
    }
    
    @Override
	protected String newKey() {
		Collections.sort(ids);
    	
		StringBuilder builder = new StringBuilder(128);
    	builder.append("DetailTask[");
    	builder.append("ids = ");
    	for(Long id : ids){
    		builder.append(id).append("-");
    	}
    	builder.append("]");
    	
		return builder.toString();
	}
    
    @Override
	protected List<Generatorable> buildGenerator(int count, int index,	int batchSize, String templatePath) {
		int from = index * batchSize;
		int to = from + batchSize;
		to = to < count ? to : count;
		List<Article> list = articles.subList(from, to);
        List<Generatorable> generators = new ArrayList<Generatorable>(list.size());
		for(Article a : list){
			Generatorable generator = new FreemarkerGenerator.
					Builder(id, site, channel, cfg, templatePath).setArticle(a).
					setPageCount(a.getTotalPage()).setPageNumber(a.getPage()).
					setUriRule(uriRule).
			        setMarkId(a.getId()).
			        build();
			generators.add(generator);
		}
		return generators;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.DETAIL;
	}

	@Override
	public String getRemark() {
		return String.format("(%d)%s--文章发布%s",
				channel.getId() , channel.getName(), ids.toString());
	}
	
	@Override
	public DetailTask setUriRule(UriRuleable uriRule){
		super.setUriRule(uriRule);
	    return this;
	}

	@Override
	protected int totalCount() {
		articles = articleDao.findPrePublish(ids);
		return articles.size();
	}
}
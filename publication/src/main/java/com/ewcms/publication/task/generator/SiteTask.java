/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.generator;

import java.util.List;

import com.ewcms.publication.cache.LRUCache;
import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.publish.PublishRunnerable;
import com.ewcms.publication.task.Taskable;

import freemarker.template.Configuration;

/**
 * 发布整个站点任务
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class SiteTask extends GenTaskBase {
	private static final int DEFAULT_CACHE_SIZE = 100;
	
	private final ChannelDaoable channelDao;
	private final TemplateDaoable templateDao;
	private final ArticleDaoable articleDao;
	
	public SiteTask(Site site, Configuration cfg, String tempRoot,
			boolean again, ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao,
			ChannelDaoable channelDao, TemplateDaoable templateDao,	ArticleDaoable articleDao) {
		
		super(site, cfg, tempRoot, again, resourceDao, sourceDao);
		this.channelDao = channelDao;
		this.templateDao = templateDao;
		this.articleDao = articleDao;
	}
	
	@Override
	protected void regSubtask(PublishRunnerable pubRunner){
		Channel root = channelDao.findRoot(site.getId());
		Taskable task = new ChannelTask(id,site, root, cfg, tempRoot, again, true, 
				resourceDao, sourceDao, channelDao, templateDao, articleDao).
				setCache(new LRUCache<String,String>(DEFAULT_CACHE_SIZE));
		
		task.regTask(pubRunner);
	}
	
	@Override
	public TaskType getTaskType() {
		return TaskType.SITE;
	}

	@Override
	public String getRemark() {
		return String.format("(%d)%s--站点网页发布", site.getId(), site.getSiteName());
	}

	@Override
	protected String newKey() {
        StringBuilder builder = new StringBuilder();
		
		builder.append("ChannelTask[");
		builder.append(site.getId());
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	protected void doHandler(List<DepTask> tasks, int count, int index, int batchSize) {
		//not task
	}

	@Override
	protected int totalCount() {
		return 0;
	}
}

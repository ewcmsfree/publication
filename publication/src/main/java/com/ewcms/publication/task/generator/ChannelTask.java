/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.task.generator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ewcms.publication.cache.LRUCache;
import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.module.Template;
import com.ewcms.publication.module.Template.TemplateType;
import com.ewcms.publication.publish.PublishRunnerable;
import com.ewcms.publication.task.Taskable;
import com.ewcms.publication.uri.UriRuleable;
import com.ewcms.publication.uri.UriRules;

import freemarker.template.Configuration;

/**
 * 发布频道任务
 * <br>
 * 发布频道下所有需要发布的资源和任务（如：首页，文章等）
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class ChannelTask extends GenTaskBase{
	
	private final String tempRoot;
	private final boolean pubChild;
	private final boolean again;
	private final ChannelDaoable channelDao;
	private final TemplateDaoable templateDao;
	private final ArticleDaoable articleDao;

	public ChannelTask(Site site, Channel channel,Configuration cfg, String tempRoot, boolean pubChild, boolean again,
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ChannelDaoable channelDao, TemplateDaoable templateDao,	ArticleDaoable articleDao) {
		
		this(null, false, site, channel, cfg, tempRoot, pubChild,again,
				resourceDao,sourceDao,channelDao,templateDao,articleDao);
		setCache(new LRUCache<String,String>());
	}
	
	public ChannelTask(String parentId, Site site, Channel channel,Configuration cfg, String tempRoot, boolean pubChild, boolean again,
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ChannelDaoable channelDao, TemplateDaoable templateDao,	ArticleDaoable articleDao) {
		
		this(parentId, true, site, channel, cfg, tempRoot, pubChild,again,
				resourceDao,sourceDao,channelDao,templateDao,articleDao);
	}
	
	protected ChannelTask(String parentId,boolean child, Site site, Channel channel,Configuration cfg, String tempRoot,  boolean again, boolean pubChild,
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ChannelDaoable channelDao, TemplateDaoable templateDao,	ArticleDaoable articleDao) {
		
		super(parentId, child, site, channel, cfg, tempRoot,again,resourceDao,sourceDao);
		this.tempRoot = tempRoot;
		this.pubChild = child;
		this.again = again;
		this.channelDao = channelDao;
		this.templateDao = templateDao;
		this.articleDao = articleDao;
	}
	
	@Override
	protected void regSubtask(PublishRunnerable pubRunner){
		if(pubChild){
			regChildrenOfChannel(pubRunner, channel);
		}else{
			regTemplateOfChannel(pubRunner, channel);
		}
	}
		
	private void regChildrenOfChannel(PublishRunnerable pubRunner, Channel parent){
		
		List<Channel> children = channelDao.findPublishChildren(site.getId(),parent.getId());
		for(Channel c : children){
			regChildrenOfChannel(pubRunner, c);
		}
		regTemplateOfChannel(pubRunner,parent);
	}
	
	private void regTemplateOfChannel(PublishRunnerable pubRunner, Channel channel){
		
		List<Template> templates = templateDao.findInChannel(channel.getId());
		//模板任务生成有次序以下代码顺序不能更改
		regDetailTemplateTask(pubRunner, channel, templates);
		regListTemplateTask(pubRunner, channel , templates);
		regHomeOrOtherTemplateTask(pubRunner, channel, templates);
	}
	
	private void regDetailTemplateTask(PublishRunnerable pubRunner, Channel channel, List<Template> templates){
		for(Template t : templates){
			
			if(!isDetailTemplate(t)){
				continue;
			}
			
			Taskable task =  new DetailChannelTask(id, site, channel, cfg, tempRoot, 
					again, t.getUniquePath(),resourceDao, sourceDao, articleDao).
					setCache(cache).
					setUriRule(getUriRule(t));
			
			task.regTask(pubRunner);
		}
	}
	
	private UriRuleable getUriRule(Template t){
		return StringUtils.isBlank(t.getUriPattern()) ?	null : UriRules.newUriRuleBy(t.getUriPattern());
	}
	
	private void regListTemplateTask(PublishRunnerable pubRunner, Channel channel, List<Template> templates){
		for(Template t : templates){
			if(!isListTemplate(t)){
				continue;
			}
			
			Taskable task =  new ListTask(id, site, channel, cfg,  tempRoot,
					t.getUniquePath(), resourceDao, sourceDao, articleDao).
					setCache(cache).
					setUriRule( getUriRule(t));
			
			task.regTask(pubRunner);
		}
	}
	
	private void regHomeOrOtherTemplateTask(PublishRunnerable pubRunner, Channel channel, List<Template> templates){
		for(Template t : templates){
			if(!isHomeOrOtherTemplate(t)){
				continue;
			}
			
			Taskable task =  new HomeTask(id, site, channel, cfg, tempRoot,
					t.getUniquePath(), resourceDao, sourceDao).
					setCache(cache).
					setUriRule(getUriRule(t));
			
			task.regTask(pubRunner);
		}
	}
	
	private boolean isListTemplate(Template template){
		return TemplateType.LIST == template.getType();
	}
	
	private boolean isDetailTemplate(Template template){
		return TemplateType.DETAIL == template.getType();
	}
	
	private boolean isHomeOrOtherTemplate(Template template){
		return TemplateType.HOME == template.getType() ||
				TemplateType.OTHER == template.getType();
	}
	
	@Override
	public TaskType getTaskType() {
		return TaskType.CHANNEL;
	}

	@Override
	public String getRemark() {
		return String.format("(%d)%s--频道网页发布", channel.getId() ,channel.getName());
	}

	@Override
	protected String newKey() {
        StringBuilder builder = new StringBuilder();
		
		builder.append("ChannelTask[");
		builder.append("channelId=");
		builder.append(channel.getId());
		builder.append(",again=");
		builder.append(again);
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	protected int totalCount() {
		return 0;
	}

	@Override
	protected void doHandler(List<DepTask> tasks, int count, int index, int batchSize) {
		// not task
	}
}

package com.ewcms.publication.task.generator;

import java.util.ArrayList;
import java.util.List;

import com.ewcms.publication.cache.Cacheable;
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
 * 发布指定频道下的文章
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class DetailChannelTask extends TemplateTaskBase{
	private final ArticleDaoable articleDao;
	
	private Long startId = Long.MAX_VALUE;
	
	public DetailChannelTask(Site site, Channel channel, Configuration cfg, String tempRoot, boolean again, String templatePath, 
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ArticleDaoable articleDao) {
		
		this(null, false, site, channel, cfg, tempRoot, again, templatePath, resourceDao, sourceDao, articleDao);
	}
	
	public DetailChannelTask(String parentId, Site site, Channel channel, Configuration cfg, String tempRoot, boolean again, String templatePath, 
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ArticleDaoable articleDao) {
		
		this(parentId, true, site, channel, cfg, tempRoot, again, templatePath, resourceDao, sourceDao, articleDao);
	}
	
	protected DetailChannelTask(String parentId, boolean child, Site site, Channel channel, Configuration cfg, String tempRoot, boolean again, String templatePath, 
			ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao, ArticleDaoable articleDao) {
		
		super(parentId, child, site, channel, cfg, tempRoot, again, templatePath, resourceDao, sourceDao);
		this.articleDao = articleDao;
		this.setUriRule(UriRules.newDetail());
	}
	
	@Override
	protected String newKey() {
		StringBuilder builder = new StringBuilder();
			
		builder.append("ChannelDetialTask[");
		builder.append("channelId=");
		builder.append(channel.getId());
		builder.append(",again=");
		builder.append(again);
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	protected List<Generatorable> buildGenerator(int count, int index, int batchSize, String templatePath) {
		List<Article> articles = articleDao.findPrePublish(channel.getId(), again, startId, batchSize);
		
		List<Generatorable> generators = new ArrayList<Generatorable>(articles.size());
		for(Article a : articles){
			Generatorable generator = new FreemarkerGenerator.
					Builder(id, site, channel, cfg, templatePath).
					setArticle(a).
					setPageCount(a.getTotalPage()).
					setPageNumber(a.getPage()).
					setUriRule(uriRule).
					setIncludeCache(cache).
					setMarkId(a.getId()).
					build();
			generators.add(generator);
			startId = a.getId();
		}
		
		return generators;
	}


	@Override
	protected int totalCount() {
		return articleDao.findPrePublishCount(channel.getId(), again);
	}
	
	@Override
	public DetailChannelTask setCache(Cacheable<String,String>includeCache){
		super.setCache(includeCache);
		return this;
	}
	
	@Override
	public DetailChannelTask setUriRule(UriRuleable uriRule){
		super.setUriRule(uriRule);
	    return this;
	}
	
	@Override
	public TaskType getTaskType() {
		return TaskType.DETAIL;
	}

	@Override
	public String getRemark() {
		return String.format("(%d)%s--频道文章发布", channel.getId() ,channel.getName());
	}

}

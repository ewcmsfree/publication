package com.ewcms.publication.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.cache.NoneCache;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.page.PageOut;
import com.ewcms.publication.module.Article;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.uri.UriRuleable;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerGenerator implements Generatorable{
	private static final Logger logger = LoggerFactory.getLogger(FreemarkerGenerator.class);
	
	public static class Builder{
		private final String taskId;
		private final Site site;
		private final Channel channel;
		private final Configuration cfg;
		private final String templatePath;
		
		private int pageNumber = -1;
		private int pageCount = -1;
		private Article article;
		private List<Article> pageArticles;
		private UriRuleable uriRule;
		private Cacheable<String,String> includeCache = new NoneCache<String,String>();
		private PageOut pageOut ;
		private Long markId  = -1l;
		
		public Builder(String taskId, Site site, Channel channel, 
				Configuration cfg, String templatePath){
			
			this.taskId = taskId;
			this.site = site;
			this.channel = channel;
			this.cfg = cfg;
			this.templatePath = templatePath;
		}
		
		public Builder setPageNumber(int pageNumber){
			this.pageNumber = pageNumber;
			
			return this;
		}
		
		public Builder setPageCount(int pageCount){
			this.pageCount = pageCount;
			
			return this;
		}
		
		public Builder setArticle(Article article){
			this.article = article;
			
			return this;
		}
		
		public Builder setPageArticles(List<Article> articles){
			this.pageArticles = articles;
			
			return this;
		}
		
		public Builder setUriRule(UriRuleable uriRule){
			this.uriRule = uriRule;
			
			return this;		
		}
		
		public Builder setIncludeCache(Cacheable<String,String> includeCache){
			this.includeCache = includeCache;
			
			return this;
		}
		
		public Builder setPageOut(PageOut pageOut){
			this.pageOut = pageOut;
			
			return this;
		}
		
		public Builder setMarkId(Long markId){
			this.markId = markId;
			
			return this;
		}
		
		public FreemarkerGenerator build(){
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put(GlobalVariable.TASK_ID.getVariable(), taskId);
			parameters.put(GlobalVariable.SITE.getVariable(), site);
			parameters.put(GlobalVariable.CHANNEL.getVariable(), channel);
			parameters.put(GlobalVariable.INCLUDE_CACHE.getVariable(), includeCache);
			
			if(pageCount > -1){
				parameters.put(GlobalVariable.PAGE_COUNT.getVariable(), pageCount);
			}
			
			if(pageNumber > -1){
				parameters.put(GlobalVariable.PAGE_NUMBER.getVariable(), pageNumber);
			}
			
			if(pageOut != null){
				parameters.put(GlobalVariable.PAGE_OUT.getVariable(), pageOut);
			}
			
			if(article != null){
				parameters.put(GlobalVariable.ARTICLE.getVariable(), article);
			}
			
			if(pageArticles != null){
				parameters.put(GlobalVariable.ARTICLE_LIST.getVariable(), pageArticles);
			}
			
			if(uriRule != null){
				parameters.put(GlobalVariable.URI_RULE.getVariable(), uriRule);
			}
			
			return new FreemarkerGenerator(taskId, site, channel, templatePath, parameters, uriRule, cfg, markId);
		}
		
	}
	
	private final String taskId;
	private final Site site;
	private final Channel channel;
	private final String templatePath;
	private final Map<String,Object> parameters;
	private final UriRuleable uriRule;
	private final Configuration cfg;
	private final Long markId;
	
	private FreemarkerGenerator(String taskId,Site site, Channel channel, String templatePath, 
			Map<String,Object> parameters, UriRuleable uriRule, Configuration cfg,Long markId){
		
		this.taskId = taskId;
		this.site = site;
		this.channel = channel;
		this.templatePath = templatePath;
		this.parameters = parameters;
		this.uriRule = uriRule;
		this.cfg = cfg;
		this.markId = markId;
	}
	
	public String getTaskId(){
		return taskId;
	}
	
	@Override
	public void process(Writer writer)throws PublishException{
		try{
			Template t = cfg.getTemplate(templatePath);
			t.process(parameters, writer);	
		}catch(TemplateException e){
			logger.error("Freemarker generator is error,template exception is {}", e.getMessage());
			throw new PublishException(e.getMessage());
		}catch(IOException e){
			logger.error("Freemarker generator is error,io exception is {}", e.getMessage());
			throw new PublishException(e.getMessage());
		}
	}
	
	@Override
	public String pubUri()throws PublishException {
		String url = "" ;
		if(StringUtils.isNotBlank(site.getSiteURL())){
			url = url + site.getSiteURL();
		}
		if(StringUtils.isNotBlank(site.getSiteRoot())){
			url = url + site.getSiteRoot() ;
		}
        return url + uriRule.uri(parameters);
	}
	
	@Override
	public String remark(){
		return String.format("(%d)%s--%s", channel.getId(),channel.getName(),templatePath);
	}
	
	@Override
	public Map<String,Object> getParameters(){
		return this.parameters;
	}

	@Override
	public String pubPath() throws PublishException {
		String uri = uriRule.uri(parameters);
        return site.getPath() + FilenameUtils.normalize(uri);
	}

	@Override
	public Long getMarkId() {
		return this.markId;
	}
}

/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.generator;

import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.cache.NoneCache;
import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.publish.PublishRunnerable;
import com.ewcms.publication.task.TaskBase;
import com.ewcms.publication.task.resource.ResourceSiteTask;
import com.ewcms.publication.task.resource.TemplateSourceSiteTask;
import com.ewcms.publication.uri.UriRuleable;

import freemarker.template.Configuration;

/**
 * 实现发布任务
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public abstract class GenTaskBase extends TaskBase{
    protected final Channel channel;
    protected final Configuration cfg;
    protected final String tempRoot;
    protected final boolean again;
    protected final ResourceDaoable resourceDao;
    protected final TemplateSourceDaoable sourceDao;
    
    protected UriRuleable uriRule;
    protected Cacheable<String,String> cache = new NoneCache<String,String>();
    
    public GenTaskBase(Site site, Configuration cfg,  String tempRoot, boolean again, 
    		 ResourceDaoable resourceDao, TemplateSourceDaoable templateSourceDao){
    	
    	this(site, new Channel(), cfg, tempRoot, again, resourceDao, templateSourceDao);
    }
    
    public GenTaskBase(Site site, Channel channel, Configuration cfg,  String tempRoot, boolean again,
    		ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao){
    	
    	this(null, false, site, channel, cfg, tempRoot, again, resourceDao, sourceDao);
    }
    
    public GenTaskBase(String parentId, Site site, Channel channel, Configuration cfg, String tempRoot,
    		boolean again, ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao){
    	
    	this(parentId, true, site, channel, cfg, tempRoot, again, resourceDao, sourceDao);
    }
    
    protected GenTaskBase(String parentId, boolean child, Site site, Channel channel, Configuration cfg, 
    		String tempRoot, boolean again, ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao){
    	
    	super(parentId, child, site);
        this.channel = channel;
        this.cfg = cfg;
        this.tempRoot = tempRoot;
        this.again = again;
        this.resourceDao = resourceDao;
        this.sourceDao = sourceDao;
    }
    
    @Override
    public void regTask(PublishRunnerable pubRunner){
    	regDependenceTask(pubRunner);
    	regSubtask(pubRunner);
    	pubRunner.regTask(this);
    }
    
    protected void regDependenceTask(PublishRunnerable pubRunner){
    	if(!child){
    		new ResourceSiteTask(id, site, false, resourceDao).regTask(pubRunner);
    		new TemplateSourceSiteTask(id, site, tempRoot, false, sourceDao).regTask(pubRunner);
    	}
    }
    
    protected abstract void regSubtask(PublishRunnerable pubRunner);
   
    public GenTaskBase setUriRule(UriRuleable uriRule){
    	if(uriRule != null){
    		this.uriRule = uriRule;    		
    	}
    	return this;
    }
    
    public GenTaskBase setCache(Cacheable<String,String> cache){
    	this.cache = cache;
    	return this;
    }
}

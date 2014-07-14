package com.ewcms.publication.task.resource;

import java.util.Collections;
import java.util.List;

import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.module.TemplateSource;

public class TemplateSourceTask extends TemplateSourceSiteTask{
	private final List<Long> ids;
	
	private List<TemplateSource> sources;

	public TemplateSourceTask(Site site, String tempRoot, List<Long> ids, TemplateSourceDaoable sourceDao) {
		super(site, tempRoot, true, sourceDao);
		this.ids = ids;
	}
	
	@Override
	protected String newKey() {
	   Collections.sort(ids);
       StringBuilder builder = new StringBuilder();
		
		builder.append("TemplateSourceTask[");
		builder.append("ids = ");
    	for(Long id : ids){
    		builder.append(id).append("-");
    	}
		builder.append("]");
		
		return builder.toString();
	}
	
	@Override
	protected void doHandler(List<DepTask> tasks, int count, int index, int batchSize) {
		int from = index * batchSize;
		int to = from + batchSize;
		to = to < count ? to : count;
		List<TemplateSource> list = sources.subList(from, to);
		for(TemplateSource source : list){
			DepTask t = processSource(source);
	    	tasks.add(t);	
		}
	}
	
	@Override
	protected int totalCount() {
		sources = sourceDao.findPublish(site.getId(), ids);
		return sources.size();
	}
}

package com.ewcms.publication.task.resource;

import java.util.Collections;
import java.util.List;

import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.module.Resource;
import com.ewcms.publication.module.Site;

public class ResourceTask extends ResourceSiteTask{
	private final List<Long> ids;
	
	private List<Resource> resources;
	
	public ResourceTask(Site site, List<Long> ids, ResourceDaoable resourceDao) {
		super(site, true, resourceDao);
		this.ids = ids;
	}
	
	@Override
	protected String newKey(){
		Collections.sort(ids);
		
		StringBuilder builder = new StringBuilder();
		builder.append("ResourceTask[");
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
		List<Resource> list = resources.subList(from, to);
		for(Resource r : list){
			tasks.add(newDepTask(r));
		}
	}
	
	@Override
	protected int totalCount() {
		resources = resourceDao.findPublish(site.getId(), ids);
		return resources.size();
	}

}

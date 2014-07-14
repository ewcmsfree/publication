package com.ewcms.publication.task.resource;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.deploy.DepTask.DepTaskType;
import com.ewcms.publication.module.Resource;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.task.TaskBase;

public class ResourceSiteTask extends TaskBase{
	private final boolean again ;
	protected final ResourceDaoable resourceDao;
	
	private Long startId = Long.MAX_VALUE;
	
	public ResourceSiteTask(Site site, boolean again, ResourceDaoable resourceDao) {
		
		this(null,false,site,again,resourceDao);
	}
	
	public ResourceSiteTask(String parentId, Site site, boolean again, ResourceDaoable resourceDao) {
		
		this(parentId,true,site,again,resourceDao);
	}
	
	protected ResourceSiteTask(String parentId, boolean child, Site site, boolean again, ResourceDaoable resourceDao) {
		
		super(parentId,child, site);
		this.again = again;
		this.resourceDao = resourceDao;
	}
	
	@Override
	protected String newKey(){
		StringBuilder builder = new StringBuilder();
		
		builder.append("ResourceTask[");
		builder.append("siteId=");
		builder.append(site.getId());
		builder.append("agein=");
		builder.append(again);
		builder.append("]");
		
		return builder.toString();
	}
	
	@Override
	protected void doHandler(List<DepTask> tasks, int count, int index, int batchSize) {
		List<Resource> resources = resourceDao.findPublish(site.getId(), again, startId, batchSize);
		for(Resource r : resources){
			tasks.add(newDepTask(r));
			startId = r.getId();
		}
	}

	protected DepTask newDepTask(Resource r){
        String pubPath = site.getPath() + FilenameUtils.normalize(r.getUri());
        return DepTask.success(id, DepTaskType.RESOURCE, r.getPath(), r.getUri(), pubPath, r.getId());
	}

	@Override
	protected int totalCount() {
		return resourceDao.findPublishCount(site.getId(), again);
	}
	
	@Override
	public String getRemark() {
		return String.format("(%d)%s--资源发布", site.getId() , site.getSiteName());
	}
	
	@Override
	public TaskType getTaskType(){
		return TaskType.RESOURCE;
	}
}

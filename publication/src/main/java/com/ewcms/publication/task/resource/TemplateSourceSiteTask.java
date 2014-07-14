package com.ewcms.publication.task.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.deploy.DepTask.DepTaskType;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.module.TemplateSource;
import com.ewcms.publication.task.TaskBase;

public class TemplateSourceSiteTask extends TaskBase{
	private static final Logger logger = LoggerFactory.getLogger(TemplateSourceSiteTask.class);
	
	private final String tempRoot;
	private final boolean again;
	protected final TemplateSourceDaoable sourceDao;
	
	private File dir;
	private Long startId = Long.MAX_VALUE;

	public TemplateSourceSiteTask(Site site, String tempRoot, 
			boolean again, TemplateSourceDaoable sourceDao) {
		
		this(null,false,site,tempRoot,again,sourceDao);
	}
	
	public TemplateSourceSiteTask(String parentId, Site site, 
			String tempRoot, boolean again, TemplateSourceDaoable sourceDao) {
		
		this(parentId,true,site,tempRoot,again,sourceDao);
		
	}
	
	protected TemplateSourceSiteTask(String parentId, boolean child,Site site, 
			String tempRoot, boolean again, TemplateSourceDaoable sourceDao){
		
		super(parentId, child, site);
		this.tempRoot = tempRoot;
		this.again = again;
		this.sourceDao = sourceDao;
	}

	@Override
	public TaskType getTaskType() {
		return TaskType.TEMPLATESOURCE;
	}

	@Override
	public String getRemark() {
		return String.format("(%d)%s--模板资源发布", site.getId() , site.getSiteName());
	}

	@Override
	protected String newKey() {
       StringBuilder builder = new StringBuilder();
		
		builder.append("TemplateSourceTask[");
		builder.append("siteId=");
		builder.append(site.getId());
		builder.append("agein=");
		builder.append(again);
		builder.append("]");
		
		return builder.toString();
	}
	
	@Override
	protected boolean initTask(List<DepTask> tasks, String id){
     	boolean success = false;
		try {
     		String path = String.format("%s/%s", tempRoot, id);
         	dir = new File(path);
			FileUtils.forceMkdir(dir);
			success = true;
		} catch (IOException e) {
			dir = null;
			tasks.add(DepTask.error(id, "创建任务临时目录失败", e.getMessage()));
			tasks.add(DepTask.finish(id));
		}
		
		return success;
	}
	
	@Override
	protected void finishTask(List<DepTask> tasks, String id) {
		tasks.add(DepTask.finish(id, dir.getPath()));
	}


	@Override
	protected void doHandler(List<DepTask> tasks, int count, int index, int batchSize) {
		List<TemplateSource> sources = sourceDao.findPublish(site.getId(), again, startId, batchSize);
		for(TemplateSource source : sources){
			DepTask t = processSource(source);
	    	tasks.add(t);
	    	startId = source.getId();
		}
	}
	
	protected DepTask processSource(TemplateSource source){
		DepTask t = null;
		OutputStream out = null;
    	try{
    		File temp = newTempFile(dir);
    		out = new FileOutputStream(temp);
    		out.write(source.getContent());
    		String pubPath = pubPath(source.getPath());
    		t = DepTask.success(id, DepTaskType.TEMPLATESOURCE, temp.getPath(),
    				source.getUniquePath(), pubPath, source.getId());
    	}catch(Exception e){
    		logger.error("Template source out temp file exception {}", e.getMessage());
    		t = DepTask.error(id, source.getUniquePath() +"写临时文件错误" , e.getMessage());
    	}finally{
    		if(out != null){
    			closeOutQuietly(out);
    		}
    	}
    	
    	return t;
	}
	
	private String pubPath(String path){
		String p = FilenameUtils.normalize(path);
		if(!StringUtils.startsWith(p, File.separator)){
			p = File.separator + p;
		}
		return site.getPath() + p;
	}
	
	private void closeOutQuietly(OutputStream out){
		try{
			out.close();
		}catch(IOException e){
			//none instance
		}
	}

	@Override
	protected int totalCount() {
		return sourceDao.findPublishCount(site.getId(), again);
	}
}

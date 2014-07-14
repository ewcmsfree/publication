/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.deploy.DepTask.DepTaskType;
import com.ewcms.publication.generator.Generatorable;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.publish.PublishRunnerable;

import freemarker.template.Configuration;

/**
 * 发布Template生成页面任务
 * 
 * @author <a href="hhywangwei@gmail.com">wangwei</a>
 */
public abstract class TemplateTaskBase extends GenTaskBase{
	private static final Logger logger = LoggerFactory.getLogger(TemplateTaskBase.class);
	
	protected final String templatePath;
	
	private File dir;
	
	protected TemplateTaskBase(String parentId, boolean child, Site site, Channel channel, Configuration cfg, String tempRoot, boolean again,
			String templatePath, ResourceDaoable resourceDao, TemplateSourceDaoable sourceDao) {
		
		super(parentId, child, site, channel, cfg, tempRoot, again, resourceDao, sourceDao);
		this.templatePath = templatePath;
	}

	@Override
	protected void regSubtask(PublishRunnerable pubRunner){
		//none instance
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
		List<Generatorable> gens = buildGenerator(count,index,batchSize,templatePath);
		for(Generatorable gen : gens){
			generator(tasks,gen,dir);	
		}
	}
	
	protected abstract List<Generatorable> buildGenerator(int count, int index, int batchSize, String templatePath);
	
	protected void generator(List<DepTask> tasks, Generatorable gen,File dir){
		Writer writer = null;
		DepTask t = null;
    	try{
    		File temp = newTempFile(dir);
    		writer = new FileWriter(temp);
    		gen.process(writer);
    		t = DepTask.success(id, getDepTaskType(), temp.getPath(),
    				gen.pubUri(), gen.pubPath(), gen.getMarkId());
    	}catch(Exception e){
    		logger.error("Generator exception is {}", e.getMessage());
    		t = DepTask.error(id,  gen.remark(), e.getMessage());
    	}finally{
    		if(writer != null){
    			closeWriterQuietly(writer);	
    		}
    	}
    	tasks.add(t);
	}
	
	private DepTaskType getDepTaskType(){
		if(getTaskType() == TaskType.HOME){
			return DepTaskType.HOME;
		}
		if(getTaskType() == TaskType.LIST){
			return DepTaskType.LIST;
		}
		if(getTaskType() == TaskType.DETAIL){
			return DepTaskType.DETAIL;
		}
		return DepTaskType.OTHER;
	}
	
	
	private void closeWriterQuietly(Writer writer){
		try{
   			writer.close();	
   		}catch(IOException e){
   			//Not instance
   		}
	}
}

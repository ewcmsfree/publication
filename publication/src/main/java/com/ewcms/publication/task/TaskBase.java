package com.ewcms.publication.task;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.publish.PublishRunnerable;

public abstract class TaskBase implements Taskable {
	private static final String FILE_NAME_CHARS = "1234567890abcdefghigklmnopqrstuvwxyz";
	private static final String TASK_ID_SAPCE = "1234567890";
	private static final int DEFAULT_BATCH_SIZE = 20;

	protected final Site site;
	protected final String id;
	protected final String parentId;
	protected final boolean child;

	private int batchSize = DEFAULT_BATCH_SIZE;
	private int totalCount = -1;
	private boolean hasNext = true;
	protected String key;
	private int index = 0;
	
	public TaskBase(String parentId, boolean child, Site site){
		this.id = newTaskId(site);
		this.parentId = parentId;
		this.child = child;
		this.site = site;
	}

	protected String newTaskId(Site site) {
		return String.format("%d-%d-%s", site.getId(),
				System.currentTimeMillis(),
				RandomStringUtils.random(8, TASK_ID_SAPCE));
	}
	
	@Override
	public String getKey() {
		if(key == null){
			key = DigestUtils.md5Hex(newKey());
		}
		return key;
	}

	protected abstract String newKey();


	@Override
	public List<DepTask> next() {
		int count = getTotalCount();

		List<DepTask> tasks = new LinkedList<DepTask>();

		if (index == 0) {
			boolean success = initTask(tasks, id);
			if (!success) {
				return tasks;
			}
		}

		if (index < ((count + batchSize -1) / batchSize)) {
			doHandler(tasks, count, index, batchSize);
			index = index + 1;
		} else {
			hasNext = false;
			finishTask(tasks, id);
		}

		if (!hasNext) {
			destroyTask(id);
		}

		return tasks;
	}

	/**
	 * 初始化任务
	 * 
	 * @param tasks 发布任务列表
	 * @param id 任务编号
	 */
	protected boolean initTask(List<DepTask> tasks, String id) {
		// sub instance
		return true;
	}

	/**
	 * 开始处理任务
	 * 
	 * @param count
	 * @param index
	 * @param batchSize
	 * @return
	 */
	protected abstract void doHandler(List<DepTask> tasks, int count, int index, int batchSize);

	/**
	 * 完成任务
	 * 
	 * @param tasks 发布任务列表
	 * @param id 任务编号
	 * @return
	 */
	protected void finishTask(List<DepTask> tasks, String id) {
		tasks.add(DepTask.finish(id));
	}

	/**
	 * 销毁任务
	 * 
	 * @param id  任务编号
	 */
	protected void destroyTask(String id) {
		// sub instance
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getParentId() {
		return parentId;
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public int getTotalCount() {
		if (totalCount == -1) {
			totalCount = totalCount();
		}
		return totalCount;
	}

	protected abstract int totalCount();

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	@Override
	public boolean isChild() {
		return child;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}
	
	@Override
	public void regTask(PublishRunnerable runner){
		runner.regTask(this);
	}

	protected File newTempFile(File dir) throws IOException {
		return new File(dir, RandomStringUtils.random(32, FILE_NAME_CHARS));
	}
}

package com.ewcms.publication.deploy;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.dao.PublishDaoable;
import com.ewcms.publication.deploy.DepTask.DepTaskType;
import com.ewcms.publication.deploy.operator.FileOperatorable;
import com.ewcms.publication.deploy.operator.LocalFileOperator;

/**
 * 资源发布
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class DepTaskRunner implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(DepTaskRunner.class);

	private final DepTaskQueueable taskQueue;
	private final PublishDaoable publishDao;
	private final FileOperatorable operator = new LocalFileOperator();

	public DepTaskRunner(DepTaskQueueable taskQueue, PublishDaoable publishDao) {

		this.taskQueue = taskQueue;
		this.publishDao = publishDao;
	}

	@Override
	public void run() {
		while (true) {

			if (Thread.interrupted()) {
				return;
			}

			try {
				DepTask t = taskQueue.task();
				if (t != null && t.isFinish()) {
					publishDao.finishPublishTask(t.getTaskId());
					if(t.getSource() != null){
						FileUtils.deleteQuietly(new File(t.getSource()));	
					}
				} else {
					boolean success = deploy(t);
					completeOne(t.getTaskId(), success);
				}
			} catch (Exception e) {
				logger.error("Dep resource error is {}", e.getMessage());
			}
		}
	}

	private boolean deploy(final DepTask task) {
		boolean success = false;
		try {
			if (task.isError()) {
				publishDao.publishErrorLog(task.getTaskId(), task.getRemark(),
						task.getException());
			} else {
				operator.copy(task.getSource(), task.getPath());
				afterSuccess(task);
				success = true;
			}
		} catch (PublishException e) {
			String remark = String.format("发布文件%s错误", task.getPath());
			publishDao
					.publishErrorLog(task.getTaskId(), remark, e.getMessage());
		}

		return success;
	}

	private void afterSuccess(final DepTask task) {
		if (task.getType() == DepTaskType.DETAIL) {
			publishDao.publishArticle(task.getMarkId(), task.getUri());
		}
		if(task.getType() == DepTaskType.RESOURCE){
			publishDao.publishResource(task.getMarkId());
		}
		if(task.getType() == DepTaskType.TEMPLATESOURCE){
			publishDao.publishTemplateSource(task.getMarkId());
		}
	}

	private void completeOne(String id, boolean success) {
		publishDao.completeOne(id, success);
	}

	public void shutdown() {
		Thread.currentThread().interrupt();
	}
}

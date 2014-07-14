package com.ewcms.publication.dao.publish;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ewcms.publication.dao.PublishDaoable;
import com.ewcms.publication.dao.constant.ArticleStatus;
import com.ewcms.publication.dao.constant.PublishStatus;

@Repository
public class PublishDao implements PublishDaoable{
	private static final String NEW_PUBLISH_TASK_SQL = "INSERT INTO pub_task (id, site_id, remark, parent_id,publish_count) VALUES (:id, :siteId, :remark, :parentId, :count)";
	private static final String START_PUBLISH_TASK_SQL = "UPDATE pub_task SET status = :status, start_time = now() WHERE id = :id";
	private static final String FINISH_PUBLISH_TASK_SQL = "UPDATE pub_task SET status = :status, finish_time = now(),use_time = extract(epoch from age(now(), start_time)) * 1000 WHERE id = :id";
	private static final String PUBLISH_ERROR_SQL = "INSERT INTO pub_error(task_id, remark, error) VALUES (:taskId, :remark, :error)";
	private static final String PUBLISH_ARTICLE_SQL = "UPDATE content_article SET url=:url, status=:status WHERE id= :id AND (status != :status OR url != :url)";
	private static final String PUBLISH_RESOURCE_SQL = "UPDATE content_resource SET status=:status WHERE id= :id AND status != :status";
	private static final String PUBLISH_TEMPLATESOURCE_SQL = "UPDATE site_templatesource SET release= :release WHERE id= :id AND release != :release";
	private static final String COMPLETE_ONE_SQL = "UPDATE pub_task SET finish_count = finish_count + :finishCount, error_count = error_count + :errorCount WHERE id=:id";

    private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public void newPublishTask(String id, String parentId, Integer siteId, String remark, Integer count) {
		
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("id", id).
				addValue("siteId", siteId).
				addValue("remark", remark).
				addValue("parentId", parentId).
				addValue("count", count);
		
		jdbcTemplate.update(NEW_PUBLISH_TASK_SQL, params);
	}

	@Override
	public void startPublishTask(String id) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("status",PublishStatus.RUNNING.ordinal()).
				addValue("id", id);
		
		jdbcTemplate.update(START_PUBLISH_TASK_SQL, params);
	}
	
	@Override
	public void finishPublishTask(String id) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("status",PublishStatus.FINISH.ordinal()).
				addValue("id", id);		
		
		jdbcTemplate.update(FINISH_PUBLISH_TASK_SQL, params);
	}

	@Override
	public void publishErrorLog(String taskId, String remark,String exception) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("taskId",taskId).
				addValue("remark", remark).
				addValue("error", exception);	
		
		jdbcTemplate.update(PUBLISH_ERROR_SQL, params);
	}

	@Override
	public void publishArticle(Long id, String url) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("url", url).
				addValue("status", ArticleStatus.RELEASE.name()).
				addValue("id", id);
		
		jdbcTemplate.update(PUBLISH_ARTICLE_SQL, params);
	}
	
	@Override
	public void publishResource(Long id) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("status", "RELEASED").
				addValue("id", id);
		
		jdbcTemplate.update(PUBLISH_RESOURCE_SQL, params);
	}
	
	@Override
	public void publishTemplateSource(Long id) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("release", Boolean.TRUE).
				addValue("id", id);
		
		jdbcTemplate.update(PUBLISH_TEMPLATESOURCE_SQL, params);
		
	}

	@Override
	public void completeOne(String id, boolean success) {
		Integer finishCount = success ? 1 : 0;
		Integer errorCount = success ? 0 : 1;
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("id", id).
				addValue("finishCount", finishCount).
				addValue("errorCount", errorCount);
		
		jdbcTemplate.update(COMPLETE_ONE_SQL, params);
	}
}

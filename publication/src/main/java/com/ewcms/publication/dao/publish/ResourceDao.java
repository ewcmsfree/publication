package com.ewcms.publication.dao.publish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.module.Resource;

@Repository
public class ResourceDao implements ResourceDaoable {
	private static final String FIND_IDS_SQL = "SELECT id,name,path,uri FROM content_resource WHERE site_id = :siteId AND id IN (:ids)";
	private static final String FIND_COUNT_SQL = "SELECT count(id) FROM content_resource WHERE site_id = :siteId AND status IN (:status)";
	private static final String FIND_SQL = "SELECT id,name,path,uri FROM content_resource "
			+ "WHERE site_id = :siteId AND status IN (:status) AND id < :id ORDER BY id DESC limit :limit";
	private static final String FIND_BY_URI_SQL = "SELECT id,name,path,uri FROM content_resource WHERE site_id= :siteId AND uri= :uri";
	
    private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public List<Resource> findPublish(Integer siteId, List<Long> ids) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("ids", ids);
		
		return jdbcTemplate.query(FIND_IDS_SQL, params, new ResourceMapper());
	}
	
	@Override
	public Integer findPublishCount(Integer siteId, Boolean forceAgain) {
		
		String[] status = forceAgain ? new String[]{"NORMAL","RELEASED"} : new String[]{"NORMAL"};
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("status", Arrays.asList(status));
		
		return jdbcTemplate.queryForObject(FIND_COUNT_SQL, params, Integer.class);
	}
 
	@Override
	public List<Resource> findPublish(Integer siteId, Boolean forceAgain, Long startId, Integer limit) {
		String[] status = forceAgain ? new String[]{"NORMAL","RELEASED"} : new String[]{"NORMAL"};
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("status", Arrays.asList(status)).
				addValue("id", startId).
				addValue("limit", limit);
		
		return jdbcTemplate.query(FIND_SQL, params, new ResourceMapper());
	}

	@Override
	public Resource findByUri(Integer siteId, String uri) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("uri", uri);
		try{
			return jdbcTemplate.queryForObject(FIND_BY_URI_SQL, params, new ResourceMapper());
		}catch(DataAccessException e){
			//none instance
		}
		return null;
	}
	
	static class ResourceMapper implements RowMapper<Resource>{
		
		@Override
		public Resource mapRow(ResultSet rs, int rowNum) throws SQLException {
			Resource r = new Resource();
			
			r.setId(rs.getLong("id"));
			r.setName(rs.getString("name"));
			r.setPath(rs.getString("path"));
			r.setUri(rs.getString("uri"));
			
			return r;
		}
	}



}

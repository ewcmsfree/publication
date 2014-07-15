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

import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.module.TemplateSource;

@Repository
public class TemplateSourceDao implements TemplateSourceDaoable{
	private static final String FIND_IDS_SQL = "SELECT t1.id,t1.path,t1.uniquepath,t2.srcentity AS content FROM site_templatesource t1, site_templatesrcent t2 "
			+ "WHERE t1.tplentityid = t2.id AND t1.site_id = :siteId AND t1.id IN (:ids)";
	private static final String FIND_COUNT_SQL =  "SELECT count(t1) FROM site_templatesource t1, site_templatesrcent t2"
			+ " WHERE t1.tplentityid = t2.id AND t1.site_id = :siteId AND t1.release IN (:release)";
	private static final String FIND_SQL = "SELECT t1.id,t1.path,t1.uniquepath,t2.srcentity AS content FROM site_templatesource t1, site_templatesrcent t2"
			+ " WHERE t1.tplentityid = t2.id AND t1.site_id = :siteId AND t1.release IN (:release) AND t1.id < :id "
			+ " ORDER BY t1.id DESC LIMIT :limit";
	private static final String FIND_BY_URI_SQL = "SELECT t1.id,t1.path,t1.uniquepath,t2.srcentity AS content FROM site_templatesource t1, site_templatesrcent t2"
			+ " WHERE t1.tplentityid = t2.id AND t1.uniquepath = :uri";
	
    private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public List<TemplateSource> findPublish(Long siteId, List<Long> ids) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("ids", ids);
		
		return jdbcTemplate.query(FIND_IDS_SQL, params, new TemplateSourceMapper());
	}

	@Override
	public List<TemplateSource> findPublish(Long siteId, Boolean forceAgain, Long startId, Integer limit) {
		Boolean[] release = forceAgain ? new Boolean[]{Boolean.FALSE,Boolean.TRUE} : new Boolean[]{Boolean.FALSE};
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("release", Arrays.asList(release)).
				addValue("id", startId).
				addValue("limit", limit);
		
		return jdbcTemplate.query(FIND_SQL, params, new TemplateSourceMapper());
	}
	
	@Override
	public Integer findPublishCount(Long siteId, Boolean forceAgain) {
		Boolean[] release = forceAgain ? new Boolean[]{Boolean.FALSE,Boolean.TRUE} : new Boolean[]{Boolean.FALSE};
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("release", Arrays.asList(release));
		
		return jdbcTemplate.queryForObject(FIND_COUNT_SQL, params, Integer.class);
	}
	
	@Override
	public TemplateSource findByUri(Long siteId, String uri) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("uri", String.format("%d%s", siteId,uri));
		try{
			return jdbcTemplate.queryForObject(FIND_BY_URI_SQL, params, new TemplateSourceMapper());
		}catch(DataAccessException e){
			//none instance
		}
		return null;
	}

	static class TemplateSourceMapper implements RowMapper<TemplateSource>{
		@Override
		public TemplateSource mapRow(ResultSet rs, int rowNum)throws SQLException {
			
			TemplateSource t = new TemplateSource();
			t.setContent(rs.getBytes("content"));
			t.setId(rs.getLong("id"));
			t.setPath(rs.getString("path"));
			t.setUniquePath(rs.getString("uniquepath"));
			
			return t;
		}
	}
}

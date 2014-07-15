package com.ewcms.publication.dao.publish;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ewcms.publication.cache.Cacheable;
import com.ewcms.publication.cache.LRUCache;
import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.module.Template;
import com.ewcms.publication.module.Template.TemplateType;
import com.ewcms.publication.module.TemplateBody;

@Repository
public class TemplateDao implements TemplateDaoable {
	private static final Logger logger = LoggerFactory.getLogger(TemplateDao.class);
	
	private static final int CACHE_MAX_SIZE = 50;
	private static final String FIND_ONE_SQL = "SELECT * FROM site_template WHERE channel_id = :channelId AND id = :id";
	private static final String FIND_CHANNEL_SQL = "SELECT * FROM site_template WHERE channel_id = :channelId AND is_verify= TRUE";
	private static final String FIND_BODY_SQL = "SELECT t1.id,t1.uniquepath,t2.tplentity FROM site_template t1,site_templateentity t2 WHERE t1.tplentityid = t2.id AND t1.uniquepath= :path";
	private static final String FIND_UNIQUE_PATH_SQL = "SELECT uniquepath FROM site_template WHERE site_id = :siteId AND channel_id = :channelId AND name = :name";
	private static final String SAVE_VERIFY_SQL = "UPDATE site_template SET is_verify = :verify WHERE id = :id";
	
    private volatile Cacheable<String, Object> cache = new LRUCache<String, Object>(CACHE_MAX_SIZE);
	
    private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public void resetCache() {
		cache = new LRUCache<>(CACHE_MAX_SIZE);
	}

	@Override
	public Template findOne(Long channelId, Long id) {
		String key = templateKey(channelId,id);
		if(cache.exist(key)){
			return (Template)cache.get(key);
		}
		
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("channelId",channelId).
				addValue("id", id);
		
		Template t = null;
		try{
			t = jdbcTemplate.queryForObject(FIND_ONE_SQL, params, new TemplateMapper());
			cache.add(key, t);
		}catch(DataAccessException e){
			logger.debug("Find template not exist,channel is {} and template id is {}", channelId, id);
		}
		
		return t;
	}
	
	private String templateKey(Long channelId,Long id){
		return String.format("template-%d-%d", channelId, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Template> findInChannel(Long channelId) {
		String key = channelKey(channelId);
		if(cache.exist(key)){
			return (List<Template>)cache.get(key);
		}
		
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("channelId",channelId);
		List<Template> list = jdbcTemplate.query(FIND_CHANNEL_SQL, params, new TemplateMapper());
		cache.add(key, list);
		
		return list;
	}
	
	private String channelKey(Long channelId){
		return String.format("channel-%d", channelId);
	}

	@Override
	public TemplateBody findBody(String path) {
		TemplateBody body = null;
		try{
			SqlParameterSource params = new MapSqlParameterSource().addValue("path",path);
			body = jdbcTemplate.queryForObject(FIND_BODY_SQL, params, new TemplateBodyMapper());
		}catch(DataAccessException e){
			logger.debug("Find template body not exist, path is {}", path );
		}
		
		return body;
	}

	@Override
	public String findUniquePath(Long siteId, Long channelId, String name) {
		String path = null;
		try{
			SqlParameterSource params = new MapSqlParameterSource().
					addValue("siteId", siteId).
					addValue("channelId", channelId).
					addValue("name", name);
			path = jdbcTemplate.queryForObject(FIND_UNIQUE_PATH_SQL, params, String.class);
		}catch(DataAccessException e){
			logger.debug("Find template unique path not exist, name is {} and siteId is {} and channelId{}", 
					new Object[]{name, siteId, channelId});
		}
		
		return path;
	}
	
	@Override
	public void saveVerifyTemplate(Long templateId, boolean verify) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("id", templateId).
				addValue("verify", verify);
		jdbcTemplate.update(SAVE_VERIFY_SQL, params);
	}
	
	static class TemplateMapper implements RowMapper<Template>{

		@Override
		public Template mapRow(ResultSet rs, int rowNum) throws SQLException {
			Template t = new Template();
			
			t.setId(rs.getLong("id"));
			t.setChannelId(rs.getLong("channel_id"));
			t.setType(TemplateType.valueOf(rs.getString("type")));
			t.setUniquePath(rs.getString("uniquepath"));
			t.setUriPattern(rs.getString("uripattern"));
			
			return t;
		}
	}
	
	static class TemplateBodyMapper implements RowMapper<TemplateBody>{

		@Override
		public TemplateBody mapRow(ResultSet rs, int rowNum)throws SQLException {
			TemplateBody body = new TemplateBody();
			
			body.setId(rs.getInt("id"));
			body.setUniquePath(rs.getString("uniquepath"));
			body.setBody(rs.getBytes("tplentity"));
			
			return body;
		}
	}
}

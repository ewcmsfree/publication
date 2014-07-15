package com.ewcms.publication.dao.preview;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.dao.publish.ChannelDao.ChannelMapper;
import com.ewcms.publication.module.Channel;

public class ChannelPreviewDao implements ChannelDaoable{
	private static final Logger logger = LoggerFactory.getLogger(ChannelPreviewDao.class);
	
	private static final String FIND_ONE_SQL = "SELECT * FROM site_channel WHERE site_id= :siteId AND id= :id AND publicenable IN (:publicenable)";
	private static final String FIND_CHILDREN_SQL = "SELECT * FROM site_channel WHERE site_id= :siteId AND parent_id = :id AND publicenable IN (:publicenable)";
	private static final String FIND_URI_SQL = "SELECT * FROM site_channel WHERE site_id= :siteId AND (absurl = :uri OR pubpath= :uri) AND publicenable IN (:publicenable)";
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final List<Boolean> publicenable ;
	
	public ChannelPreviewDao(DataSource dataSource, boolean mock){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		publicenable = mock ? Arrays.asList(Boolean.TRUE,Boolean.FALSE) : Arrays.asList(Boolean.TRUE);
	}

	@Override
	public void resetCache() {
		// none instance
	}

	@Override
	public Channel findRoot(Long siteId) {
		//none instance
		return null;
	}

	@Override
	public Channel findPublishOne(Long siteId, Long id) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("id", id).
				addValue("publicenable", publicenable);
		try{
			return jdbcTemplate.queryForObject(FIND_ONE_SQL, params, new ChannelMapper());	
		}catch(DataAccessException e){
			logger.error("Site {} id {}, channel not exist", siteId, id);
		}
		return null;
	}

	@Override
	public List<Channel> findPublishChildren(Long siteId, Long id) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("id", id).
				addValue("publicenable", publicenable);
		
		return jdbcTemplate.query(FIND_CHILDREN_SQL, params, new ChannelMapper());
	}

	@Override
	public Channel findPublishParent(Long siteId, Long id) {
		Channel c = findPublishOne(siteId, id);
		if(c == null){
			return c;
		}
		return findPublishOne(siteId,c.getParentId());
	}

	@Override
	public Channel findPublishByUri(Long siteId, String uri) {
		
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId).
				addValue("uri", uri).
				addValue("publicenable", publicenable);
		try{
			return jdbcTemplate.queryForObject(FIND_URI_SQL, params, new ChannelMapper());	
		}catch(DataAccessException e){
			logger.error("Site {} uri {}, channel not exist", siteId, uri);
		}
		return null;
	}

}

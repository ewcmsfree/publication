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
import com.ewcms.publication.dao.ChannelDaoable;
import com.ewcms.publication.module.Channel;

@Repository
public class ChannelDao implements ChannelDaoable{
	private static final Logger logger = LoggerFactory.getLogger(ChannelDao.class);
	
	private static final String FIND_ROOT_SQL = "SELECT * FROM site_channel WHERE parent_id IS NULL AND site_id = :siteId AND publicenable = TRUE";
	private static final String FIND_PUBLISH_ONE_SQL = "SELECT * FROM site_channel WHERE site_id= :siteId AND id= :id AND publicenable = TRUE";
	private static final String FIND_PUBLISH_CHILDREN_SQL = "SELECT * FROM site_channel WHERE site_id= :siteId AND parent_id = :id AND publicenable = TRUE";
	private static final String FIND_PUBLISH_URI_SQL = "SELECT * FROM site_channel WHERE site_id= :siteId AND (absurl = :uri OR pubpath= :uri) AND publicenable = TRUE";
	
	private static final int CACHE_MAX_SIZE = 50;
	
	private volatile Cacheable<String, Object> cache = new LRUCache<String, Object>(CACHE_MAX_SIZE);

    private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public void resetCache() {
		cache = new LRUCache<String, Object>(CACHE_MAX_SIZE);
	}

	@Override
	public Channel findRoot(Integer siteId) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("siteId", siteId);
		
		return jdbcTemplate.queryForObject(FIND_ROOT_SQL, params, new ChannelMapper());
	}

	@Override
	public Channel findPublishOne(Integer siteId, Integer id) {
		String key = articleKey(siteId, id);
		if(cache.exist(key)){
			return (Channel)cache.get(key);
		}
		
		Channel channel = null;
		try{
			SqlParameterSource params = new MapSqlParameterSource().
					addValue("siteId", siteId).
					addValue("id", id);
			channel = jdbcTemplate.queryForObject(FIND_PUBLISH_ONE_SQL, params, new ChannelMapper());
			cache.add(key, channel);
		}catch(DataAccessException e){
			logger.debug("Find channel not exist,siteId is {} and channelId is {}", siteId ,id);
		}
		
		return channel;
	}
	
	private String articleKey(Integer siteId,Integer id){
		return String.format("article-%d-%d",siteId, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Channel> findPublishChildren(Integer siteId, Integer id) {
		String key = childrenKey(siteId, id);
		if(cache.exist(key)){
			return (List<Channel>)cache.get(key);
		}
		
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("id", id).
				addValue("siteId", siteId);
		
		List<Channel> children = jdbcTemplate.query(FIND_PUBLISH_CHILDREN_SQL, params, new ChannelMapper());
		cache.add(key, children);
		
		return children;
	}
	
	private String childrenKey(Integer siteId, Integer id){
		return String.format("children-%d-%d", siteId, id);
	}

	@Override
	public Channel findPublishParent(Integer siteId,Integer id) {
		Channel channel = this.findPublishOne(siteId, id);
		if(channel == null){
			return null;
		}
		return findPublishOne(siteId,channel.getParentId());
	}

	@Override
	public Channel findPublishByUri(Integer siteId, String uri) {
		String key = uriKey(siteId, uri);
		if(cache.exist(key)){
			return (Channel)cache.get(key);
		}
		
		Channel channel = null;
		try{
			SqlParameterSource params = new MapSqlParameterSource().
					addValue("siteId", siteId ).
					addValue("uri", uri);
			channel = jdbcTemplate.queryForObject(FIND_PUBLISH_URI_SQL, params, new ChannelMapper());
			cache.add(key, channel);
		}catch(DataAccessException e){
			logger.debug("Find channel not exist,siteId is {} and uri is {}", siteId ,uri);
		}
		return channel;
	}
	
	private String uriKey(Integer siteId,String uri){
		return String.format("article-%d-%s",siteId, uri);
	}
	
	public static class ChannelMapper implements RowMapper<Channel>{

		@Override
		public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
			Channel channel = new Channel();
			
			channel.setAbsUrl(rs.getString("absurl"));
			channel.setDir(rs.getString("dir"));
			channel.setIconUrl(rs.getString("iconurl"));
			channel.setId(rs.getInt("id"));
			channel.setListSize(rs.getInt("listsize"));
			channel.setMaxSize(rs.getInt("maxsize"));
			channel.setName(rs.getString("name"));
			channel.setParentId(rs.getInt("parent_id"));
			channel.setPubPath(rs.getString("pubpath"));
			channel.setUrl(rs.getString("url"));
			channel.setSiteId(rs.getInt("site_id"));
			
			return channel;
		}
	}
}

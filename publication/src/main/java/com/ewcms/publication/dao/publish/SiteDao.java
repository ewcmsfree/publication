package com.ewcms.publication.dao.publish;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
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
import com.ewcms.publication.dao.SiteDaoable;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.module.Site.Deploy;

@Repository
public class SiteDao implements SiteDaoable{
	private static final Logger logger = LoggerFactory.getLogger(SiteDao.class);
	
    private static final int CACHE_MAX_SIZE = 10;
    private static final String FIND_ONE_SQL = "SELECT t1.id AS id,t1.extrafile AS extrafile,t1.metadescripe AS metadescripe,"
    		+ " t1.metakey AS metakey,t1.resourcedir AS resourcedir,t1.sitename AS sitename,t1.siteroot AS siteroot,"
    		+ " t1.siteurl AS siteurl,t2.outputtype AS outputtype,t2.hostname AS hostname,t2.username AS username,"
    		+ " t2.password AS password,t2.path AS path,t2.port AS port "
    		+ " FROM site_site t1,site_siteserver t2 "
    		+ " WHERE t1.serverid = t2.id AND t1.id = :id AND t1.publicenable = TRUE";
	
	private volatile Cacheable<Long, Site> cache = new LRUCache<Long, Site>(CACHE_MAX_SIZE);
	
    private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public void resetCache() {
		cache = new LRUCache<Long, Site>(CACHE_MAX_SIZE);
	}

	@Override
	public Site findOne(Long id) {
		if(cache.exist(id)){
			return cache.get(id);
		}
		Site site = null;
		try{
			SqlParameterSource params = new MapSqlParameterSource().
					addValue("id", id);
			site = jdbcTemplate.queryForObject(FIND_ONE_SQL, params, new SiteMapper());
			cache.add(id, site);	
		}catch(DataAccessException e){
			logger.debug("Find site not exist,siteId is {},exception is {}", id,e.getMessage());
		}
		
		return site;
	}
	
	static class SiteMapper implements RowMapper<Site>{

		@Override
		public Site mapRow(ResultSet rs, int rowNum) throws SQLException {
			 Site site = new Site();
			 
			 site.setId(rs.getLong("id"));
			 site.setExtraFile(rs.getString("extrafile"));
			 site.setMetaDescripe(rs.getString("metadescripe"));
			 site.setMetaKey(rs.getString("metakey"));
			 site.setResourceDir(rs.getString("resourcedir"));
			 site.setSiteName(rs.getString("sitename"));
			 site.setSiteRoot(rs.getString("siteroot"));
			 site.setSiteURL(rs.getString("siteurl"));
			 site.setHostName(rs.getString("hostname"));
			 site.setUserName(rs.getString("username"));
			 site.setPassword(rs.getString("password"));
			 site.setPath(rs.getString("path"));
			 site.setPort(rs.getString("port"));
			 
			 String d = rs.getString("outputtype");
			 if(StringUtils.isNotBlank(d)){
				 site.setDeploy(Deploy.valueOf(d));	 
			 }
			 
			 return site;
		}
	}
}

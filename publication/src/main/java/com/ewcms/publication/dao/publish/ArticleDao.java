package com.ewcms.publication.dao.publish;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.dao.constant.ArticleStatus;
import com.ewcms.publication.module.Article;
import com.ewcms.publication.module.ArticleInfo;

@Repository
public class ArticleDao implements ArticleDaoable{
	private static final String PRERELEASE_STATUS = ArticleStatus.PRERELEASE.name();
	private static final String RELEASE_STATUS = ArticleStatus.RELEASE.name();
	
	private static final String FIND_PRE_PUBLISH_BY_IDS_SQL = "SELECT t1.*, t2.page, t2.detail FROM content_article t1,content_content t2 "
			+ " WHERE t1.id = t2.article_id AND t1.id IN (:ids) AND t1.delete = FALSE ";
	private static final String FIND_PRE_PUBLISH_COUNT_SQL = "SELECT COUNT(t1.id) FROM content_article t1,content_content t2,content_article_main t3 "
			+ " WHERE t1.id = t2.article_id AND t1.id = t3.article_id AND t3.channel_id = :channelId AND t1.delete = FALSE AND t1.status IN (:status)";
	private static final String FIND_PRE_PUBLISH_SQL = "SELECT t1.*, t2.page, t2.detail FROM content_article t1,content_content t2,content_article_main t3 "
			+ " WHERE t1.id = t2.article_id AND t1.id = t3.article_id AND t3.channel_id = :channelId AND t1.delete = FALSE AND t1.status IN (:status) AND t1.id < :id "
			+ " ORDER BY t1.id DESC LIMIT :limit";
	private static final String FIND_PUBLISH_COUNT_SQL = "SELECT COUNT(t1.id) FROM content_article t1,content_article_main t2 "
			+ " WHERE t1.id = t2.article_id AND t2.channel_id = :channelId AND t1.delete = FALSE AND t1.status = :status";
	private static final String FIND_PUBLISH_SQL = "SELECT t1.* FROM content_article t1,content_article_main t2 "
			+ " WHERE t1.id = t2.article_id AND t2.channel_id = :channelId AND t1.delete = FALSE AND t1.status = :status "
			+ " ORDER BY t2.sort ASC,t1.createtime DESC OFFSET :offset LIMIT :limit ";
	private static final String FIND_PUBLISH_TOP_SQL = "SELECT t1.* FROM content_article t1,content_article_main t2 "
			+ " WHERE t1.id = t2.article_id AND t2.channel_id = :channelId AND t1.delete = FALSE AND t1.status = :status "
			+ " AND t2.top = :top ORDER BY t2.sort ASC,t1.createtime DESC OFFSET :offset LIMIT :limit ";
	private static final String FIND_PUBLISH_BY_IDS_SQL = "SELECT * FROM content_article "
			+ "WHERE id IN (:ids) AND delete = FALSE AND status = :status";
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public List<Article> findPrePublish(List<Long> ids) {
		SqlParameterSource params = new MapSqlParameterSource().addValue("ids", ids);
		return jdbcTemplate.query(FIND_PRE_PUBLISH_BY_IDS_SQL, params, new ArticleMapper());
	}

	@Override
	public int findPrePublishCount(Long channelId, Boolean forceAgain) {
		String[] status = forceAgain ? 	new String[]{PRERELEASE_STATUS, RELEASE_STATUS} : new String[]{PRERELEASE_STATUS};
		SqlParameterSource params = new MapSqlParameterSource()
		        .addValue("channelId", channelId)
		        .addValue("status", Arrays.asList(status));
		
		return jdbcTemplate.queryForObject(FIND_PRE_PUBLISH_COUNT_SQL, params, Integer.class);
	}

	@Override
	public List<Article> findPrePublish(Long channelId, Boolean forceAgain, Long startId, Integer limit) {
		String[] status = forceAgain ? 	new String[]{PRERELEASE_STATUS, RELEASE_STATUS} : new String[]{PRERELEASE_STATUS};
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("channelId", channelId).
				addValue("status", Arrays.asList(status)).
				addValue("id", startId).
				addValue("limit", limit);
		
		return jdbcTemplate.query(FIND_PRE_PUBLISH_SQL, params, new ArticleMapper());
	}
	
	@Override
	public int findPublishCount(Long channelId) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("channelId", channelId).
				addValue("status", RELEASE_STATUS);
		
		return jdbcTemplate.queryForObject(FIND_PUBLISH_COUNT_SQL , params, Integer.class);
	}

	@Override
	public List<ArticleInfo> findPublish(Long channelId, Integer page,Integer row, Boolean top) {
		int offset = page * row;
		MapSqlParameterSource params = new MapSqlParameterSource().
				addValue("channelId", channelId).
				addValue("status", RELEASE_STATUS).
				addValue("offset", offset).
				addValue("limit", row);
		
		String sql = FIND_PUBLISH_SQL;
		if(top != null){
			sql = FIND_PUBLISH_TOP_SQL;
			params.addValue("top", top);
		}
		
		return jdbcTemplate.query(sql , params, new ArticleInfoMapper());
	}

	@Override
	public List<ArticleInfo> findPublish(List<Long> ids) {
		SqlParameterSource params = new MapSqlParameterSource().
				addValue("ids", ids).
				addValue("status", RELEASE_STATUS);
		
		return jdbcTemplate.query(FIND_PUBLISH_BY_IDS_SQL, params, new ArticleInfoMapper());
	}
	
	static class ArticleMapper implements RowMapper<Article>{

		@Override
		public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
			Article a = new Article();
			
			a.setAuthor(rs.getString("author"));
			a.setComment(rs.getBoolean("comment"));
			a.setCreateTime(rs.getDate("createtime"));
			a.setModified(rs.getDate("modified"));
			a.setPublished(rs.getDate("published"));
			a.setId(rs.getLong("id"));
			a.setImage(rs.getString("image"));
			a.setInside(rs.getBoolean("inside"));
			a.setKeyword(rs.getString("key_word"));
			a.setOrigin(rs.getString("origin"));
			a.setOwner(rs.getString("owner"));
			a.setShortTitle(rs.getString("short_title"));
			a.setShortTitleStyle(rs.getString("short_title_style"));
			a.setSubTitle(rs.getString("sub_title"));
			a.setSubTitleStyle(rs.getString("sub_title_style"));
			a.setSummary(rs.getString("summary"));
			a.setTag(rs.getString("tag"));
			a.setTitle(rs.getString("title"));
			a.setTitleStyle(rs.getString("title_style"));
			a.setTotalPage(rs.getInt("total"));
			a.setUrl(rs.getString("url"));
			a.setPage(rs.getInt("page"));
			a.setContent(getContent(rs.getBytes("detail"),Charset.forName("UTF-8")));
			
			return a;
		}
		
		private String getContent(byte[] c,Charset charset){
			return c == null ? "": new String(c,charset);
		}
	}
	
	static class ArticleInfoMapper implements RowMapper<ArticleInfo>{

		@Override
		public ArticleInfo mapRow(ResultSet rs, int rowNum)throws SQLException {
			ArticleInfo a = new ArticleInfo();
			
			a.setAuthor(rs.getString("author"));
			a.setComment(rs.getBoolean("comment"));
			a.setCreateTime(rs.getDate("createtime"));
			a.setModified(rs.getDate("modified"));
			a.setPublished(rs.getDate("published"));
			a.setId(rs.getLong("id"));
			a.setImage(rs.getString("image"));
			a.setKeyword(rs.getString("key_word"));
			a.setOrigin(rs.getString("origin"));
			a.setOwner(rs.getString("owner"));
			a.setShortTitle(rs.getString("short_title"));
			a.setSubTitle(rs.getString("sub_title"));
			a.setSummary(rs.getString("summary"));
			a.setTag(rs.getString("tag"));
			a.setTitle(rs.getString("title"));
			a.setUrl(rs.getString("url"));
			
			return a;
		}
	}
}

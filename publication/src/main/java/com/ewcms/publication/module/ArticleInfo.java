package com.ewcms.publication.module;

import java.util.Date;

/**
 * 文章信息，不包括文章内容
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>title:标题</li>
 * <li>shortTitle:短标题</li>
 * <li>subTitle:副标题</li>
 * <li>author:作者</li>
 * <li>origin:来源</li>
 * <li>keyword:关键字</li>
 * <li>tag:标签</li>
 * <li>summary:摘要</li>
 * <li>image:文章图片</li>
 * <li>comment:允许评论</li>
 * <li>type:文章类型</li>
 * <li>modified:修改时间</li>
 * <li>url:链接地址</li>
 * <li>owner:创建者</li>
 * <li>relations:相关文章</li>
 * <li>createTime:创建时间</li>
 * <li>published:发布时间</li>
 * </ul>
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class ArticleInfo {
	
	private Long id;
	private String title;
	private String shortTitle;
	private String subTitle;
	private String author;
	private String origin;
	private String keyword;
	private String tag;
	private String summary;
	private String image;
	private Boolean comment;
	private String owner;
	private String url;
	private Date createTime;
	private Date modified;
	private Date published;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Boolean getComment() {
		return comment;
	}
	public void setComment(Boolean comment) {
		this.comment = comment;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Date getPublished() {
		return published;
	}
	public void setPublished(Date published) {
		this.published = published;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticleInfo other = (ArticleInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SmallArticle [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", shortTitle=");
		builder.append(shortTitle);
		builder.append(", subTitle=");
		builder.append(subTitle);
		builder.append(", author=");
		builder.append(author);
		builder.append(", origin=");
		builder.append(origin);
		builder.append(", keyword=");
		builder.append(keyword);
		builder.append(", tag=");
		builder.append(tag);
		builder.append(", summary=");
		builder.append(summary);
		builder.append(", image=");
		builder.append(image);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", owner=");
		builder.append(owner);
		builder.append(", url=");
		builder.append(url);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", modified=");
		builder.append(modified);
		builder.append(", published=");
		builder.append(published);
		builder.append("]");
		return builder.toString();
	}
}

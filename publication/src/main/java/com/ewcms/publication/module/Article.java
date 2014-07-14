/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.module;

import java.util.Date;
import java.util.List;

/**
 * 文章
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>title:标题</li>
 * <li>titleStyle:标题样式</li>
 * <li>shortTitle:短标题</li>
 * <li>shortTitleStyle:短标题样式</li>
 * <li>subTitle:副标题</li>
 * <li>subTitlStyle:副标题样式</li>
 * <li>author:作者</li>
 * <li>origin:来源</li>
 * <li>keyword:关键字</li>
 * <li>tag:标签</li>
 * <li>summary:摘要</li>
 * <li>contents:内容集合对象</li>
 * <li>image:文章图片</li>
 * <li>comment:允许评论</li>
 * <li>type:文章类型</li>
 * <li>owner:创建者</li>
 * <li>published:发布时间</li>
 * <li>modified:修改时间</li>
 * <li>status:状态</li>
 * <li>url:链接地址</li>
 * <li>delete:删除标志</li>
 * <li>relations:相关文章</li>
 * <li>createTime:创建时间</li>
 * <li>categories:文章分类属性集合</li>
 * <li>totalPage:内容总页数</li>
 * <li>inside:使用内部标题</li>
 * </ul>
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class Article {

	private Long id;
	private String title;
	private String titleStyle;
	private String shortTitle;
	private String shortTitleStyle;
	private String subTitle;
	private String subTitleStyle;
	private String author;
	private String origin;
	private String keyword;
	private String tag;
	private String summary;
	private String content;
	private String image;
	private Boolean comment;
	private String owner;
	private String url;
	private Integer page;
	private Integer totalPage;
	private Boolean inside;
	private List<Integer> relations;
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

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer contentTotal) {
		this.totalPage = contentTotal;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String detail) {
		this.content = detail;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Boolean getInside() {
		return inside;
	}

	public void setInside(Boolean inside) {
		this.inside = inside;
	}

	public String getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(String titleStyle) {
		this.titleStyle = titleStyle;
	}

	public String getShortTitleStyle() {
		return shortTitleStyle;
	}

	public void setShortTitleStyle(String shortTitleStyle) {
		this.shortTitleStyle = shortTitleStyle;
	}

	public String getSubTitleStyle() {
		return subTitleStyle;
	}

	public void setSubTitleStyle(String subTitleStyle) {
		this.subTitleStyle = subTitleStyle;
	}

	public List<Integer> getRelations() {
		return relations;
	}

	public void setRelations(List<Integer> relations) {
		this.relations = relations;
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

	public boolean isMain() {
		return this.page == 0;
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
		Article other = (Article) obj;
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
		builder.append("Article [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", titleStyle=");
		builder.append(titleStyle);
		builder.append(", shortTitle=");
		builder.append(shortTitle);
		builder.append(", shortTitleStyle=");
		builder.append(shortTitleStyle);
		builder.append(", subTitle=");
		builder.append(subTitle);
		builder.append(", subTitleStyle=");
		builder.append(subTitleStyle);
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
		builder.append(", detail=");
		builder.append(content);
		builder.append(", image=");
		builder.append(image);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", owner=");
		builder.append(owner);
		builder.append(", url=");
		builder.append(url);
		builder.append(", page=");
		builder.append(page);
		builder.append(", totalPage=");
		builder.append(totalPage);
		builder.append(", inside=");
		builder.append(inside);
		builder.append(", relations=");
		builder.append(relations);
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

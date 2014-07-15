/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.module;

/**
 * <ul>
 * <li>id:栏目编号</li>
 * <li>name: 栏目名称</li>
 * <li>dir: 栏目目录</li>
 * <li>pubPath:栏目发布路径</li>
 * <li>url:栏目域名</li>
 * <li>absUrl:栏目访问地址</li>
 * <li>listSize:列表显示最大条数</li>
 * <li>maxSize:最大显示条数</li>
 * <li>describe:专栏说明</li>
 * <li>channelEntity:专栏引导图</li>
 * <li>channelType:专栏类型</li>
 * <li>sort:排序号</li>
 * <li>appChannel:应用于频道</li>
 * </ul>
 * 
 * @author 王伟
 * @version 2014-06-12
 */
public class Channel {
	private Long id;
	private String name;
	private String dir;
	private String url;
	private String absUrl;
	private String iconUrl;
	private String pubPath;
	private Integer listSize = 20;
	private Integer maxSize = 9999;
	private String describe;
	private Long parentId;
	private String appChannel;
	private Long siteId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDir() {
		return dir == null ? "" : dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getUrl() {
		return url == null ? "" : url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAbsUrl() {
		return absUrl;
	}

	public void setAbsUrl(String realDir) {
		this.absUrl = realDir;
	}

	public String getPubPath() {
		return pubPath;
	}

	public void setPubPath(String pubPath) {
		this.pubPath = pubPath;
	}

	public Integer getListSize() {
		return listSize;
	}

	public void setListSize(Integer listSize) {
		this.listSize = listSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
 
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAppChannel() {
		return appChannel;
	}

	public void setAppChannel(String appChannel) {
		this.appChannel = appChannel;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Channel other = (Channel) obj;
		if (this.id != other.id
				&& (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Channel [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", dir=");
		builder.append(dir);
		builder.append(", url=");
		builder.append(url);
		builder.append(", absUrl=");
		builder.append(absUrl);
		builder.append(", iconUrl=");
		builder.append(iconUrl);
		builder.append(", pubPath=");
		builder.append(pubPath);
		builder.append(", listSize=");
		builder.append(listSize);
		builder.append(", maxSize=");
		builder.append(maxSize);
		builder.append(", describe=");
		builder.append(describe);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", appChannel=");
		builder.append(appChannel);
		builder.append(", siteId=");
		builder.append(siteId);
		builder.append("]");
		return builder.toString();
	}
}

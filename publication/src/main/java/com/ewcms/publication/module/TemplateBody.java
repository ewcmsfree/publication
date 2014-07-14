/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

/**
 * 
 */
package com.ewcms.publication.module;


/**
 * <ul>
 * <li>id:编号</li>
 * <li>content: 模板类容</li>
 * <li>uniquePath:模板唯一路径（FreeMarker模板加载路径）</li>
 * <li>updateTime:模板最后修改时间</li>
 * </ul>
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class TemplateBody {
	
	private Integer id;
	private String uniquePath;
	private byte[] body;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUniquePath() {
		return uniquePath;
	}

	public void setUniquePath(String uniquePath) {
		this.uniquePath = uniquePath;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] content) {
		this.body = content;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TemplateBody other = (TemplateBody) obj;
		if (this.id != other.id
				&& (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 61 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Template [id=");
		builder.append(id);
		builder.append(", uniquePath=");
		builder.append(uniquePath);
		builder.append("]");
		return builder.toString();
	}
}

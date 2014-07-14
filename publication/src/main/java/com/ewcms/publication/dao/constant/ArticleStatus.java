package com.ewcms.publication.dao.constant;

/**
 * 文章状态
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public enum ArticleStatus {
	DRAFT("初稿"),
	REEDIT("重新编辑"),
	REVIEW("审核中"),
	REVIEWBREAK("审核中断"), 
	PRERELEASE("发布版"),
	RELEASE("已发布");

	private String remark;

	private ArticleStatus(String description) {
		this.remark = description;
	}

	public String getRemark() {
		return remark;
	}
}

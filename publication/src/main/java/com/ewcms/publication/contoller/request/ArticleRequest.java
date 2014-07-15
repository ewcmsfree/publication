package com.ewcms.publication.contoller.request;

import java.util.List;

public class ArticleRequest extends SiteRequest{
	private Long channelId;
	private List<Long> ids;

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ArticleRequest [ids=");
		builder.append(ids);
		builder.append(", getSiteId()=");
		builder.append(getSiteId());
		builder.append(", isAgain()=");
		builder.append(isAgain());
		builder.append("]");
		return builder.toString();
	}
}

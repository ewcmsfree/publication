package com.ewcms.publication.contoller.request;

public class ChannelRequest extends SiteRequest {
	private Long channelId;
	private boolean child;

	public Long getChannelId() {
		return channelId;
	}
	
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	
	public void setChild(boolean child) {
		this.child = child;
	}
	
	public boolean isChild() {
		return child;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChannelRequest [channelId=");
		builder.append(channelId);
		builder.append(", child=");
		builder.append(child);
		builder.append(", getSiteId()=");
		builder.append(getSiteId());
		builder.append(", isAgain()=");
		builder.append(isAgain());
		builder.append("]");
		return builder.toString();
	}
	
}

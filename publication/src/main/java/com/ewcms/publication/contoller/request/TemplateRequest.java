package com.ewcms.publication.contoller.request;

public class TemplateRequest extends SiteRequest {
	private int channelId;
	private int templateId;

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TemplateRequest [channelId=");
		builder.append(channelId);
		builder.append(", templateId=");
		builder.append(templateId);
		builder.append(", getSiteId()=");
		builder.append(getSiteId());
		builder.append(", isAgain()=");
		builder.append(isAgain());
		builder.append("]");
		return builder.toString();
	}
}

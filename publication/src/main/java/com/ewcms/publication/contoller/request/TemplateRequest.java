package com.ewcms.publication.contoller.request;

public class TemplateRequest extends SiteRequest {
	private Long channelId;
	private Long templateId;

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
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

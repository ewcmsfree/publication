package com.ewcms.publication.contoller.request;

public class SiteRequest {
	private int siteId;
	private boolean again;
	
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public boolean isAgain() {
		return again;
	}
	public void setAgain(boolean again) {
		this.again = again;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteRequest [siteId=");
		builder.append(siteId);
		builder.append(", again=");
		builder.append(again);
		builder.append("]");
		return builder.toString();
	}	
}

package com.ewcms.publication.module;


public class TemplateSource {
	private Long id;
    private String path;
	private String uniquePath;
	private byte[] content;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getUniquePath() {
		return uniquePath;
	}
	public void setUniquePath(String uniquePath) {
		this.uniquePath = uniquePath;
	}
	
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
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
		TemplateSource other = (TemplateSource) obj;
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
		builder.append("TemplateSource [id=");
		builder.append(id);
		builder.append(", path=");
		builder.append(path);
		builder.append(", uniquePath=");
		builder.append(uniquePath);
		builder.append("]");
		return builder.toString();
	}
}

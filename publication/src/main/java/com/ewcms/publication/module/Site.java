/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.module;

/**
 * <ul>
 * <li>id:站点编号
 * <li>siteName:站点名称
 * <li>siteRoot:站点目录
 * <li>siteURL: 站点访问URL地址
 * <li>metaKey:meta搜索关键字
 * <li>metaDescripe:meta关键字说明
 * <li>extraFile:生成扩展文件名称
 * <li>siteServer:站点发布服务器信息对象
 * <li>resourceDir:站点资源发布 绝对目录
 * </ul>
 * 
 * @author 王伟
 * @version 2014-06-12
 */
public class Site {
	
	public enum Deploy {
		 LOCAL,SFTP,FTP,FTPS,SMB;
	}
	
	private Long id;
	private String siteName;
	private String siteRoot;
	private String siteURL;
	private String metaKey;
	private String metaDescripe;
	private String extraFile;
	private String resourceDir;
	private String path;
	private String hostName;
	private String port;
	private String userName;
	private String password;
	private Deploy deploy = Deploy.LOCAL;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteRoot() {
		return siteRoot;
	}

	public void setSiteRoot(String siteRoot) {
		this.siteRoot = siteRoot;
	}

	public String getSiteURL() {
		return siteURL;
	}

	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

	public String getMetaKey() {
		return metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	public String getMetaDescripe() {
		return metaDescripe;
	}

	public void setMetaDescripe(String metaDescripe) {
		this.metaDescripe = metaDescripe;
	}

	public String getExtraFile() {
		return extraFile;
	}

	public void setExtraFile(String extraFile) {
		this.extraFile = extraFile;
	}

	public String getResourceDir() {
		return resourceDir;
	}

	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Deploy getDeploy() {
		return deploy;
	}

	public void setDeploy(Deploy server) {
		this.deploy = server;
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
		Site other = (Site) obj;
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
		builder.append("Site [id=");
		builder.append(id);
		builder.append(", siteName=");
		builder.append(siteName);
		builder.append(", siteRoot=");
		builder.append(siteRoot);
		builder.append(", siteURL=");
		builder.append(siteURL);
		builder.append(", metaKey=");
		builder.append(metaKey);
		builder.append(", metaDescripe=");
		builder.append(metaDescripe);
		builder.append(", extraFile=");
		builder.append(extraFile);
		builder.append(", resourceDir=");
		builder.append(resourceDir);
		builder.append(", path=");
		builder.append(path);
		builder.append(", hostName=");
		builder.append(hostName);
		builder.append(", port=");
		builder.append(port);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", deploy=");
		builder.append(deploy);
		builder.append("]");
		return builder.toString();
	}
}

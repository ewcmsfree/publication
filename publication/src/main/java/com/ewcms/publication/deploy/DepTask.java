package com.ewcms.publication.deploy;

/**
 * 发布任务
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class DepTask {
	
	public enum DepTaskType{
		DETAIL,LIST,HOME,RESOURCE,TEMPLATESOURCE,OTHER;
	}
	
	private final String taskId;
	private final DepTaskType type;
	private final String source;
	private final String uri;
	private final String path;
	private final boolean finish;
	private final Long markId;
	private final boolean error;
	private final String remark;
	private final String exception;
	
	private DepTask(String taskId, DepTaskType type,
			String source, String uri,String path,Long markId, boolean finish,
			boolean error,String remark,String exception){
		
		this.taskId = taskId;
		this.type = type;
		this.source = source;
		this.uri = uri;
		this.path = path;
		this.finish = finish;
		this.markId = markId;
		this.error = error;
		this.remark = remark;
		this.exception = exception;
	}

	public String getTaskId(){
		return this.taskId;
	}
	
	public String getSource() {
		return source;
	}

	public String getUri() {
		return uri;
	}
	
	public String getPath() {
		return path;
	}

	public DepTaskType getType(){
		return this.type;
	}
	
	public Long getMarkId(){
		return this.markId;
	}
	
	public boolean isFinish(){
		return this.finish;
	}
	
	public boolean isError() {
		return error;
	}

	public String getRemark() {
		return remark;
	}

	public String getException() {
		return exception;
	}

	public static DepTask finish(String taskId){
		return new DepTask(taskId, null, null , null,null, -1l, true, false, null,null);
	}
	
	public static DepTask finish(String taskId, String source){
		return new DepTask(taskId, null, source, null,null, -1l, true, false, null,null);
	}
	
	public static DepTask success(String taskId,DepTaskType type,String source, String uri,String path){
		return new DepTask(taskId, type, source, uri, path, -1L, false,false, null, null);
	}
	
	public static DepTask success(String taskId,DepTaskType type,String source, String uri,String path, Long markId){
		return new DepTask(taskId, type, source, uri, path, markId, false,false, null, null);
	}
	
	public static DepTask error(String taskId,String remark,String exception){
		return new DepTask(taskId, null, null, null,null, -1l, false, true, remark,exception);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DepTask [taskId=");
		builder.append(taskId);
		builder.append(", type=");
		builder.append(type);
		builder.append(", source=");
		builder.append(source);
		builder.append(", uri=");
		builder.append(uri);
		builder.append(", path=");
		builder.append(path);
		builder.append(", finish=");
		builder.append(finish);
		builder.append(", markId=");
		builder.append(markId);
		builder.append(", error=");
		builder.append(error);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", exception=");
		builder.append(exception);
		builder.append("]");
		return builder.toString();
	}
}

package com.ewcms.publication.contoller.response;

public class PublishResponse {
	private int code = 0;
	private boolean success = true;
	private String message;
	private long time = System.currentTimeMillis();
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public static PublishResponse success(){
		return new PublishResponse();
	}
	
	public static PublishResponse success(String message){
		PublishResponse r = new PublishResponse();
		r.setMessage(message);

		return r;
	}
	
	public static PublishResponse fail(int code,String message){
		PublishResponse r = new PublishResponse();
		r.setCode(code);
		r.setSuccess(false);
		r.setMessage(message);
		
		return r;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PublishResponse [code=");
		builder.append(code);
		builder.append(", success=");
		builder.append(success);
		builder.append(", message=");
		builder.append(message);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}
}

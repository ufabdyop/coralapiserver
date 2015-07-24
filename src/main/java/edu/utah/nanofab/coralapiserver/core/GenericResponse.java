package edu.utah.nanofab.coralapiserver.core;

public class GenericResponse {
    boolean success;
	String message;
	public GenericResponse() {
		success = false;
		message = "";
	}
	public GenericResponse(boolean isSuccess) {
		this.setSuccess(isSuccess);
		this.setMessage("");
	}
	public GenericResponse(boolean isSuccess, String message) {
		this.setSuccess(isSuccess);
		this.setMessage(message);
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
}

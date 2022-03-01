package helio.rest.model;

import com.google.gson.annotations.Expose;

import helio.rest.RestUtils;

public class ApiError {

	@Expose
	private int code;
	@Expose
	private String message;

	public ApiError() {
		super();
	}

	public ApiError(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return RestUtils.toJson(this);
	}
}
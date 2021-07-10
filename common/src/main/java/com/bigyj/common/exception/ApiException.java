package com.bigyj.common.exception;

public class ApiException extends RuntimeException {
	private final String code;

	public ApiException(String code) {
		super();
		this.code = code;
	}


	public ApiException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ApiException(String code, String message, Object... args) {
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}


package com.bigyj.common.entity;

import java.io.Serializable;

public class ApiResult<T> implements Serializable {
	private String code ;
	private String msg ;
	private T data ;

	public ApiResult() {
	}

	public ApiResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ApiResult(String code, T data) {
		this.code = code;
		this.data = data;
	}

	public ApiResult(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}


}

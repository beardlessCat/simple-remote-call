package com.bigyj.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApiResultCode {
	SUCCESS(20000,"成功"),
	ERROR(20001,"失败");
	private Integer code;
	private String msg;

	Integer getCode() {
		return code;
	}

	void setCode(Integer code) {
		this.code = code;
	}

	String getMsg() {
		return msg;
	}

	void setMsg(String msg) {
		this.msg = msg;
	}
}

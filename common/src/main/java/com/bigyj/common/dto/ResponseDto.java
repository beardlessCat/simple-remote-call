package com.bigyj.common.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
	private T data ;
	private String code ;
	private String msg ;
}

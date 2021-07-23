package com.bigyj.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RequestTemplate implements Serializable{

	/**
	 * 请求header
	 */
	private  Map<String, Object> headers = new HashMap<>();

}

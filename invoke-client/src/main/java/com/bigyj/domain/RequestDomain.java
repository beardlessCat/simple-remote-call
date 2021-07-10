package com.bigyj.domain;

import lombok.Data;

import org.springframework.http.HttpMethod;

@Data
public class RequestDomain {
	private String value;
	private HttpMethod  method;
	private boolean withAccessToken;
	private int maxAttempts ;
}

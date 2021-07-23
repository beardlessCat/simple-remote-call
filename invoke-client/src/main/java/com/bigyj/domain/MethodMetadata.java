package com.bigyj.domain;

import java.util.List;

import com.bigyj.interceptor.RequestInterceptor;
import lombok.Data;

import org.springframework.http.HttpMethod;
@Data
public class MethodMetadata {
	private String value;
	private HttpMethod method;
	private boolean withAccessToken;
	private int maxAttempts ;
	private List<RequestInterceptor> requestInterceptors ;
}

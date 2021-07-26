package com.bigyj.config;

import com.bigyj.interceptor.HeaderRequestInterceptor;
import com.bigyj.interceptor.RequestInterceptor;

import org.springframework.context.annotation.Bean;

public class RemoteCallConfig {
	@Bean
	public RequestInterceptor remoteCallRequestInterceptor(){
		return new HeaderRequestInterceptor();
	}
}

package com.bigyj.config;

import com.bigyj.interceptor.RemoteCallRequestInterceptor;
import com.bigyj.interceptor.RequestInterceptor;

import org.springframework.context.annotation.Bean;

public class RemoteCallConfig {
	@Bean
	public RequestInterceptor remoteCallRequestInterceptor(){
		return new RemoteCallRequestInterceptor();
	}
}

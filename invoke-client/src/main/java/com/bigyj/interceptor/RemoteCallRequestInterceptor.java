package com.bigyj.interceptor;

import com.bigyj.domain.RequestTemplate;

public class RemoteCallRequestInterceptor implements RequestInterceptor{
	@Override
	public void apply(RequestTemplate requestTemplate) {
		System.out.printf("拦截器执行......");
	}
}

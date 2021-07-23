package com.bigyj.interceptor;

public class RemoteCallRequestInterceptor implements RequestInterceptor{
	@Override
	public void apply(RequestTemplate requestTemplate) {
		System.out.printf("拦截器执行......");
	}
}

package com.bigyj.interceptor;

import com.bigyj.domain.RequestTemplate;

public interface RequestInterceptor {
	void apply(RequestTemplate requestTemplate);
}

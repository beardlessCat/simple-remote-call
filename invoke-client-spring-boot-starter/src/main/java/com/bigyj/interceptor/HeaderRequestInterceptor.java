package com.bigyj.interceptor;


import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bigyj.domain.RequestTemplate;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * header拦截器，获取原始请求中的header信息，赋值给RequestTemplate对象，用于feign调用
 */
public class HeaderRequestInterceptor implements RequestInterceptor{
	@Override
	public void apply(RequestTemplate requestTemplate) {
		//RequestContextHolder 通过RequestContextHolder#getRequestAttributes()方法获取请求对象
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			Map<String, String> headers = requestTemplate.getHeaders();
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				headers.put(key, value);
			}
			requestTemplate.setHeaders(headers);
		}
	}
}

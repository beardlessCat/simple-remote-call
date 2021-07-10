package com.bigyj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bigyj.common.exception.ApiException;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String authorization = request.getHeader("Authorization");
		if(StringUtils.isEmpty(authorization)){
			throw new ApiException("403","无权访问！");
		}
		return true;
	}
}

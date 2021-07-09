package com.bigyj.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Function;

import com.bigyj.annotation.InvokeRequest;
import com.bigyj.config.RemoteConfig;
import com.bigyj.hanlder.method.HttpMethodHandler;
import com.bigyj.hanlder.method.MethodHandler;
import lombok.Data;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;

@Data
public class MethodHandlerFactory {
	private RestTemplateBuilder restTemplateBuilder;
	private RemoteConfig remoteConfig;
	private String clientPath ;
	public MethodHandlerFactory(RestTemplateBuilder restTemplateBuilder, RemoteConfig remoteConfig) {
		this.restTemplateBuilder = restTemplateBuilder;
		this.remoteConfig = remoteConfig;
	}

	/**
	 * 抽取方法上的注解信息
	 * @param method
	 * @return
	 */
	public MethodHandler fromMethod(Method method) {
		InvokeRequest request = AnnotatedElementUtils.findMergedAnnotation(method, InvokeRequest.class);
		HttpMethod httpMethod = request.method();
		String requestPath = request.path();
		Type returnType = method.getGenericReturnType();
		boolean withAccessToken = request.withAccessToken();
		return this.createMethodHandler(httpMethod,requestPath,withAccessToken,returnType,restTemplateBuilder,
				args -> {
					Object arg = args[0];
					return arg;
				});
	}
	private MethodHandler createMethodHandler(HttpMethod httpMethod,String  requestPath, boolean withAccessToken,Type returnType,RestTemplateBuilder restTemplateBuilder, Function<Object[], Object> requestWrapper){
		return new HttpMethodHandler(remoteConfig.getRemoteUrl()+clientPath+requestPath,httpMethod,requestPath,withAccessToken,returnType ,requestWrapper,restTemplateBuilder.build());

	}
}

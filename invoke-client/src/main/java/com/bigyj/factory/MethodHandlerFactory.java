package com.bigyj.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.bigyj.annotation.InvokeRequest;
import com.bigyj.config.RemoteConfig;
import com.bigyj.domain.RequestDomain;
import com.bigyj.hanlder.method.HttpMethodHandler;
import com.bigyj.hanlder.method.MethodHandler;
import com.bigyj.supplier.AccessTokenSupplier;
import lombok.Data;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.annotation.AnnotatedElementUtils;

@Data
public class MethodHandlerFactory {
	private RestTemplateBuilder restTemplateBuilder;
	private RemoteConfig remoteConfig;
	private String clientPath ;
	private AccessTokenSupplier accessTokenSupplier;
	public MethodHandlerFactory(RestTemplateBuilder restTemplateBuilder, RemoteConfig remoteConfig,AccessTokenSupplier accessTokenSupplier) {
		this.restTemplateBuilder = restTemplateBuilder;
		this.remoteConfig = remoteConfig;
		this.accessTokenSupplier = accessTokenSupplier ;
	}

	/**
	 * 抽取方法上的注解信息
	 * @param method
	 * @return
	 */
	public MethodHandler fromMethod(Method method) {
		InvokeRequest request = AnnotatedElementUtils.findMergedAnnotation(method, InvokeRequest.class);
		RequestDomain requestDomain = new RequestDomain();
		Type returnType = method.getGenericReturnType();
		requestDomain.setValue(remoteConfig.getRemoteUrl()+clientPath+request.value());
		requestDomain.setMethod(request.method());
		requestDomain.setWithAccessToken(request.withAccessToken());
		requestDomain.setMaxAttempts(request.maxAttempts());
		return this.createMethodHandler(requestDomain,returnType,restTemplateBuilder);
	}
	private MethodHandler createMethodHandler(RequestDomain requestDomain, Type returnType,RestTemplateBuilder restTemplateBuilder){
		return new HttpMethodHandler(
				requestDomain,
				returnType,
				restTemplateBuilder.build(),
				accessTokenSupplier);
	}
}

package com.bigyj.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bigyj.ClientConfigurationcation;
import com.bigyj.annotation.InvokeRequest;
import com.bigyj.config.RemoteConfig;
import com.bigyj.domain.MethodMetadata;
import com.bigyj.domain.RequestTemplate;
import com.bigyj.hanlder.method.HttpMethodHandler;
import com.bigyj.hanlder.method.MethodHandler;
import com.bigyj.interceptor.RequestInterceptor;
import com.bigyj.supplier.AccessTokenSupplier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;

@Data
@Slf4j
public class MethodHandlerFactory {
	private RestTemplateBuilder restTemplateBuilder;
	private RemoteConfig remoteConfig;
	private String clientPath ;
	private AccessTokenSupplier accessTokenSupplier;
	private Map<String, AnnotationConfigApplicationContext> contexts = new ConcurrentHashMap();
	private List<ClientConfigurationcation> configurations ;
	private String name ;
	public MethodHandlerFactory(RestTemplateBuilder restTemplateBuilder, RemoteConfig remoteConfig,AccessTokenSupplier accessTokenSupplier) {
		this.restTemplateBuilder = restTemplateBuilder;
		this.remoteConfig = remoteConfig;
		this.accessTokenSupplier = accessTokenSupplier ;
	}

	/**
	 * 配置config
	 * @param configurations
	 */
	public void configConfigurations(List<ClientConfigurationcation> configurations){
		this.configurations = configurations ;
	}
	/**
	 * 通过config配置Context
	 */
	public void configContexts() {
		//fixme AnnotationConfigApplicationContext#register()方法的作用
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		configurations.stream().forEach(configurationcation -> {
			//区分客户端进行配置类绑定，进行不同客户端的配置隔离
			if(name.equals(configurationcation.getName())){
				Arrays.stream(configurationcation.getConfiguration()).forEach(configuration->{
					context.register(new Class[]{configuration});
				});
			}
		});
		context.refresh();
		contexts.put(name,context);
	}
	/**
	 * 抽取方法上的注解信息
	 * @param method
	 * @return
	 */
	public MethodHandler fromMethod(Method method) {
		System.out.println(contexts);
		InvokeRequest request = AnnotatedElementUtils.findMergedAnnotation(method, InvokeRequest.class);
		MethodMetadata methodMetadata = new MethodMetadata();
		Type returnType = method.getGenericReturnType();
		methodMetadata.setValue(remoteConfig.getRemoteUrl()+clientPath+request.value());
		methodMetadata.setMethod(request.method());
		methodMetadata.setWithAccessToken(request.withAccessToken());
		methodMetadata.setMaxAttempts(request.maxAttempts());
		//
		RequestTemplate requestTemplate = new RequestTemplate() ;
		return this.createMethodHandler(methodMetadata,returnType,restTemplateBuilder,this.initRequestInterceptor(),requestTemplate);
	}

	/**
	 * 创建handler对象
	 * @param methodMetadata
	 * @param returnType
	 * @param restTemplateBuilder
	 * @param requestInterceptors
	 * @param requestTemplate
	 * @return
	 */
	private MethodHandler createMethodHandler(MethodMetadata methodMetadata,
			Type returnType,
			RestTemplateBuilder restTemplateBuilder,
			List<RequestInterceptor> requestInterceptors,
			RequestTemplate requestTemplate){
		return new HttpMethodHandler(
				methodMetadata,
				returnType,
				restTemplateBuilder.build(),
				accessTokenSupplier,
				requestInterceptors,
				requestTemplate);
	}

	/**
	 * 获取AnnotationConfigApplicationContext，初始化拦截器
	 * @return
	 */
	public List<RequestInterceptor> initRequestInterceptor(){
		List<RequestInterceptor> requestInterceptors = new ArrayList<>() ;
		Map<String, RequestInterceptor> beansOfType = contexts.get(name).getBeansOfType(RequestInterceptor.class);
		beansOfType.forEach((key,value)->{
			requestInterceptors.add(value);
		});
		return requestInterceptors ;
	}
}

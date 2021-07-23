package com.bigyj;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bigyj.factory.MethodHandlerFactory;
import com.bigyj.hanlder.ClientInvocationHandler;
import com.bigyj.hanlder.method.MethodHandler;
import com.bigyj.interceptor.RequestInterceptor;
import lombok.Setter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

public class InvokeClientFactoryBean implements FactoryBean<Object>, ApplicationContextAware {
	private ApplicationContext applicationContext ;
	@Setter
	private Class<?> type;
	@Setter
	private String path;
	@Setter
	private String name ;
	/**
	 * 生成代理对象
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object getObject() {
		MethodHandlerFactory methodHandlerFactory = applicationContext.getBean(MethodHandlerFactory.class);
		Assert.notNull(methodHandlerFactory, "MethodHandlerFactory must not be null");
		Map<Method, MethodHandler> dispatch = new ConcurrentHashMap<>();
		methodHandlerFactory.setClientPath(path);
		methodHandlerFactory.setName(name);
		/**
		 * 配置Context
		 */
		methodHandlerFactory.configContexts();
		//获取接口类的全部方法;
		for (Method method : type.getMethods()) {
			if (method.getDeclaringClass() == Object.class) {
				continue;
			} else if (method.isDefault() && !method.isSynthetic()) {
				continue;
			} else {
				//通过method的注解信息，获取远程调用的信息
				dispatch.put(method, methodHandlerFactory.fromMethod(method));
			}
		}
		ClientInvocationHandler invocationHandler = new ClientInvocationHandler(dispatch);
		return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, invocationHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return this.type;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}

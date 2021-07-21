package com.bigyj;

import java.util.Map;

import com.bigyj.annotation.EnableInvokeClient;
import com.bigyj.interceptor.RequestInterceptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableInvokeClient
public class InvokeDemoApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(InvokeDemoApplication.class, args);
		Map<String, RequestInterceptor> beansOfType = context.getBeansOfType(RequestInterceptor.class);
		System.out.println(beansOfType);
		//Object remoteCallRequestInterceptor = context.getBean(RequestInterceptor.class);
		//System.out.println(remoteCallRequestInterceptor);
	}

}

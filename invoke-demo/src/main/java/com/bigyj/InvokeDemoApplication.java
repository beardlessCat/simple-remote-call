package com.bigyj;

import java.util.Arrays;
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
		Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

	}

}

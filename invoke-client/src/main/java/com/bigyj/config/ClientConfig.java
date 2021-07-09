package com.bigyj.config;

import com.bigyj.factory.MethodHandlerFactory;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

	@Bean
	MethodHandlerFactory methodHandlerFactory (RestTemplateBuilder restTemplateBuilder,RemoteConfig remoteConfig){
		return new MethodHandlerFactory(restTemplateBuilder,remoteConfig);
	}
}

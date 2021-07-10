package com.bigyj.config;

import com.bigyj.client.RestTokenClient;
import com.bigyj.factory.MethodHandlerFactory;
import com.bigyj.supplier.AccessTokenSupplier;
import com.bigyj.supplier.RedisAccessTokenSupplier;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ClientConfig {
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		//配置redisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		// 配置连接工厂
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);//key序列化
		redisTemplate.setHashKeySerializer(stringSerializer);//Hash key序列化
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	AccessTokenSupplier accessTokenSupplier (@Lazy RestTokenClient restTokenClient, RedisTemplate redisTemplate ,RemoteConfig remoteConfig){
		return new RedisAccessTokenSupplier(restTokenClient,redisTemplate,remoteConfig);
	}
	@Bean
	MethodHandlerFactory methodHandlerFactory (RestTemplateBuilder restTemplateBuilder,RemoteConfig remoteConfig ,AccessTokenSupplier accessTokenSupplier){
		return new MethodHandlerFactory(restTemplateBuilder,remoteConfig,accessTokenSupplier);
	}
}

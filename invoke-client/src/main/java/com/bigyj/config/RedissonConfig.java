package com.bigyj.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonConfig {
	private String host;
	private int connectTimeout;

	@Bean
	public RedissonClient redissonClient(){
		Config config = new Config();
		config.setTransportMode(TransportMode.NIO);
		config.useSingleServer()
				.setAddress(host)
				.setConnectTimeout(connectTimeout);
		return Redisson.create(config);
	}
}
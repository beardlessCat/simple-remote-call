package com.bigyj.config;

import com.bigyj.breaker.holder.BreakerManagerHolder;
import com.bigyj.breaker.holder.MemoryBreakerManagerHolder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BreakerCommandConfig {
	@Bean
	public BreakerManagerHolder memoryBreakerManagerHolder(BreakerConfig breakerConfig){
		return new MemoryBreakerManagerHolder(breakerConfig);
	}
}

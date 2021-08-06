package com.bigyj.config;

import com.bigyj.breaker.holder.BreakerManagerHolder;
import com.bigyj.breaker.holder.MemoryBreakerManagerHolder;
import com.bigyj.breaker.holder.RedisBreakerManagerHolder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BreakerCommandConfig {
	@Bean
	public BreakerManagerHolder memoryBreakerManagerHolder(){
		return new RedisBreakerManagerHolder();
	}
}

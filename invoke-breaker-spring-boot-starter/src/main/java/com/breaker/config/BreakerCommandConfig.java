package com.breaker.config;


import com.breaker.aspect.BreakerCommandAspect;
import com.breaker.holder.BreakerManagerHolder;
import com.breaker.holder.MemoryBreakerManagerHolder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BreakerCommandConfig {
	@Bean
	public BreakerManagerHolder memoryBreakerManagerHolder(){
		return new MemoryBreakerManagerHolder();
	}

	@Bean
	public BreakerCommandAspect breakerCommandAspect(){
		return new BreakerCommandAspect();
	}
}

package com.bigyj.config;

import com.bgiyj.holder.BreakerManagerHolder;
import com.bgiyj.holder.MemoryBreakerManagerHolder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BreakerCommandConfig {
	@Bean
	public BreakerManagerHolder memoryBreakerManagerHolder(){
		return new MemoryBreakerManagerHolder();
	}
}

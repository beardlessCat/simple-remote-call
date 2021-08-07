package com.bigyj.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "breaker")
public class BreakerConfig {
	private int maxOpenToTryTime;
	private int maxFailCount;
	private int maxSuccessCount;
	private int maxOpenRetryCount;
}

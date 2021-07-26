package com.bigyj.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "remote.config")
public class RemoteConfig {
	private String secret ;
	private String clientId ;
	private String remoteUrl;
}

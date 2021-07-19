package com.bigyj;

import lombok.Setter;

public class ClientConfigurationcation {
	@Setter
	private String name ;
	@Setter
	private Class<?>[] configuration;

	ClientConfigurationcation(String name, Class<?>[] configuration) {
		this.name = name;
		this.configuration = configuration;
	}

	ClientConfigurationcation() {
	}
}

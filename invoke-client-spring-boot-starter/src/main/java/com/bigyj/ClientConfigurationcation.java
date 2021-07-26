package com.bigyj;

import java.beans.ConstructorProperties;

import lombok.Data;

@Data
public class ClientConfigurationcation{

	private String name ;

	private Class<?>[] configuration;

	@ConstructorProperties({"name", "configuration"})
	public ClientConfigurationcation(String name, Class<?>[] configuration) {
		this.name = name;
		this.configuration = configuration;
	}

	public ClientConfigurationcation() {
	}
}

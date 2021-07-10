package com.bigyj.client;

import com.bigyj.annotation.InvokeClient;
import com.bigyj.annotation.InvokeRequest;

import org.springframework.http.HttpMethod;

@InvokeClient(value = "/v1/api")
public interface RequestClient {
	@InvokeRequest(value = "/hello",method = HttpMethod.GET,maxAttempts = 5)
	public String hello(String name);
}

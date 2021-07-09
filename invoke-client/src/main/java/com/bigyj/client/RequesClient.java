package com.bigyj.client;

import com.bigyj.annotation.InvokeClient;
import com.bigyj.annotation.InvokeRequest;

import org.springframework.http.HttpMethod;

@InvokeClient(value = "/v1/api")
public interface RequesClient {
	@InvokeRequest(value = "/hello",withAccessToken = false,method = HttpMethod.GET)
	public String hello();
}

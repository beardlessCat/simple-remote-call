package com.bigyj.client;

import com.bigyj.annotation.InvokeClient;
import com.bigyj.annotation.InvokeRequest;

import org.springframework.http.HttpMethod;

@InvokeClient(value = "/v1/api")
public interface RequestClient {
	@InvokeRequest(value = "/hello",withAccessToken = false,method = HttpMethod.GET)
	public String hello();
	@InvokeRequest(value = "/delete",method = HttpMethod.DELETE)
	public String delete();
	@InvokeRequest(value = "/add",withAccessToken = false,method = HttpMethod.POST)
	public String add();
}

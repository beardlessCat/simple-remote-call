package com.bigyj.client;

import com.bigyj.annotation.InvokeClient;
import com.bigyj.annotation.InvokeRequest;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.User;

import org.springframework.http.HttpMethod;

@InvokeClient(value = "/v1/api",name = "requestClient")
public interface RequestClient {
	@InvokeRequest(value = "/queryUser",method = HttpMethod.POST,maxAttempts = 5)
	public ResponseDto<User> queryUser(User user);
}

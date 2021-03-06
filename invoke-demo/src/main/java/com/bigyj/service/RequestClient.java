package com.bigyj.service;

import com.bigyj.annotation.InvokeClient;
import com.bigyj.annotation.InvokeRequest;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.User;
import com.bigyj.config.RemoteCallConfig;

import org.springframework.http.HttpMethod;

@InvokeClient(value = "/v1/api",
		name = "requestClient",
		configuration = {RemoteCallConfig.class})
public interface RequestClient {
	@InvokeRequest(value = "/queryUser",method = HttpMethod.POST,maxAttempts = 5)
	public ResponseDto<User> queryUser(User user);
}

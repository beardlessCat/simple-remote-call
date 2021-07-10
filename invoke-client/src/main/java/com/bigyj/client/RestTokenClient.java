package com.bigyj.client;

import com.bigyj.annotation.InvokeClient;
import com.bigyj.annotation.InvokeRequest;
import com.bigyj.common.dto.AccessTokenQueryDto;
import com.bigyj.common.entity.AccessToken;

@InvokeClient(value = "/v1/oauth")
public interface RestTokenClient {
	@InvokeRequest(value = "/getAccessToken",withAccessToken = false,maxAttempts = 5)
	public AccessToken getAccessToken(AccessTokenQueryDto accessTokenQueryDto);
}

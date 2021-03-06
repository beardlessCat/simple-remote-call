package com.bigyj.client;

import com.bigyj.annotation.InvokeClient;
import com.bigyj.annotation.InvokeRequest;
import com.bigyj.common.dto.AccessTokenQueryDto;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.AccessToken;
import com.bigyj.config.RemoteCallConfig;

@InvokeClient(value = "/v1/oauth",name = "restToken")
public interface RestTokenClient {
	@InvokeRequest(value = "/getAccessToken",
			withAccessToken = false,
			maxAttempts = 5)
	public ResponseDto<AccessToken> getAccessToken(AccessTokenQueryDto accessTokenQueryDto);
}

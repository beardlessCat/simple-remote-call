package com.bigyj.controller;

import java.util.UUID;

import com.bigyj.common.dto.AccessTokenQueryDto;
import com.bigyj.common.entity.AccessToken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/oauth")
public class TokenController {
	@PostMapping("/getAccessToken")
	public AccessToken getAccessToken(@RequestBody AccessTokenQueryDto accessTokenQueryDto){
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(UUID.randomUUID().toString().replace("-",""));
		accessToken.setTokenAvailableTime(6000);
		return accessToken;
	}
}

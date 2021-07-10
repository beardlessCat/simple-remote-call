package com.bigyj.controller;

import com.bigyj.client.RequestClient;
import com.bigyj.client.RestTokenClient;
import com.bigyj.common.dto.AccessTokenQueryDto;
import com.bigyj.common.entity.AccessToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RequestClient requesClient;
	@Autowired
	private RestTokenClient restTokenClient ;
	@RequestMapping("hello")
	public String hello(){
		String hello = requesClient.hello("java");
		return hello ;
	}
	@RequestMapping("token")
	public AccessToken token(){
		AccessTokenQueryDto accessTokenQueryDto = new AccessTokenQueryDto().setClientId("1").setSecret("2");
		AccessToken accessToken = restTokenClient.getAccessToken(accessTokenQueryDto);
		return accessToken ;
	}
}

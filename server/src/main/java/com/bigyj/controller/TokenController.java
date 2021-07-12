package com.bigyj.controller;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.bigyj.common.dto.AccessTokenQueryDto;
import com.bigyj.common.dto.ResponseCommon;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.AccessToken;
import com.bigyj.common.utils.AESUtil;
import constant.AesConstant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/oauth")
public class TokenController {
	@PostMapping("/getAccessToken")
	public ResponseCommon getAccessToken(@RequestBody AccessTokenQueryDto accessTokenQueryDto) throws Exception {
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(UUID.randomUUID().toString().replace("-",""));
		accessToken.setTokenAvailableTime(6000);
		ResponseDto responseDto = new ResponseDto().setCode("0000").setData(accessToken).setMsg("认证成功");
		String tokenJson = JSONObject.toJSONString(responseDto);
		String encrypt = AESUtil.encrypt(AesConstant.AES_KEY, AesConstant.AES_IV, tokenJson);
		return new ResponseCommon().setCode("0000").setEntryData(encrypt).setMsg("token获取成功！");
	}
}

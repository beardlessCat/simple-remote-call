package com.bigyj.controller;

import com.alibaba.fastjson.JSONObject;
import com.bigyj.common.dto.ResponseCommon;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.User;
import com.bigyj.common.utils.AESUtil;
import constant.AesConstant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class HttpController {
	@PostMapping("/queryUser")
	public ResponseCommon queryUser(@RequestBody User user) throws Exception {
		User queryUser = new User().setAge(10).setName(user.getName());
		ResponseDto responseDto = new ResponseDto().setCode("0000").setData(queryUser).setMsg("查询成功");
		String userJson = JSONObject.toJSONString(responseDto);
		String encrypt = AESUtil.encrypt(AesConstant.AES_KEY, AesConstant.AES_IV, userJson);
		return new ResponseCommon().setCode("0000").setEntryData(encrypt).setMsg("token获取成功！");
	}
}

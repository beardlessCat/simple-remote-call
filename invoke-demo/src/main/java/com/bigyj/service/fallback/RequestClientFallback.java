package com.bigyj.service.fallback;

import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.User;
import com.bigyj.service.RequestClient;

import org.springframework.stereotype.Component;

@Component
public class RequestClientFallback implements RequestClient {
	@Override
	public ResponseDto<User> queryUser(User user){
		System.out.println("fallback  执行");
		user = new User().setName("匿名用户");
		ResponseDto responseDto = new ResponseDto().setCode("0000").setData(user).setMsg("接口熔断！");
		return responseDto;
	}
}

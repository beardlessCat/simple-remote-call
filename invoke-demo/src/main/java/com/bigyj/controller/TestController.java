package com.bigyj.controller;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.bigyj.client.RequestClient;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.User;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private RequestClient requesClient;

	@Autowired
	private RedissonClient redissonClient;
	@RequestMapping("hello")
	public ResponseDto hello(){
		ResponseDto<User> user = requesClient.queryUser(new User().setName("小明"));
		return user ;
	}

}

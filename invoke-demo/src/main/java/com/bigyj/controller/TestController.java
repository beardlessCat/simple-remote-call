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
	private static final String KEY ="LOCK:REDIS";
	private int amount = 1000 ;
	@Autowired
	private RequestClient requesClient;

	@Autowired
	private RedissonClient redissonClient;
	@RequestMapping("hello")
	public ResponseDto hello(){
		ResponseDto<User> user = requesClient.queryUser(new User().setName("小明"));
		return user ;
	}

	@GetMapping("lock")
	public String lock() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1000);
		RLock lock = redissonClient.getLock(KEY);
		for (int i = 0; i < 1000; i++) {
			new Thread(() -> {
				boolean res = false;
				try {
					res = lock.tryLock(100, 10, TimeUnit.SECONDS);
					if(res){
						amount--;
					}
				}
				catch (InterruptedException exception) {
					exception.printStackTrace();
				}
				finally{
					lock.unlock();
				}
				latch.countDown();
			}).start();
		}
		latch.await();
		return "amount:" + amount;
	}

}

package com.bigyj.controller;

import com.bigyj.client.RequestClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private RequestClient requesClient;

	@RequestMapping("hello")
	public String hello(){
		String hello = requesClient.hello();
		return hello ;
	}

}

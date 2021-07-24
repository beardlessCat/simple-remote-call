package com.bigyj;

import com.bigyj.annotation.EnableInvokeClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableInvokeClient(basePackages = {"com.bigyj.client","com.bigyj.service"})
public class InvokeDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(InvokeDemoApplication.class, args);
	}

}

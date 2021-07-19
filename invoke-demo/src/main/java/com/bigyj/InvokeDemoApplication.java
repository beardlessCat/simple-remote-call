package com.bigyj;

import java.util.Arrays;

import com.bigyj.annotation.EnableInvokeClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableInvokeClient
public class InvokeDemoApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(InvokeDemoApplication.class, args);
		Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

	}
}

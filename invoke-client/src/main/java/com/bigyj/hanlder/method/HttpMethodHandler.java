package com.bigyj.hanlder.method;

import java.lang.reflect.Type;
import java.util.function.Function;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class HttpMethodHandler implements MethodHandler{
	private String url ;
	private HttpMethod httpMethod;
	private String requestPath;
	private boolean withAccessToken;
	private Type returnType;
	private Function<Object[], Object> requestWrapper;
	private RestTemplate restTemplate ;
	//执行远程调用
	@Override
	public Object invoke(Object[] args) throws Throwable {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+"");
		headers.add("Content-Type","application/json;charset=utf-8");
		//HttpEntity<Ma> entity = new HttpEntity<>(requestDto, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, null, String.class);
		String body = responseEntity.getBody();
		System.out.println(body);
		return body;
	}
}

package com.bigyj.hanlder.method;

import java.lang.reflect.Type;

import com.bigyj.common.entity.AccessToken;
import com.bigyj.domain.RequestDomain;
import com.bigyj.supplier.AccessTokenSupplier;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class HttpMethodHandler implements MethodHandler{
	private RequestDomain requestDomain;
	private Type returnType;
	private RestTemplate restTemplate ;
	private AccessTokenSupplier accessTokenSupplier;

	/**
	 * 执行远程调用
	 */
	@Override
	public Object invoke(Object[] args) throws Throwable {
		Object params =null;
		if(args.length>0){
			params = args[0];
		}
		AccessToken accessToken = new AccessToken() ;
		if(requestDomain.isWithAccessToken()){
			accessToken = accessTokenSupplier.get();
		}
		return execute(params,accessToken);
	}
	Object execute(Object params , AccessToken accessToken){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+accessToken.getAccessToken());
		headers.add("Content-Type","application/json;charset=utf-8");
		HttpEntity entity = new HttpEntity<>(params, headers);
		ResponseEntity responseEntity = restTemplate.exchange(requestDomain.getValue(), requestDomain.getMethod(), entity, returnType.getClass());
		Object body = responseEntity.getBody();
		return body;
	}
}

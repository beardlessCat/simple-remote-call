package com.bigyj.hanlder.method;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import com.bigyj.common.entity.AccessToken;
import com.bigyj.common.exception.ApiException;
import com.bigyj.domain.RequestDomain;
import com.bigyj.supplier.AccessTokenSupplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
@Slf4j
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
		if(args!=null&&args.length>0){
			params = args[0];
		}
		AccessToken accessToken = new AccessToken() ;
		if(requestDomain.isWithAccessToken()){
			accessToken = accessTokenSupplier.get();
		}
		int tryCount = 0;
		//超时重试机制
		for(;;){
			try {
				tryCount++ ;
				return execute(params, accessToken);
			}catch (ResourceAccessException e){
				logger.error(requestDomain.getValue()+"接口重试次数"+tryCount);
				if(tryCount>requestDomain.getMaxAttempts()){
					throw new ApiException("00001","接口调用失败！");
				}
				try {
					//线程等待重试
					TimeUnit.MILLISECONDS.sleep(tryWaitMILLIS);
				}catch (InterruptedException ex){
					Thread.currentThread().interrupt();
					throw ex;
				}
				continue;
			}catch (HttpClientErrorException e) {
				throw e;
			} catch (RestClientException e) {
				throw new ApiException(e.getMessage());
			}
		}
	}
	Object execute(Object params , AccessToken accessToken) throws ClassNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		if(!StringUtils.isEmpty(accessToken.getAccessToken())){
			headers.add("Authorization", "Bearer "+accessToken.getAccessToken());
		}
		headers.add("Content-Type","application/json;charset=utf-8");
		HttpEntity entity = new HttpEntity<>(params, headers);
		Class<?> aClass = Class.forName(returnType.getTypeName());
		ResponseEntity responseEntity = restTemplate.exchange(requestDomain.getValue(), requestDomain.getMethod(), entity,aClass);
		HttpStatus statusCode = responseEntity.getStatusCode();
		Object body = responseEntity.getBody();
		return body;
	}
}

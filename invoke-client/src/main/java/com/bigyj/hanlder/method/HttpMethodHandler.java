package com.bigyj.hanlder.method;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import com.bigyj.common.dto.ResponseCommon;
import com.bigyj.common.entity.AccessToken;
import com.bigyj.common.exception.ApiException;
import com.bigyj.common.utils.AESUtil;
import com.bigyj.domain.MethodMetadata;
import com.bigyj.supplier.AccessTokenSupplier;
import com.fasterxml.jackson.databind.ObjectMapper;
import constant.AesConstant;
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
	private MethodMetadata methodMetadata;
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
		if(methodMetadata.isWithAccessToken()){
			accessToken = accessTokenSupplier.get();
		}
		int tryCount = 0;
		//超时重试机制
		for(;;){
			try {
				tryCount++ ;
				//判断是否有拦截器，进行拦截器执行
				methodMetadata.getRequestInterceptors().stream().forEach(
					requestInterceptor -> requestInterceptor.apply(null)
				);
				return execute(params, accessToken);
			}catch (ResourceAccessException e){
				logger.error(methodMetadata.getValue()+"接口重试次数"+tryCount);
				if(tryCount> methodMetadata.getMaxAttempts()){
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
	Object execute(Object params , AccessToken accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		if(!StringUtils.isEmpty(accessToken.getAccessToken())){
			headers.add("Authorization", "Bearer "+accessToken.getAccessToken());
		}
		headers.add("Content-Type","application/json;charset=utf-8");
		HttpEntity entity = new HttpEntity<>(params, headers);
		ResponseEntity<ResponseCommon> responseEntity = restTemplate
				.exchange(methodMetadata.getValue(), methodMetadata.getMethod(), entity, ResponseCommon.class);
		HttpStatus statusCode = responseEntity.getStatusCode();
		ResponseCommon body = responseEntity.getBody();
		String entryData = body.getEntryData();
		String decrypt = AESUtil.decrypt(AesConstant.AES_KEY, AesConstant.AES_IV, entryData);
		ObjectMapper objectMapper = new ObjectMapper();
		Object result = objectMapper.readValue(decrypt, objectMapper.constructType(returnType));
		return result;
	}
}

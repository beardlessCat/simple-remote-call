package com.bigyj.supplier;

import java.io.Serializable;

import com.bigyj.client.RestTokenClient;
import com.bigyj.common.dto.AccessTokenQueryDto;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.AccessToken;
import com.bigyj.config.RemoteConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Slf4j
public class RedisAccessTokenSupplier implements AccessTokenSupplier{
	private static final String TOKEN_KEY= "ACCESS_TOKEN";
	private RestTokenClient restTokenClient ;
	private RedisTemplate redisTemplate ;
	private RemoteConfig remoteConfig ;

	@Override
	public AccessToken get() {
		AccessToken accessToken = new AccessToken();
		//从redis数据库中获取数据
		ValueOperations<Serializable, String> operations = redisTemplate.opsForValue();
		String token = operations.get(TOKEN_KEY);
		//如果不为空，直接返回
		if(!StringUtils.isEmpty(token)){
			accessToken.setAccessToken(token);
			return accessToken;
		}else {
			logger.error("token失效，请重新获取token");
			//通过接口获取token
			ResponseDto<AccessToken> result = restTokenClient
					.getAccessToken(new AccessTokenQueryDto().setClientId(remoteConfig.getClientId())
							.setSecret(remoteConfig.getSecret()));
			accessToken = result.getData();
		}
		return accessToken;
	}
}

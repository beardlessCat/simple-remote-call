package com.bigyj.supplier;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.bigyj.client.RestTokenClient;
import com.bigyj.common.dto.AccessTokenQueryDto;
import com.bigyj.common.dto.ResponseDto;
import com.bigyj.common.entity.AccessToken;
import com.bigyj.config.RemoteConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

@AllArgsConstructor
@Slf4j
public class RedisAccessTokenSupplier implements AccessTokenSupplier{
	private static final String TOKEN_LOCK_KEY= "ACCESS_TOKEN_LOCK";
	private static final String TOKEN_KEY= "ACCESS_TOKEN";
	private RestTokenClient restTokenClient ;
	private RedisTemplate redisTemplate ;
	private RemoteConfig remoteConfig ;
	private RedissonClient redissonClient ;

	@Override
	public AccessToken get() throws InterruptedException {
		AccessToken accessToken = new AccessToken();
		//从redis数据库中获取数据
		String token = this.getRedisToken();
		//如果不为空，直接返回
		if(!StringUtils.isEmpty(token)){
			accessToken.setAccessToken(token);
			return accessToken;
		}else {
			logger.error("token失效，请重新获取token");
			RLock lock = redissonClient.getLock(TOKEN_LOCK_KEY);
			boolean res = false;
			//双重检查锁
			try {
				res = lock.tryLock(100, 10, TimeUnit.SECONDS);
				if(res){
					String localToken = this.getRedisToken();
					if(StringUtils.isEmpty(localToken)){
						accessToken = this.getNew();
					}else {
						accessToken = new AccessToken().setAccessToken(localToken);
					}
				}
			}finally{
				lock.unlock();
			}
		}
		return accessToken;
	}

	/**
	 * 通过接口获取token
	 * @return
	 */
	private AccessToken getNew() {
		ResponseDto<AccessToken> result = restTokenClient
				.getAccessToken(new AccessTokenQueryDto().setClientId(remoteConfig.getClientId())
						.setSecret(remoteConfig.getSecret()));
		AccessToken accessToken = result.getData();
		ValueOperations<Serializable, String> operations = redisTemplate.opsForValue();

		operations.set(TOKEN_KEY,accessToken.getAccessToken());
		return accessToken;
	}

	/**
	 * 从redis中获取token
	 * @return
	 */
	private String getRedisToken() {
		ValueOperations<Serializable, String> operations = redisTemplate.opsForValue();
		String token = operations.get(TOKEN_KEY);
		return token;
	}

}

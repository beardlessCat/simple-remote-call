package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerStateManager;
import com.bigyj.utils.ProtoStuffUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisBreakerManagerHolder implements BreakerManagerHolder{
	@Autowired
	private RedisTemplate redisTemplate ;

	@Override
	public BreakerStateManager get(String targetName) {
		byte[] bytes  = (byte[]) redisTemplate.opsForValue().get(targetName);
		BreakerStateManager breakerStateManager = ProtoStuffUtil.deserialize(bytes, BreakerStateManager.class);
		return breakerStateManager;
	}

	@Override
	public void manage(String targetName, BreakerStateManager breakerManager) {
		redisTemplate.opsForValue().set(targetName, ProtoStuffUtil.serialize(breakerManager));
	}

	@Override
	public BreakerStateManager create() {
		return null;
	}
}

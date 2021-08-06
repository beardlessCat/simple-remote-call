package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerStateManager;
import com.bigyj.breaker.manager.RedisBreakerManagerObject;
import com.bigyj.utils.ProtoStuffUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisBreakerManagerHolder implements BreakerManagerHolder{

	@Autowired
	private RedisTemplate redisTemplate ;

	@Override
	public BreakerStateManager get(String targetName) {
		byte[] bytes  = (byte[]) redisTemplate.opsForValue().get(targetName);
		RedisBreakerManagerObject redisBreakerManagerObject = ProtoStuffUtil.deserialize(bytes, RedisBreakerManagerObject.class);
		BreakerStateManager breakerStateManager = redisBreakerManagerObject.toManager();
 		return breakerStateManager;
	}

	@Override
	public void manage(String targetName, BreakerStateManager breakerManager) {
		RedisBreakerManagerObject redisBreakerManagerObject = new RedisBreakerManagerObject(breakerManager);
		redisTemplate.opsForValue().set(targetName, ProtoStuffUtil.serialize(redisBreakerManagerObject));
	}

}

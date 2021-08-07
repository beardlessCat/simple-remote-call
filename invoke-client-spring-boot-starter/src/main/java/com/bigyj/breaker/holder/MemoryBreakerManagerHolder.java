package com.bigyj.breaker.holder;

import java.util.concurrent.ConcurrentHashMap;

import com.bigyj.breaker.constant.BreakerManagerConfigConstant;
import com.bigyj.breaker.manager.BreakerStateManager;
import com.bigyj.config.BreakerConfig;

public class MemoryBreakerManagerHolder implements BreakerManagerHolder{
	private BreakerConfig breakerConfig;

	public MemoryBreakerManagerHolder(BreakerConfig breakerConfig) {
		this.breakerConfig = breakerConfig;
	}
	private ConcurrentHashMap<String, BreakerStateManager> breakerManagers = new ConcurrentHashMap();

	@Override
	public BreakerStateManager get(String targetName) {
		BreakerStateManager breakerStateManager = breakerManagers.get(targetName);
		if(breakerStateManager == null){
			int maxOpenToTryTime = breakerConfig.getMaxOpenToTryTime()>0?breakerConfig.getMaxOpenToTryTime(): BreakerManagerConfigConstant.MAX_OPEN_TO_TRY_TIME;
			int maxFailCount = breakerConfig.getMaxFailCount()>0?breakerConfig.getMaxFailCount():BreakerManagerConfigConstant.MAX_FAIL_COUNT;
			int maxSuccessCount = breakerConfig.getMaxSuccessCount()>0?breakerConfig.getMaxSuccessCount():BreakerManagerConfigConstant.MAX_SUCCESS_COUNT;
			int maxOpenRetryCount = breakerConfig.getMaxOpenRetryCount()>0?breakerConfig.getMaxOpenRetryCount():BreakerManagerConfigConstant.MAX_OPEN_RETRY_COUNT;
			breakerStateManager = new BreakerStateManager(0,0,0,maxOpenToTryTime,maxFailCount,maxSuccessCount, maxOpenRetryCount);
			breakerManagers.putIfAbsent(targetName,breakerStateManager);
		}
		return breakerStateManager;
	}

	@Override
	public void manage(String targetName, BreakerStateManager breakerManager) {

	}
}

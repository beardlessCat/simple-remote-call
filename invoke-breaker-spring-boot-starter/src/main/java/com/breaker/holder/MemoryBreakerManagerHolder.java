package com.breaker.holder;

import java.util.concurrent.ConcurrentHashMap;

import com.breaker.manager.BreakerStateManager;
import com.breaker.manager.MetaBreaker;

public class MemoryBreakerManagerHolder implements BreakerManagerHolder{

	private ConcurrentHashMap<String, BreakerStateManager> breakerManagers = new ConcurrentHashMap();

	@Override
	public BreakerStateManager get(String targetName, MetaBreaker metaBreaker) {
		BreakerStateManager breakerStateManager = breakerManagers.get(targetName);
		if(breakerStateManager == null){
			breakerStateManager = new BreakerStateManager(0,0,0,metaBreaker);
			breakerManagers.putIfAbsent(targetName,breakerStateManager);
		}
		return breakerStateManager;
	}

	@Override
	public void manage(String targetName, BreakerStateManager breakerManager) {

	}
}

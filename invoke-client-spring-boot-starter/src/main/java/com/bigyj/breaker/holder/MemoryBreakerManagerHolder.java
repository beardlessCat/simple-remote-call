package com.bigyj.breaker.holder;

import java.util.concurrent.ConcurrentHashMap;

import com.bigyj.breaker.manager.BreakerStateManager;

public class MemoryBreakerManagerHolder implements BreakerManagerHolder{
	private ConcurrentHashMap<String, BreakerStateManager> breakerManagers = new ConcurrentHashMap();
	@Override
	public BreakerStateManager get(String targetName) {
		return breakerManagers.get(targetName);
	}

	@Override
	public void manage(String targetName, BreakerStateManager breakerManager) {
		//fixme		breakerManagers.putIfAbsent(targetName,breakerManager)
		breakerManagers.put(targetName,breakerManager);
	}

	@Override
	public BreakerStateManager create() {
		return null;
	}
}

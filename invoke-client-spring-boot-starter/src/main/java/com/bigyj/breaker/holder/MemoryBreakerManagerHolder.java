package com.bigyj.breaker.holder;

import java.util.concurrent.ConcurrentHashMap;

import com.bigyj.breaker.manager.BreakerManager;

public class MemoryBreakerManagerHolder implements BreakerManagerHolder{
	private ConcurrentHashMap<String, BreakerManager> breakerManagers = new ConcurrentHashMap();
	@Override
	public BreakerManager get(String targetName) {
		return breakerManagers.get(targetName);
	}

	@Override
	public void manage(String targetName, BreakerManager breakerManager) {
		//fixme		breakerManagers.putIfAbsent(targetName,breakerManager)
		breakerManagers.put(targetName,breakerManager);
	}
}

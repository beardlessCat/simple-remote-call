package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerStateManager;

public class RedisBreakerManagerHolder implements BreakerManagerHolder{
	@Override
	public BreakerStateManager get(String targetName) {
		return null;
	}

	@Override
	public void manage(String targetName, BreakerStateManager breakerManager) {

	}

	@Override
	public BreakerStateManager create() {
		return null;
	}
}

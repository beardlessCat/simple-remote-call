package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerManager;

public class RedisBreakerManagerHolder implements BreakerManagerHolder{
	@Override
	public BreakerManager get(String targetName) {
		return null;
	}

	@Override
	public void manage(String targetName, BreakerManager breakerManager) {

	}

	@Override
	public BreakerManager create() {
		return null;
	}
}

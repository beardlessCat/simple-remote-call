package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerManager;

public abstract class BreakerState {
	public abstract void handle(BreakerManager breakerManager);
}

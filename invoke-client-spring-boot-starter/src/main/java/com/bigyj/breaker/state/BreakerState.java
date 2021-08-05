package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

public abstract class BreakerState {
	private BreakerStateManager breakerStateManager ;

	public BreakerState(BreakerStateManager breakerStateManager) {
		this.breakerStateManager = breakerStateManager;
	}
}

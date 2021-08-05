package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

public class OpenState extends BreakerState {
	public OpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
		breakerStateManager.clear();
	}
}

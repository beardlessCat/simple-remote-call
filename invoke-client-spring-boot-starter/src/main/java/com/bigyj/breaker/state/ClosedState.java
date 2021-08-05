package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

public class ClosedState extends BreakerState{
	public ClosedState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
	}
}

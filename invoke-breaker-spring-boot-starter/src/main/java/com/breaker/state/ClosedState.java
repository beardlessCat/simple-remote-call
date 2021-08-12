package com.breaker.state;

import com.breaker.manager.BreakerStateManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClosedState extends BreakerState{
	public ClosedState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
		breakerStateManager.clear();
	}
	@Override
	public void actException() {
		breakerStateManager.increaseFailureCount();
		if(breakerStateManager.failCountReached()){
			breakerStateManager.toOpenStatus();
		}
	}
}

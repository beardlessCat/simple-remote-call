package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;
import lombok.extern.slf4j.Slf4j;
import java.security.SecureRandom;

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

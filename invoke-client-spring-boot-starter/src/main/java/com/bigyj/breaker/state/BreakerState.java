package com.bigyj.breaker.state;

import java.io.Serializable;

import com.bigyj.breaker.manager.BreakerStateManager;

public abstract class BreakerState implements Serializable {
	private BreakerStateManager breakerStateManager ;

	public BreakerState(BreakerStateManager breakerStateManager) {
		this.breakerStateManager = breakerStateManager;
	}
}

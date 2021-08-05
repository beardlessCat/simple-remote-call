package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

public class HalfOpenState extends BreakerState{

	public HalfOpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
	}

	/**
	 * 半恢复状态需进行限流处理
	 */
	@Override
	public void methodIsAboutToBeCalled() {
		super.methodIsAboutToBeCalled();

	}
}

package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

public class HalfOpenState extends BreakerState{

	public HalfOpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
	}

	/**
	 * ��ָ�״̬�������������
	 */
	@Override
	public void methodIsAboutToBeCalled() {
		super.methodIsAboutToBeCalled();

	}
}

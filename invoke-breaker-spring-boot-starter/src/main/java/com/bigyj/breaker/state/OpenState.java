package com.bigyj.breaker.state;


import java.util.Timer;
import java.util.TimerTask;

import com.bigyj.breaker.manager.BreakerStateManager;

public class OpenState extends BreakerState {
	public OpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
		//fixme 考虑换为线程池
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				timeToTry();
				timer.cancel();
			}
		}, breakerStateManager.getMaxOpenToTryTime());
	}

	/**
	 * 定时器变为半恢复状态
	 */
	private void timeToTry() {
		breakerStateManager.toHalfOpenStatus();
	}
}

package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

import java.util.Timer;
import java.util.TimerTask;

public class OpenState extends BreakerState {
	public OpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
		//fixme ¿¼ÂÇ»»ÎªÏß³Ì³Ø
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
	 * ±äÎª°ë»Ö¸´×´Ì¬
	 */
	private void timeToTry() {
		breakerStateManager.toHalfOpenStatus();
	}


}

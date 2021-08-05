package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

import java.util.Timer;
import java.util.TimerTask;

public class OpenState extends BreakerState {
	public OpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
		//fixme ���ǻ�Ϊ�̳߳�
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
	 * ��Ϊ��ָ�״̬
	 */
	private void timeToTry() {
		breakerStateManager.toHalfOpenStatus();
	}


}

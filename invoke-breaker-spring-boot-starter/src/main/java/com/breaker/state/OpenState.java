package com.breaker.state;


import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.breaker.manager.BreakerStateManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenState extends BreakerState {

	private static ScheduledThreadPoolExecutor RECOVER_EXECUTOR;

	public OpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
		if (null == RECOVER_EXECUTOR) {
			RECOVER_EXECUTOR = new ScheduledThreadPoolExecutor(1);
			logger.info("恢复程线程池已初始化");
		}
		RECOVER_EXECUTOR.schedule(
			()->{
				timeToHalfOpen();
			},
			breakerStateManager.getMaxOpenToTryTime(),
			TimeUnit.SECONDS
		);
	}

	/**
	 * 开启状态，直接执行fallbackCall方法
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Override
	public void methodIsAboutToBeCalled() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		breakerStateManager.fallbackCall();
	}

	/**
	 * 定时器变为半恢复状态
	 */
	private void timeToHalfOpen() {
		breakerStateManager.toHalfOpenStatus();
	}
}

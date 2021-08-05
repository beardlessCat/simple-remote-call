package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

public abstract class BreakerState {
	protected BreakerStateManager breakerStateManager ;

	public BreakerState(BreakerStateManager breakerStateManager) {
		this.breakerStateManager = breakerStateManager;
	}

	/**
	 * 调用方法之前处理的操作
	 */
	public void methodIsAboutToBeCalled() {
		//如果是断开状态，直接返回，然后等超时转换到半断开状态
		if (breakerStateManager.isOpen()) {
			throw new RuntimeException("服务已熔断，请稍等重试！");
		}
	}

	/**
	 * 方法调用成功之后的操作
	 */
	public void actSuccess() {
		breakerStateManager.increaseSuccessCount();
	}

	/**
	 * 方法调用发生异常操作后的操作
	 */
	public void actException() {
		//增加失败次数计数器，并且保存错误信息
		breakerStateManager.increaseFailureCount();
		//重置连续成功次数
		//breakerStateManager.resetConsecutiveSuccessCount();
	}

}

package com.bigyj.breaker.state;

import java.io.Serializable;

import com.bigyj.breaker.manager.BreakerStateManager;
import com.bigyj.common.exception.ApiException;
import com.bigyj.exception.MethodNotAvailableException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BreakerState implements Serializable {
	public BreakerStateManager breakerStateManager ;
	public BreakerState(BreakerStateManager breakerStateManager) {
		this.breakerStateManager = breakerStateManager;
	}

	/**
	 * 调用方法之前处理的操作
	 */
	public void methodIsAboutToBeCalled() {
		//如果是断开状态，直接返回，然后等超时转换到半断开状态
		if (breakerStateManager.isOpen()) {
			throw new MethodNotAvailableException("服务已熔断，请稍等重试！");
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
	}

}

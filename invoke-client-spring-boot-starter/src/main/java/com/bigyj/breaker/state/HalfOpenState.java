package com.bigyj.breaker.state;

import java.security.SecureRandom;
import com.bigyj.breaker.manager.BreakerStateManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HalfOpenState extends BreakerState{

	public HalfOpenState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
	}

	/**
	 * 半恢复状态需进行限流处理
	 */
	@Override
	public void methodIsAboutToBeCalled() {
		SecureRandom secureRandom = new SecureRandom();
		int x = secureRandom.nextInt(10);
		if(x>5){
			throw new RuntimeException("服务已熔断，请稍等重试！");
		}
	}

	@Override
	public void actSuccess() {
		super.actSuccess();
		if(breakerStateManager.successCountReached()){
			breakerStateManager.toCloseStatus();
		}
	}

	@Override
	public void actException() {
		super.actException();
		breakerStateManager.resetSuccessCount();
		if(breakerStateManager.failRetryCountReached()){
			breakerStateManager.toOpenStatus();
		}
	}
}

package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;
import lombok.extern.slf4j.Slf4j;
import java.security.SecureRandom;

@Slf4j
public class ClosedState extends BreakerState{
	public ClosedState(BreakerStateManager breakerStateManager) {
		super(breakerStateManager);
		breakerStateManager.clear();
	}
	@Override
	public void methodIsAboutToBeCalled() {
		SecureRandom secureRandom = new SecureRandom();
		int x = secureRandom.nextInt(10);
		if(x>=5){
			logger.error("接口半恢复，被拦截，直接返回异常！");
			throw new RuntimeException("服务已熔断，请稍等重试[限流拦截]！");
		}else {
			logger.error("接口半恢复，限流放行，进行接口尝试！");
			super.actException();
		}
	}
}

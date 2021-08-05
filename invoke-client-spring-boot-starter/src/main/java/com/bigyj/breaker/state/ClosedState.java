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
			logger.error("�ӿڰ�ָ��������أ�ֱ�ӷ����쳣��");
			throw new RuntimeException("�������۶ϣ����Ե�����[��������]��");
		}else {
			logger.error("�ӿڰ�ָ����������У����нӿڳ��ԣ�");
			super.actException();
		}
	}
}

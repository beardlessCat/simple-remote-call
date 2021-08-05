package com.bigyj.breaker.state;

import com.bigyj.breaker.manager.BreakerStateManager;

public abstract class BreakerState {
	protected BreakerStateManager breakerStateManager ;

	public BreakerState(BreakerStateManager breakerStateManager) {
		this.breakerStateManager = breakerStateManager;
	}

	/**
	 * ���÷���֮ǰ����Ĳ���
	 */
	public void methodIsAboutToBeCalled() {
		//����ǶϿ�״̬��ֱ�ӷ��أ�Ȼ��ȳ�ʱת������Ͽ�״̬
		if (breakerStateManager.isOpen()) {
			throw new RuntimeException("�������۶ϣ����Ե����ԣ�");
		}
	}

	/**
	 * �������óɹ�֮��Ĳ���
	 */
	public void actSuccess() {
		breakerStateManager.increaseSuccessCount();
	}

	/**
	 * �������÷����쳣������Ĳ���
	 */
	public void actException() {
		//����ʧ�ܴ��������������ұ��������Ϣ
		breakerStateManager.increaseFailureCount();
		//���������ɹ�����
		//breakerStateManager.resetConsecutiveSuccessCount();
	}

}

package com.bigyj.breaker.manager;

import java.io.Serializable;

import com.bigyj.enums.BreakerStateEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RedisBreakerManagerObject implements Serializable {

	/**
	 * ʧ�ܴ���
	 */
	private int failCount ;

	/**
	 * �ɹ�����
	 */
	private int successCount ;

	/**
	 * �ӿ��۶�ʱ��
	 */
	private long closeAt;

	/**
	 * ���ʧ�ܴ���
	 */
	private int maxFailCount ;

	/**
	 * ���ɹ�����
	 */
	private int maxSuccessCount;

	/**
	 *��ָ����Դ���
	 */
	private int openRetryCount ;

	/**
	 * ��ָ�����Դ������ﵽ�������󣬱�ΪOPEN״̬
	 */
	private int maxOpenRetryCount ;

	/**
	 * ��ǰ״̬
	 */
	private BreakerStateEnum stateEnum ;

	/**
	 * ����breakerStateManager��ȡRedisBreakerManagerObject����
	 * @param breakerStateManager
	 */
	public RedisBreakerManagerObject(BreakerStateManager breakerStateManager) {
		this.failCount = breakerStateManager.getFailCount();
		this.successCount = breakerStateManager.getSuccessCount();
		this.closeAt = breakerStateManager.getCloseAt();
		this.maxFailCount = breakerStateManager.getMaxFailCount();
		this.maxSuccessCount = breakerStateManager.getMaxSuccessCount();
		this.openRetryCount = breakerStateManager.getOpenRetryCount();
		this.maxOpenRetryCount = breakerStateManager.getMaxOpenRetryCount();
		if(breakerStateManager.isClosed()){
			this.stateEnum = BreakerStateEnum.CLOSE;
		}
		if(breakerStateManager.isOpen()){
			this.stateEnum = BreakerStateEnum.OPEN;
		}
		if(breakerStateManager.isHalfOpen()){
			this.stateEnum = BreakerStateEnum.HALFOPEN;
		}
	}
	//����RedisBreakerManagerObject��ȡbreakerStateManager����
	public BreakerStateManager toManager() {
		BreakerStateManager breakerStateManager = new BreakerStateManager(this.failCount, this.successCount, this.closeAt, this.maxFailCount, this.maxSuccessCount, this.openRetryCount, this.maxOpenRetryCount);
		if(this.stateEnum == BreakerStateEnum.CLOSE){
			breakerStateManager.toCloseStatus();
		}
		if(this.stateEnum == BreakerStateEnum.OPEN){
			breakerStateManager.toOpenStatus();
		}
		if(this.stateEnum == BreakerStateEnum.HALFOPEN){
			breakerStateManager.toHalfOpenStatus();
		}
		return breakerStateManager;
	}
}

package com.bigyj.breaker.manager;

import java.io.Serializable;

import com.bigyj.enums.BreakerStateEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RedisBreakerManagerObject implements Serializable {

	/**
	 * 失败次数
	 */
	private int failCount ;

	/**
	 * 成功次数
	 */
	private int successCount ;

	/**
	 * 接口熔断时间
	 */
	private long closeAt;

	/**
	 * 最大失败次数
	 */
	private int maxFailCount ;

	/**
	 * 最大成功次数
	 */
	private int maxSuccessCount;

	/**
	 *半恢复尝试次数
	 */
	private int openRetryCount ;

	/**
	 * 半恢复最大尝试次数，达到最大次数后，变为OPEN状态
	 */
	private int maxOpenRetryCount ;
	/**
	 * 接口熔断最长时间
	 */
	private int maxOpenToTryTime;
	/**
	 * 当前状态
	 */
	private BreakerStateEnum stateEnum ;

	/**
	 * 根据breakerStateManager获取RedisBreakerManagerObject对象
	 * @param breakerStateManager
	 */
	public RedisBreakerManagerObject(BreakerStateManager breakerStateManager) {
		this.failCount = breakerStateManager.getFailCount();
		this.successCount = breakerStateManager.getSuccessCount();
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
	//根据RedisBreakerManagerObject获取breakerStateManager对象
	public BreakerStateManager toManager() {
		BreakerStateManager breakerStateManager = new BreakerStateManager(0,0,0,maxOpenToTryTime,maxFailCount,maxSuccessCount, maxOpenRetryCount);

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

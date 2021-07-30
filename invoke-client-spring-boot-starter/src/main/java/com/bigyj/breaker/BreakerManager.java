package com.bigyj.breaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 *
 * OPEN-------[failCouint>MAX_FAIL_COUNT]--------->CLOSE-------[time>timeOut]--------->HALFOPEN--------[SUCCESS]-------->OPEN
 */
@Data
@AllArgsConstructor
@ToString
public class BreakerManager {
	private static final int MAX_FAIL_COUNT = 10 ;
	private static final int MAX_TIME_OUT = 10 ;
	/**
	 * 失败次数
	 */
	private int failCount ;
	/**
	 * 超时时间
	 */
	private int timeOut;
	/**
	 *当前状态
	 */
	private Breaker.BreakStatus currentStatus;

	public void addFailCount(){
		Breaker.BreakStatus currentStatus = getCurrentStatus();
		int count = this.failCount++;
		if(count>=10){
			this.toCloseStatus();
		}
		this.setFailCount(count);
	}

	/**
	 * 变为OPEN状态
	 */
	public void toOpenStatus(){
		currentStatus = Breaker.BreakStatus.OPEN;
	}

	/**
	 * 变为CLOSE状态
	 */
	public void toCloseStatus(){
		currentStatus = Breaker.BreakStatus.CLOSE;

	}

	/**
	 * 变为HALFOPEN状态
	 */
	public void toHalfOpenStatus(){
		currentStatus = Breaker.BreakStatus.HALFOPEN;
	}
}

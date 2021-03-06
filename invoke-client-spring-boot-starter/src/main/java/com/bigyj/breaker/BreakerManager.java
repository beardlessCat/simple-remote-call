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
	/**
	 * 最大失败次数
	 */
	private static final int MAX_FAIL_COUNT = 10 ;

	/**
	 * 最大成功次数
	 */
	private static final int MAX_SUCCESS_COUNT = 3 ;

	/**
	 * 熔断后接口重试最大时间
	 */
	public static final long MAX_CLOSE_TO_TRY_TIME = 5000L ;

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
	 *当前状态
	 */
	private Breaker.BreakStatus currentStatus;

	public void addFailCount(){
		Breaker.BreakStatus currentStatus = getCurrentStatus();
		this.failCount++ ;
		if(this.failCount>=MAX_FAIL_COUNT){
			this.toCloseStatus();
			//记录当前开始熔断时刻
			long closeAt = System.currentTimeMillis();
			this.closeAt = closeAt;
		}
	}

	public void addSuccessCount(){
		this.successCount++ ;
		if(this.successCount>MAX_SUCCESS_COUNT){
			toOpenStatus();
			this.closeAt = 0;
			this.successCount=0;
			this.failCount=0;
		}
	}
	/**
	 * 变为OPEN状态
	 */
	public void toOpenStatus(){
		this.currentStatus = Breaker.BreakStatus.OPEN;
		System.out.println("【接口已恢复】");
	}

	/**
	 * 变为CLOSE状态
	 */
	public void toCloseStatus(){
		this.currentStatus = Breaker.BreakStatus.CLOSE;
		System.out.println("【接口熔断】");
	}

	/**
	 * 变为HALFOPEN状态
	 */
	public void toHalfOpenStatus(){
		this.currentStatus = Breaker.BreakStatus.HALFOPEN;
		System.out.println("【接口半恢复恢复】");
	}
}

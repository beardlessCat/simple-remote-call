package com.bigyj.breaker.manager;

import com.bigyj.breaker.Breaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 *
 * CLOES-------[failCouint>MAX_FAIL_COUNT]--------->OPEN-------[time>timeOut]--------->HALFOPEN--------[SUCCESS]-------->CLOSE
 */
@Data
@AllArgsConstructor
@ToString
public class BreakerManager{


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
	 *当前状态
	 */
	private Breaker.BreakStatus currentStatus;

	public synchronized void  addRetryCount(){
		this.openRetryCount++;
		if(this.openRetryCount>=maxOpenRetryCount){
			this.toOpenStatus();
			this.openRetryCount = 0;
		}
	}

	public synchronized void  addFailCount(){
		this.failCount++ ;
		if(this.failCount>=this.maxFailCount){
			this.toOpenStatus();
			//记录当前开始熔断时刻
			long closeAt = System.currentTimeMillis();
			this.closeAt = closeAt;
		}
	}
	
	public synchronized void addSuccessCount(){
		this.successCount++ ;
		if(this.successCount>this.maxSuccessCount){
			toCloseStatus();
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
		System.out.println("【接口熔断】");
	}

	/**
	 * 变为CLOSE状态
	 */
	
	public void toCloseStatus(){
		this.currentStatus = Breaker.BreakStatus.CLOSE;
		System.out.println("【接口回复】");
	}

	/**
	 * 变为HALFOPEN状态
	 */
	public void toHalfOpenStatus(){
		this.currentStatus = Breaker.BreakStatus.HALFOPEN;
		System.out.println("【接口半恢复恢复】");
	}
}

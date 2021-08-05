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
			long closeAt = System.currentTimeMillis();
			this.closeAt = closeAt;
		}
	}

	public synchronized void  addFailCount(){
		this.failCount++ ;
		//fixme 有点臃肿，尝试寻找更加简洁的方式（当前状态为close时才打开）
		if(this.failCount>=this.maxFailCount){
			if(this.currentStatus==Breaker.BreakStatus.CLOSE){
				this.toOpenStatus();
				//记录当前开始熔断时刻
				long closeAt = System.currentTimeMillis();
				this.closeAt = closeAt;
			}
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
		System.out.println("【断路器变为OPEN】");
	}

	/**
	 * 变为CLOSE状态
	 */
	
	public void toCloseStatus(){
		this.currentStatus = Breaker.BreakStatus.CLOSE;
		System.out.println("【断路器变为CLOSE】");
	}

	/**
	 * 变为HALFOPEN状态
	 */
	public void toHalfOpenStatus(){
		this.currentStatus = Breaker.BreakStatus.HALFOPEN;
		//半恢复清空
		System.out.println("【断路器变为HALF-OPEN】");
	}
}

package com.bigyj;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.bigyj.breaker.Breaker;
import com.bigyj.breaker.BreakerManager;

public class Test {
	private static  Map<String,BreakerManager>  breakerManagers = new HashMap<>();
	private static  String interfaceId = "callInterface" ;
	public static void main(String[] args)  {
		BreakerManager manager = getBreakManagerByInterface(interfaceId);
		if(manager == null){
			for(int i= 0;i<20;i++){
				//模拟接口调用
				try {
					callInterface(i);
				}
				catch (InterruptedException e) {
					//出现异常，增加失败次数
					manager  = new BreakerManager(0,10, Breaker.BreakStatus.OPEN);
					manager.addFailCount();
					breakerManagers.put(interfaceId,manager);
				}
			}
		}else {
			Breaker.BreakStatus currentStatus = manager.getCurrentStatus();

			int failCount = manager.getFailCount();
			int timeOut = manager.getTimeOut();
			if(currentStatus== Breaker.BreakStatus.OPEN){
				//断路器OPNE状态，直接进行接口调用
				//
			}else if(currentStatus== Breaker.BreakStatus.HALFOPEN){
				//半恢复，尝试一次，
				//成功，回复open状态

				//失败，增加失败次数
				manager.addFailCount();
			}else {
				//熔断,增加失败此时(加锁处理)

			}

		}
	}

	private static void callInterface(int i) throws InterruptedException {
		if(i<5){
			//open状态
			TimeUnit.SECONDS.sleep(2);
			System.out.println("接口调用成功");
		}else if(i<12){
			//模拟接口调用失败，进入close状态
			TimeUnit.SECONDS.sleep(2);
			System.out.println("接口调用失败");
			throw new InterruptedException();
		}else if(i==12){
			//模拟接口调用成功异常，进入半恢复状态图
			TimeUnit.SECONDS.sleep(2);
			System.out.println("接口调用成功");
		}else {
			//进入open状态
			TimeUnit.SECONDS.sleep(2);
			System.out.println("接口调用成功");
		}
		System.out.println(breakerManagers.get(interfaceId).toString());
	}

	private static BreakerManager getBreakManagerByInterface(String interfaceId) {
		return breakerManagers.get(interfaceId);
	}
}

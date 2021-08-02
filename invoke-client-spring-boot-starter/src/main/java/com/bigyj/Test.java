package com.bigyj;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.bigyj.breaker.Breaker;
import com.bigyj.breaker.manager.BreakerManager;

public class Test {
	/*private static  Map<String, BreakerManager>  breakerManagers = new HashMap<>();
	private static  String interfaceId = "callInterface" ;
	public static void main(String[] args)  {
		for(int i= 0;i<25;i++){
			BreakerManager manager = getBreakManagerByInterface(interfaceId);
			if(manager == null){
				manager  = new BreakerManager(0,0, 0,Breaker.BreakStatus.OPEN);
				openCall(i,manager);
			}else {
				Breaker.BreakStatus currentStatus = manager.getCurrentStatus();
				int closeAt = (int) manager.getCloseAt();
				if(currentStatus== Breaker.BreakStatus.OPEN){
					//断路器OPNE状态，直接进行接口调用
					openCall(i,manager);
				}else if(currentStatus== Breaker.BreakStatus.HALFOPEN){
					SecureRandom secureRandom = new SecureRandom();
					int x = secureRandom.nextInt(10);
					if(x>5){
						//半恢复，随机尝试一次，
						openCall(i,manager);
					}
					//成功，回复open状态
					//失败，增加失败次数
					manager.addFailCount();
				}else {
					//熔断,增加失败此时(加锁处理)
					System.out.println("接口熔断");
					long now = System.currentTimeMillis();
					if(now - closeAt >= BreakerManager.MAX_CLOSE_TO_TRY_TIME){
						System.out.println("接口熔断时间到达最大值，开始尝试调用接口");
						openCall(i,manager);
					}
				}
			}
		}
	}

	private static void openCall(int i, BreakerManager manager) {
		//模拟接口调用
		try {
			callInterface(i);
			if(manager.getCurrentStatus() != Breaker.BreakStatus.OPEN){
				if(manager.getCurrentStatus() == Breaker.BreakStatus.CLOSE){
					manager.toHalfOpenStatus();
				}
				manager.addSuccessCount();
			}
		}catch (InterruptedException e) {
			//出现异常，增加失败次数
			manager.addFailCount();
			breakerManagers.put(interfaceId,manager);
		}finally {
			if(manager!=null){
				System.out.println(manager.toString());
			}
		}
	}

	private static void callInterface(int i) throws InterruptedException {
		if(i<1){
			//open状态
			TimeUnit.SECONDS.sleep(1);
			System.out.println("接口调用成功");
		}else if(i<12){
			//模拟接口调用失败，进入close状态
			TimeUnit.SECONDS.sleep(1);
			System.out.println("接口调用失败");
			throw new InterruptedException();
		}else if(i==12){
			//模拟接口调用成功异常，进入半恢复状态图
			TimeUnit.SECONDS.sleep(1);
			System.out.println("接口调用成功");
		}else {
			//进入open状态
			TimeUnit.SECONDS.sleep(1);
			System.out.println("接口调用成功");
		}
	}

	private static BreakerManager getBreakManagerByInterface(String interfaceId) {
		return breakerManagers.get(interfaceId);
	}*/
}

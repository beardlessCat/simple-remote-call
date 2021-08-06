package com.bigyj;

import java.util.concurrent.ConcurrentHashMap;

import com.bigyj.breaker.manager.BreakerStateManager;

public class Test {
	public static void main(String[] args) {
		ConcurrentHashMap<String, BreakerStateManager> breakerManagers = new ConcurrentHashMap();
		BreakerStateManager breakerStateManager = new BreakerStateManager(0,0,0,10,10, 0,10);
		breakerManagers.put("1",breakerStateManager);
		BreakerStateManager after = breakerManagers.get("1");
		after.toHalfOpenStatus();
		after.addSuccessCount();
		after.addFailCount();
		//breakerManagers.put("1",after);
		BreakerStateManager afterTwo = breakerManagers.get("1");
		System.out.println(afterTwo);
	}
}

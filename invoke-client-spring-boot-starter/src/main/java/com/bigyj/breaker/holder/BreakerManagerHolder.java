package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerStateManager;

public interface BreakerManagerHolder {
	/**
	 * 获取BreakerStateManager对象
	 * @param targetName
	 * @return
	 */
	BreakerStateManager get(String targetName);

	/**
	 * 管理（存储、更新）BreakerStateManager对象
	 * @param targetName
	 * @param breakerManager
	 */
	void manage(String targetName , BreakerStateManager breakerManager) ;

}

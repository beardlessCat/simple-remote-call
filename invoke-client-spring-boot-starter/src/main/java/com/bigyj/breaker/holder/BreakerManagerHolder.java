package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerStateManager;
import com.bigyj.breaker.manager.MetaBreaker;

public interface BreakerManagerHolder {
	/**
	 * 获取BreakerStateManager对象
	 * @param targetName
	 * @return
	 */
	BreakerStateManager get(String targetName, MetaBreaker metaBreaker);

	/**
	 * 管理（存储、更新）BreakerStateManager对象
	 * @param targetName
	 * @param breakerManager
	 */
	void manage(String targetName , BreakerStateManager breakerManager) ;

}

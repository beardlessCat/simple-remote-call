package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerStateManager;

public interface BreakerManagerHolder {

	BreakerStateManager get(String targetName);

	void manage(String targetName , BreakerStateManager breakerManager) ;

	BreakerStateManager create();

}

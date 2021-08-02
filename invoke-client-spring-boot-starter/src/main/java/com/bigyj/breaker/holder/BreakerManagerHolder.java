package com.bigyj.breaker.holder;

import com.bigyj.breaker.manager.BreakerManager;

public interface BreakerManagerHolder {

	BreakerManager get(String targetName);

	void manage(String targetName , BreakerManager breakerManager) ;
}

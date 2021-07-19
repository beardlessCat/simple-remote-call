package com.bigyj.supplier;

import com.bigyj.common.entity.AccessToken;

public interface AccessTokenSupplier {
	public AccessToken get() throws InterruptedException;
}

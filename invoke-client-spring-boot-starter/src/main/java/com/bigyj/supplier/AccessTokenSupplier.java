package com.bigyj.supplier;

import com.bigyj.common.entity.AccessToken;

public interface AccessTokenSupplier {
	//获取accessTokem
	public AccessToken getAccessToken() throws InterruptedException;
	//刷新accessToken
	public AccessToken refreshAccessToken() throws InterruptedException;

	public void monitor();
}

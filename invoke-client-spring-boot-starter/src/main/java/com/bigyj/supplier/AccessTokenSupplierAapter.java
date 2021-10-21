package com.bigyj.supplier;

import com.bigyj.common.entity.AccessToken;

public class AccessTokenSupplierAapter extends MemoryAccessTokenSupplier implements AccessTokenSupplier{
    @Override
    public AccessToken getAccessToken() {
        return super.getAccessToken();
    }

    @Override
    public AccessToken refreshAccessToken() {
        return super.refreshAccessToken();
    }
}

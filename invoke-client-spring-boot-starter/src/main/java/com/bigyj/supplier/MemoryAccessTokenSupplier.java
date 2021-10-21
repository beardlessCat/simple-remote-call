package com.bigyj.supplier;

import com.bigyj.common.entity.AccessToken;

public class MemoryAccessTokenSupplier extends AbstractAccessTokenSupplier{
    @Override
    public AccessToken getAccessToken() {
        return null;
    }

    @Override
    public AccessToken refreshAccessToken() {
        return null;
    }
}

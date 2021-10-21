package com.bigyj.supplier;

import com.bigyj.common.entity.AccessToken;

/**
 * 定义AbstractAccessTokenSupplier抽象类，公告逻辑可直接写在改类中，
 * 抽象方法由子类去实现，公共方法由抽象类中非抽象放实现
 */
public abstract class AbstractAccessTokenSupplier implements AccessTokenSupplier{

    public abstract AccessToken getAccessToken() throws InterruptedException;


    public abstract AccessToken refreshAccessToken() throws InterruptedException;

    /**
     * 监控方法
     */
    @Override
    public void monitor(){

    }
}

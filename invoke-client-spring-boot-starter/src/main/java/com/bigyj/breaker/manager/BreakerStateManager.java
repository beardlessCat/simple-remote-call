package com.bigyj.breaker.manager;

import com.bigyj.breaker.state.BreakerState;
import com.bigyj.breaker.state.ClosedState;
import com.bigyj.breaker.state.HalfOpenState;
import com.bigyj.breaker.state.OpenState;
import lombok.Data;

@Data
public class BreakerStateManager {
    /**
     * 失败次数
     */
    private int failCount ;

    /**
     * 成功次数
     */
    private int successCount ;

    /**
     * 接口熔断时间
     */
    private long closeAt;

    /**
     * 最大失败次数
     */
    private int maxFailCount ;

    /**
     * 最大成功次数
     */
    private int maxSuccessCount;
    /**
     *半恢复尝试次数
     */
    private int openRetryCount ;
    /**
     * 半恢复最大尝试次数，达到最大次数后，变为OPEN状态
     */
    private int maxOpenRetryCount ;
    /**
     * 当前状态
     */
    private BreakerState breakerState ;

    public BreakerStateManager(int failCount, int successCount, long closeAt, int maxFailCount, int maxSuccessCount, int openRetryCount, int maxOpenRetryCount) {
        this.failCount = failCount;
        this.successCount = successCount;
        this.closeAt = closeAt;
        this.maxFailCount = maxFailCount;
        this.maxSuccessCount = maxSuccessCount;
        this.openRetryCount = openRetryCount;
        this.toCloseStatus();
    }

    public void toOpenStatus(){
        this.breakerState = new OpenState(this);
        System.out.println("【断路器变为OPEN】");
    }

    /**
     * 变为CLOSE状态
     */

    public void toCloseStatus(){
        this.breakerState = new ClosedState(this);
        System.out.println("【断路器变为CLOSE】");
    }

    /**
     * 变为HALFOPEN状态
     */
    public void toHalfOpenStatus(){
        this.breakerState = new HalfOpenState(this);
        System.out.println("【断路器变为HALF-OPEN】");
    }

    public void clear() {
        this.failCount = 0 ;
        this.successCount = 0;
        this.openRetryCount = 0;
        this.closeAt = 0;
    }

    /**
     * 是否关闭
     * @return
     */
    public boolean isClosed() {
        return breakerState instanceof ClosedState;
    }

    /**
     * 是否开启
     * @return
     */
    public boolean isOpen() {
        return breakerState instanceof OpenState;
    }

    /**
     * 是否半开启
     * @return
     */
    public boolean isHalfOpen() {
        return breakerState instanceof HalfOpenState;
    }
    public synchronized void  addRetryCount(){
        this.openRetryCount++;
        if(this.openRetryCount>=maxOpenRetryCount){
            this.toOpenStatus();
            this.openRetryCount = 0;
            long closeAt = System.currentTimeMillis();
            this.closeAt = closeAt;
        }
    }

    public synchronized void  addFailCount(){
        this.failCount++ ;
        //fixme 有点臃肿，尝试寻找更加简洁的方式（当前状态为close时才打开）
        if(this.failCount>=this.maxFailCount){
            if(this.isClosed()){
                this.toOpenStatus();
                //记录当前开始熔断时刻
                long closeAt = System.currentTimeMillis();
                this.closeAt = closeAt;
            }
        }
    }

    public synchronized void addSuccessCount(){
        this.successCount++ ;
        if(this.successCount>this.maxSuccessCount){
            toCloseStatus();
            this.closeAt = 0;
            this.successCount=0;
            this.failCount=0;
        }
    }
}

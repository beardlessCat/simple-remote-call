package com.bigyj.breaker.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.bigyj.breaker.exception.MethodNotAvailableException;
import com.bigyj.breaker.state.BreakerState;
import com.bigyj.breaker.state.ClosedState;
import com.bigyj.breaker.state.OpenState;
import com.bigyj.breaker.utils.SpringUtils;
import com.bigyj.breaker.state.HalfOpenState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
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
     * 接口熔断最长时间
     */
    private long maxOpenToTryTime;

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
    /**
     * Breaker相关参数
     */
    private MetaBreaker metaBreaker ;

    public BreakerStateManager(int failCount, int successCount,int openRetryCount, MetaBreaker  metaBreaker) {
        this.failCount = failCount;
        this.successCount = successCount;
        this.maxOpenToTryTime = metaBreaker.getMaxOpenToTryTime();
        this.maxFailCount = metaBreaker.getMaxFailCount();
        this.maxSuccessCount = metaBreaker.getMaxSuccessCount();
        this.openRetryCount = openRetryCount;
        this.maxOpenRetryCount = metaBreaker.getMaxOpenRetryCount();
        this.metaBreaker = metaBreaker;
        this.toCloseStatus();
    }

    public void toOpenStatus(){
        this.breakerState = new OpenState(this);
        this.successCount = 0;
        this.openRetryCount = 0;
        logger.error("【断路器变为OPEN】");
    }

    /**
     * 变为CLOSE状态
     */

    public void toCloseStatus(){
        this.breakerState = new ClosedState(this);
        this.clear();
        logger.error("【断路器变为CLOSE】");
    }

    /**
     * 变为HALFOPEN状态
     */
    public void toHalfOpenStatus(){
        this.breakerState = new HalfOpenState(this);
        logger.error("【断路器变为HALF-OPEN】");
    }

    public void clear() {
        this.failCount = 0 ;
        this.successCount = 0;
        this.openRetryCount = 0;
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

    /**
     * 增加重试次数
     */
    private void addRetryCount(){
        this.openRetryCount++;
        if(this.openRetryCount>=maxOpenRetryCount){
            this.toOpenStatus();
            this.openRetryCount = 0;
        }
    }

    private void  addFailCount(){
        this.failCount++ ;
    }

    /**
     * 增加连续成功次数
     */
    private void addSuccessCount(){
        this.successCount++ ;
    }

    /**
     * 增加成功次数（外部调用）
     */
    public synchronized void increaseSuccessCount(){
        this.addSuccessCount(); ;
    };

    /**
     * 增加时报次数（外部调用）
     */
    public synchronized void increaseFailureCount(){
        this.addFailCount();
    };

    /**
     * 增加重试次数（外部调用）
     */
    public synchronized void increaseRetryCount(){
        this.addRetryCount();
    };

    /**
     * 重置成功次数
     */
    public void resetSuccessCount() {
        this.successCount = 0 ;
    }

    /**
     * 是否达到连续成功次数
     * @return
     */
    public boolean successCountReached() {
        return this.successCount>=this.getMaxSuccessCount();
    }

    /**
     * 是否达到最大失败重试次数
     * @return
     */
    public boolean failRetryCountReached() {
        return this.openRetryCount>=this.maxOpenRetryCount;
    }

    /**
     * 是否达到最大失败测试
     * @return
     */
    public boolean failCountReached() {
        return this.failCount>=this.maxFailCount;
    }

    /**
     * 执行fallBack方法
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object fallbackCall() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(!this.metaBreaker.isIfFallback()){
            throw new MethodNotAvailableException("服务已熔断，请稍等重试！");
        }
        Class<?> fallback = metaBreaker.getFallback();
        String name = metaBreaker.getName();
        Object bean = SpringUtils.getBean(fallback);
        Object[] args = metaBreaker.getArgs();
        Method method = fallback.getDeclaredMethod(name,args[0].getClass());
        Object result = method.invoke(bean, args);
        return result;
    }
}
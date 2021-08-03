package com.bigyj.aspect;

import java.lang.reflect.Method;
import java.security.SecureRandom;

import com.bigyj.annotation.BreakerCommand;
import com.bigyj.breaker.Breaker;
import com.bigyj.breaker.holder.BreakerManagerHolder;
import com.bigyj.breaker.manager.BreakerManager;
import com.bigyj.exception.MethodNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class BreakerCommandAspect {
	private static final String MESSAGE = "method in not available!";

	@Value("${breaker.maxOpenToTryTime}")
	private int maxOpenToTryTime ;

	@Value("${breaker.maxFailCount}")
	private int maxFailCount;

	@Value("${breaker.maxSuccessCount}")
	private int maxSuccessCount;

	@Value("${breaker.maxOpenRetryCount}")
	private int maxOpenRetryCount;

	@Autowired
	private BreakerManagerHolder breakerManagerHolder ;

	@Pointcut("@annotation(com.bigyj.annotation.BreakerCommand)")
	public void retryPointCut() {
	}

	@Around("retryPointCut()")
	public Object retryAdvice(ProceedingJoinPoint point) throws Throwable {
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		String methodName = method.getDeclaringClass().getName() + "." + method.getName();
		Object[] args = point.getArgs();
		Object result = null;
		BreakerCommand breakerCommand = method.getAnnotation(BreakerCommand.class);
		if(maxOpenToTryTime<0){
			maxOpenToTryTime = breakerCommand.maxOpenToTryTime();
		}
		if(maxFailCount<0){
			maxFailCount = breakerCommand.maxFailCount();
		}
		if(maxSuccessCount<0){
			maxSuccessCount = breakerCommand.maxSuccessCount();
		}
		if(maxOpenRetryCount<0){
			maxOpenRetryCount = breakerCommand.maxSuccessCount();
		}
		BreakerManager breakerManager = breakerManagerHolder.get(methodName);
		if(breakerManager == null){
			breakerManager = new BreakerManager(0,0,0,maxFailCount,maxSuccessCount, 0,maxOpenRetryCount, Breaker.BreakStatus.CLOSE);
			result = openCall(point,args,methodName,breakerManager);
		}else {
			logger.error("当前接口熔断器状态{}",breakerManager.toString());
			Breaker.BreakStatus currentStatus = breakerManager.getCurrentStatus();
			long closeAt = breakerManager.getCloseAt();
			if(currentStatus== Breaker.BreakStatus.CLOSE){
				//断路器CLOSE状态，直接进行接口调用
				result = openCall(point,args,methodName,breakerManager);
			}else if(currentStatus== Breaker.BreakStatus.HALFOPEN){
				SecureRandom secureRandom = new SecureRandom();
				//半熔点时，部分接口进行放行
				int x = secureRandom.nextInt(10);
				if(x>5){
					//半恢复，随机尝试一次，
					logger.error("接口半恢复，进行接口调用尝试！");
					result = openCall(point,args,methodName,breakerManager);
				}else {
					//直接返回失败结果，不再增加失败次数
					logger.error("接口半恢复，不进行接口调用！");
					throw new MethodNotAvailableException(MESSAGE);
				}
			}else {
				//熔断,增加失败次数(加锁处理)
				long now = System.currentTimeMillis();
				if(now - closeAt >= maxOpenToTryTime){
					logger.error("接口熔断时间到达最大值，开始尝试调用接口");
					result = openCall(point,args,methodName,breakerManager);
				}else {
					logger.error("接口已经熔断，不再进行接口调用");
				}
			}
		}
		return result ;
	}

	private Object openCall(ProceedingJoinPoint point, Object[] args, String methodName, BreakerManager breakerManager) throws Throwable {
		Object result = null;
		try {
			result = point.proceed(args);
			if(breakerManager.getCurrentStatus() != Breaker.BreakStatus.CLOSE){
				if(breakerManager.getCurrentStatus() == Breaker.BreakStatus.OPEN){
					breakerManager.toHalfOpenStatus();
				}
				breakerManager.addSuccessCount();
			}
		}catch (Throwable throwable){
			breakerManager.addFailCount();
			//若当前状态为半恢复，增加重试次数
			if(breakerManager.getCurrentStatus() == Breaker.BreakStatus.HALFOPEN){
				breakerManager.addRetryCount();
			}
			breakerManagerHolder.manage(methodName,breakerManager);
			throw throwable;
		}
		return result ;
	}

}

package com.bigyj.aspect;

import com.bigyj.annotation.BreakerCommand;
import com.bigyj.breaker.holder.BreakerManagerHolder;
import com.bigyj.breaker.manager.BreakerStateManager;
import com.bigyj.breaker.state.BreakerState;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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
		BreakerStateManager breakerStateManager = breakerManagerHolder.get(methodName);
		BreakerState breakerState = breakerStateManager.getBreakerState();
		//判断当前接口是否能够调用（断路器是否开启）【区分open、half-open】
		breakerState.methodIsAboutToBeCalled();
		try {
			result = point.proceed(args);
			//正常执行
		}catch (Exception e){
			//发生异常
			breakerState.actException();
		}
		breakerState.actSuccess();
		/*if(breakerStateManager == null){
			breakerStateManager = new BreakerStateManager(0,0,maxOpenToTryTime,maxFailCount,maxSuccessCount, 0,maxOpenRetryCount);
			result = openCall(point,args,methodName,breakerStateManager);
		}else {
			logger.error("当前接口熔断器状态{}",breakerStateManager.toString());
			if(breakerStateManager.isClosed()){
				//断路器CLOSE状态，直接进行接口调用
				result = openCall(point,args,methodName,breakerStateManager);
			}else if(breakerStateManager.isHalfOpen()){
				SecureRandom secureRandom = new SecureRandom();
				//半熔点时，部分接口进行放行(限流)
				int x = secureRandom.nextInt(10);
				if(x>5){
					//半恢复，随机尝试一次，
					logger.error("接口半恢复，进行接口调用尝试！");
					result = openCall(point,args,methodName,breakerStateManager);
				}else {
					//直接返回失败结果，不再增加失败次数
					logger.error("接口半恢复，不进行接口调用！");
					throw new MethodNotAvailableException(MESSAGE);
				}
			}else {
				logger.error("接口已经熔断，不再进行接口调用");
				throw new MethodNotAvailableException(MESSAGE);
			}
		}*/
		/**
		 * 及时同步数据
		 */
		breakerManagerHolder.manage(methodName,breakerStateManager);
		return result ;
	}

	private Object openCall(ProceedingJoinPoint point, Object[] args, String methodName, BreakerStateManager breakerManager) throws Throwable {
		Object result = null;
		try {
			result = point.proceed(args);
			if(breakerManager.isHalfOpen()){
				breakerManager.addSuccessCount();
			}
		}catch (Throwable throwable){
			breakerManager.addFailCount();
			//若当前状态为半恢复，增加重试次数
			if(breakerManager.isHalfOpen()){
				breakerManager.addRetryCount();
			}
			breakerManagerHolder.manage(methodName,breakerManager);
			throw throwable;
		}
		return result ;
	}

}

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
			maxOpenRetryCount = breakerCommand.maxOpenRetryCount();
		}
		BreakerStateManager breakerStateManager = breakerManagerHolder.get(methodName);
		BreakerState breakerState = breakerStateManager.getBreakerState();
		//判断当前接口是否能够调用（断路器是否开启）
		breakerState.methodIsAboutToBeCalled();
		try {
			result = point.proceed(args);
			//正常执行
		}catch (Exception e){
			logger.error("接口调用失败！");
			//发生异常区分
			breakerState.actException();
		}
		breakerState.actSuccess();
		return result ;
	}
}

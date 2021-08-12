package com.breaker.aspect;

import java.lang.reflect.Method;

import com.breaker.annotation.BreakerCommand;
import com.breaker.holder.BreakerManagerHolder;
import com.breaker.manager.BreakerStateManager;
import com.breaker.manager.MetaBreaker;
import com.breaker.state.BreakerState;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.beans.factory.annotation.Autowired;

@Aspect
@Slf4j
public class BreakerCommandAspect {
	@Autowired
	private BreakerManagerHolder breakerManagerHolder ;

	@Pointcut("@annotation(com.breaker.annotation.BreakerCommand)")
	public void retryPointCut() {
	}

	@Around("retryPointCut()")
	public Object retryAdvice(ProceedingJoinPoint point) throws Throwable {
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		String methodName = method.getDeclaringClass().getName() + "." + method.getName();
		Object[] args = point.getArgs();
		Object result = null;
		BreakerCommand breakerCommand = method.getAnnotation(BreakerCommand.class);
		int maxOpenToTryTime = breakerCommand.maxOpenToTryTime();
		int maxFailCount = breakerCommand.maxFailCount();
		int maxSuccessCount = breakerCommand.maxSuccessCount();
		int maxOpenRetryCount = breakerCommand.maxOpenRetryCount();
		Class<?> fallback = breakerCommand.fallback();
		MetaBreaker metaBreaker = MetaBreaker.builder()
				.maxOpenRetryCount(maxOpenRetryCount)
				.maxOpenToTryTime(maxOpenToTryTime)
				.maxFailCount(maxFailCount)
				.maxSuccessCount(maxSuccessCount)
				.ifFallback(fallback==null ? false:true)
				.fallback(fallback)
				.args(args)
				.name(method.getName())
				.build();
		BreakerStateManager breakerStateManager = breakerManagerHolder.get(methodName,metaBreaker);
		logger.error(breakerStateManager.toString());
		BreakerState breakerState = breakerStateManager.getBreakerState();
		//判断当前接口是否能够调用（断路器是否开启）
		breakerState.methodIsAboutToBeCalled();
		try {
			result = point.proceed(args);
			//正常执行
			breakerState.actSuccess();
		}catch (Exception e){
			//发生异常区分
			breakerState.actException();
		}
		return result ;
	}

}

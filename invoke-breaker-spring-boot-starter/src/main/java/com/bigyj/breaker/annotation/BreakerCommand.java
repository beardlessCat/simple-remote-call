package com.bigyj.breaker.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bigyj.breaker.constant.BreakerManagerConfigConstant;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BreakerCommand {
	/**
	* 最大失败次数
	*/
	int maxFailCount() default BreakerManagerConfigConstant.MAX_FAIL_COUNT;

	/**
	 * 半恢复后接口重试最大连续成功次数
	 */
	int maxSuccessCount() default BreakerManagerConfigConstant.MAX_SUCCESS_COUNT;

	/**
	 * 熔断后接口重试最大时间
	 */
	int maxOpenToTryTime() default  BreakerManagerConfigConstant.MAX_OPEN_TO_TRY_TIME;

	/**
	 * 接口熔后，到达最大口重试最大时间后，最多尝试次数
	 * @return
	 */
	int maxOpenRetryCount() default  BreakerManagerConfigConstant.MAX_OPEN_RETRY_COUNT;

	/**
	 * 接口熔断后，调用的方法
	 * @return
	 */
	Class<?> fallback() default void.class;

}

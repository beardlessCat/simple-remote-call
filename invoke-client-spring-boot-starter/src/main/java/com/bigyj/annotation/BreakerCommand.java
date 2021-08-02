package com.bigyj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BreakerCommand {
	/**
	* 最大失败次数
	*/
	int maxFailCount() default 10;

	/**
	 * 熔断后接口重试最大时间
	 */
	int maxSuccessCount() default 5;

	/**
	 * 熔断后接口重试最大时间
	 */
	int maxCloseToTryTime() default  5000;
}

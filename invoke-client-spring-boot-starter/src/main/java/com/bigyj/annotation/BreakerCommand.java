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
	int maxFailCount() default 3;

	/**
	 * 半恢复后接口重试最大成功次数
	 */
	int maxSuccessCount() default 5;

	/**
	 * 熔断后接口重试最大时间
	 */
	int maxOpenToTryTime() default  10*1000;

	/**
	 * 接口熔后，到达最大口重试最大时间后，最多尝试次数
	 * @return
	 */
	int maxOpenRetryCount() default  5;
}

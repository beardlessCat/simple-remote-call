package com.bigyj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpMethod;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  InvokeRequest
{

	@AliasFor("path")
	String value() default "";

	/**
	 * 请求方法
	 * @return
	 */
	HttpMethod method() default HttpMethod.POST;

	/**
	 * 是否携带accessToken
	 * @return
	 */
	boolean withAccessToken() default true;

	/**
	 * 重试次数
	 * @return
	 */
	int maxAttempts() default 0;
}

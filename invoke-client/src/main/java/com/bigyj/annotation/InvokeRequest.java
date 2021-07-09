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

	@AliasFor("value")
	String path() default "";

	HttpMethod method() default HttpMethod.POST;

	boolean withAccessToken() default true;

	int maxAttempts() default 0;
}

package com.bigyj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InvokeClient {
	@AliasFor("path")
	String value() default "";

	@AliasFor("value")
	String path() default "";

	/**
	 * 客户端调用配置类
	 * @return
	 */
	Class<?>[] configuration() default {};
}

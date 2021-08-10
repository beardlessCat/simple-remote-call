package com.bigyj.breaker.manager;

import lombok.Getter;

@Getter
public class MetaBreaker {
	private final int maxOpenToTryTime ;
	private final int maxFailCount;
	private final int maxSuccessCount ;
	private final int maxOpenRetryCount;
	private final Class<?> fallback ;
	private final Object[] args ;
	private final String name ;
	private final boolean ifFallback ;
	private MetaBreaker(Builder builder) {
		this.maxOpenToTryTime = builder.maxOpenToTryTime ;
		this.maxFailCount = builder.maxFailCount ;
		this.maxSuccessCount = builder.maxSuccessCount ;
		this.maxOpenRetryCount = builder.maxOpenRetryCount; ;
		this.fallback = builder.fallback;
		this.args =builder.args;
		this.name =builder.name;
		this.ifFallback = builder.ifFallback;
	}

	public static Builder builder() {
		return new Builder();
	}
	public static final class Builder {
		private int maxOpenToTryTime ;
		private int maxFailCount;
		private int maxSuccessCount ;
		private int maxOpenRetryCount;
		private Class<?> fallback ;
		private Object[] args ;
		private String name ;
		private boolean ifFallback ;
		public Builder args(Object[] args){
			this.args = args ;
			return this;
		}

		public Builder name(String name){
			this.name = name ;
			return this;
		}
		public Builder fallback(Class<?> fallback){
			this.fallback = fallback ;
			return this;
		}
		public Builder maxOpenToTryTime(int maxOpenToTryTime){
			this.maxOpenToTryTime = maxOpenToTryTime ;
			return this;
		}

		public Builder maxFailCount(int maxFailCount){
			this.maxFailCount = maxFailCount ;
			return this;
		}

		public Builder maxSuccessCount(int maxSuccessCount){
			this.maxSuccessCount = maxSuccessCount ;
			return this;
		}

		public Builder maxOpenRetryCount(int maxOpenRetryCount){
			this.maxOpenRetryCount = maxOpenRetryCount ;
			return this;
		}
		public Builder ifFallback(boolean ifFallback){
			this.ifFallback = ifFallback ;
			return this;
		}
		public MetaBreaker build() {
			return new MetaBreaker(this);
		}
	}
}

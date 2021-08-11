package com.bigyj.breaker.exception;

public class MethodNotAvailableException extends RuntimeException {
	public MethodNotAvailableException(String message) {
		super(message);
	}
}

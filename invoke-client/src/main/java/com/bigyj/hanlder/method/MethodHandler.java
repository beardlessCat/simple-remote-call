package com.bigyj.hanlder.method;

public interface MethodHandler {
	long tryWaitMILLIS = 1000;
	Object invoke(Object[] args) throws Throwable;
}

package com.bigyj.hanlder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.bigyj.hanlder.method.MethodHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInvocationHandler implements InvocationHandler {
	private Map<Method, MethodHandler> dispatch ;
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//获取自定义的MethodHandler,进行远程方法调用
		return dispatch.get(method).invoke(args);
	}
}

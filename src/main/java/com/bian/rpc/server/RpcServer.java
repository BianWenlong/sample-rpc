package com.bian.rpc.server;

import java.lang.reflect.Method;

public class RpcServer {
	
	public static Object rpcInvoke(final Object service,String methodName,Class<?>[] paramtypes,Object[] arguments){
		try {
			Method m=service.getClass().getMethod(methodName, paramtypes);
			Object o=m.invoke(service, arguments);	
			return o;
		} catch (Throwable t) {
			return t;
		}
	}
}

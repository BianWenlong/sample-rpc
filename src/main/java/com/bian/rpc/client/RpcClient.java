package com.bian.rpc.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RpcClient {

	@SuppressWarnings("unchecked")
	public static <T> T rpcRefer(final Class<T> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class<?>[] { interfaceClass }, new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						Socket s=null;
						Object result = null;
						try {
							s = new Socket("127.0.0.1", 54330);
							OutputStream aos = s.getOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(aos);
							oos.writeUTF(method.getName());
							oos.writeObject(method.getParameterTypes());
							oos.writeObject(args);
							ObjectInputStream in = new ObjectInputStream(s
									.getInputStream());
							result = in.readObject();
							if (result instanceof Throwable) {
								throw (Throwable) result;
							}
						} finally {
							s.close();
						}
						return result;
					}

				});
	}
}

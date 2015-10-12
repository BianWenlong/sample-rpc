package com.bian.rpc.client;

import java.io.ByteArrayOutputStream;
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
							ByteArrayOutputStream bos=new ByteArrayOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(bos);
							oos.writeUTF(method.getName());
							oos.writeObject(method.getParameterTypes());
							oos.writeObject(args);
							byte[] oldBytes=bos.toByteArray();
							byte[] newBytes=new byte[oldBytes.length+4];
							for(int i=0;i!=oldBytes.length;++i){
								newBytes[i+4]=oldBytes[i];
							}
							int length=newBytes.length;
							newBytes[0]=(byte)(length&0xff);
							newBytes[1]=(byte)((length>>8)&0xff);
							newBytes[2]=(byte)((length>>16)&0xff);
							newBytes[3]=(byte)((length>>24)&0xff);
							aos.write(newBytes);
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

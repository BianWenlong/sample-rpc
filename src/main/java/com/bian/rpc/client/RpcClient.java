package com.bian.rpc.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClients;

public class RpcClient {
	 
	public static<T> T rpcRefer(final Class<T> interfaceClass){
		return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler(){
			HttpClient httpClient=HttpClients.createDefault();
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				HttpPost post=new HttpPost("localhost:8080/rpc/add");
				ByteArrayOutputStream aos=new ByteArrayOutputStream();
				ObjectOutputStream oos=new ObjectOutputStream(aos);
				oos.writeUTF(method.getName());
				oos.writeObject(method.getParameterTypes());
				oos.writeObject(args);
				InputStreamEntity entity=new InputStreamEntity(new ByteArrayInputStream(aos.toByteArray()));
				post.setEntity(entity);
				HttpResponse response=httpClient.execute(post);
				HttpEntity e=null;
				e=response.getEntity();
				ObjectInputStream in=new ObjectInputStream(e.getContent());
				Object result=in.readInt();
				if(result instanceof Throwable){
					throw (Throwable) result;
				}
				return result;
			}
			
		}) ;
	}
}

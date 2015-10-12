package com.bian.rpc.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bian.rpc.service.impl.RpcAddImpl;

public class RpcServer {
	private static final Logger logger = Logger.getLogger(RpcServer.class);

	private static final int DEFAULT_PORT = 8888;
	
	public static final byte[] doInvoke(byte[] receives) throws IOException, ClassNotFoundException{
		byte[] data=new byte[receives.length-4];
		for(int i=0;i!=data.length;++i){
			data[i]=receives[i+4];
		}
		InputStream in=new ByteArrayInputStream(data);
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		ObjectInputStream ois=new ObjectInputStream(in);
		String methodName=ois.readUTF();
		Class[] paramtypes=(Class[]) ois.readObject();
		Object[] arguments=(Object[]) ois.readObject();
		Object result=rpcInvoke(new RpcAddImpl(),methodName,paramtypes,arguments);
		oos.writeObject(result);
		byte[] send=bos.toByteArray();
		return send;
	}
	public static Object rpcInvoke(final Object service, String methodName,
			Class<?>[] paramtypes, Object[] arguments) {
		try {
			Method m = service.getClass().getMethod(methodName, paramtypes);
			Object o = m.invoke(service, arguments);
			return o;
		} catch (Throwable t) {
			return t;
		}
	}

	public static void start(final int port) throws IOException,
			ClassNotFoundException {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ServerSocketChannel serverChannel=ServerSocketChannel.open();
					serverChannel.bind(new InetSocketAddress(port));
					Selector selector=Selector.open();
					serverChannel.configureBlocking(false);
					serverChannel.register(selector,SelectionKey.OP_ACCEPT,new ConnectHandler());
					while(true){
						selector.select();
						Set<SelectionKey> keys=selector.selectedKeys();
						Iterator<SelectionKey> it=keys.iterator();
						while(it.hasNext()){
							SelectionKey key=it.next();
							Handler handler=(Handler)key.attachment();
							handler.doService(key);
							it.remove();
						}
					}
				} catch (IOException e) {
					logger.error("网络错误"+e.getMessage());
				}
			}

		});
		t.start();
	}
}

package com.bian.rpc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.bian.rpc.service.impl.RpcAddImpl;

public class RpcServer {
	private static final Logger logger = Logger.getLogger(RpcServer.class);

	private static final int DEFAULT_PORT = 8888;

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
					final ServerSocket serverSocket = new ServerSocket(port);
					while (true) {
						Socket socket = serverSocket.accept();
						if (socket == null) {
							logger.error("连接错误");
							continue;
						}
						InputStream in = socket.getInputStream();
						ObjectInputStream oin = new ObjectInputStream(in);
						String methodName = oin.readUTF();
						Class<?>[] paramterTypes = (Class<?>[]) oin
								.readObject();
						Object[] arguments = (Object[]) oin.readObject();
						Object result = rpcInvoke(new RpcAddImpl(), methodName,
								paramterTypes, arguments);
						ObjectOutputStream oout = new ObjectOutputStream(socket
								.getOutputStream());
						oout.writeObject(result);
					}
				} catch (Exception e) {
					logger.error("error"+e.getMessage());
				}
			}

		});
		t.start();
	}
}

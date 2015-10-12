package com.bian.rpc.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ConnectHandler implements Handler{

	@Override
	public void doService(SelectionKey key) {
		ServerSocketChannel serverSocketChannel =(ServerSocketChannel) key.channel();
		try {
			SocketChannel sc=serverSocketChannel.accept();
			sc.configureBlocking(false);
			sc.register(key.selector(), SelectionKey.OP_READ,new ReadHandler());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

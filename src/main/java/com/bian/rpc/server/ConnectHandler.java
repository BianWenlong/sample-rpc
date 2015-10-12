package com.bian.rpc.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ConnectHandler implements Handler{

	@Override
	public void doService(SelectionKey key) {
		SocketChannel socketChannel =(SocketChannel) key.channel();
		try {
			socketChannel.configureBlocking(false);
			socketChannel.register(key.selector(), SelectionKey.OP_READ,new ReadHandler());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

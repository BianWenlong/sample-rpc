package com.bian.rpc.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteHandler implements Handler{
	private final byte[] send;
	
	public WriteHandler(byte[] send){
		this.send=send;
	}

	@Override
	public void doService(SelectionKey key) {
		ByteBuffer sendBuffer=ByteBuffer.allocate(send.length);
		sendBuffer.put(send);
		sendBuffer.flip();
		SocketChannel sc=(SocketChannel) key.channel();
		try {
			sc.write(sendBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}

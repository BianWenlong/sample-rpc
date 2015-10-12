package com.bian.rpc.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.bian.rpc.Utils.IntegerToByteUtils;

public class ReadHandler implements Handler{
	
	private byte[] receive=new byte[1024];
	private ByteBuffer buffer=ByteBuffer.allocate(1024);
	private int length=0;
	@Override
	public void doService(SelectionKey key) {
		SocketChannel sc=(SocketChannel) key.channel();
		byte[] send=null;
		try {
			int num=sc.read(buffer);
			if(num!=-1){
				buffer.flip();
				int newLength=buffer.limit();
				if(length+newLength>receive.length){
					resize();
				}
				buffer.get(receive, length, newLength);
				length+=newLength;
				buffer.clear();	
				if(isReadEnd()){
					send=RpcServer.doInvoke(receive);
					sc.register(key.selector(), SelectionKey.OP_WRITE, new WriteHandler(send));
				}else{
					sc.register(key.selector(), SelectionKey.OP_WRITE, this);
				}
			}else{
				sc.register(key.selector(), SelectionKey.OP_WRITE, new WriteHandler(send));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isReadEnd(){
		if(length<4){
			return false;
		}
		int receiveLength=IntegerToByteUtils.bytesToInt(receive, 0);
		if(length==receiveLength){
			return true;
		}else if (length>receiveLength){
			throw new RuntimeException("传输数据长度错误");
		}
		return false;
	}
	
	private void resize(){
		byte[] newReceive=new byte[receive.length*2];
		for(int i=0;i!=receive.length;++i){
			newReceive[i]=receive[i];
		}
		receive=newReceive;
	}
}

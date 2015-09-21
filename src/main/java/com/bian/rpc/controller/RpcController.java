package com.bian.rpc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bian.rpc.client.RpcClient;
import com.bian.rpc.server.RpcServer;
import com.bian.rpc.service.api.RpcAdd;
import com.bian.rpc.service.impl.RpcAddImpl;

@Controller
@RequestMapping("/rpc")
public class RpcController {
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public void RpcAdd(HttpServletRequest request,HttpServletResponse response) throws IOException, ClassNotFoundException{
		ObjectInputStream ois=new ObjectInputStream(request.getInputStream());
		String methodName=ois.readUTF();
		Class<?>[] paramterTypes=(Class<?>[]) ois.readObject();
		Object[] arguments=(Object[]) ois.readObject();
		Object o=RpcServer.rpcInvoke(new RpcAddImpl(), methodName, paramterTypes, arguments);
		ObjectOutputStream oos=new ObjectOutputStream(response.getOutputStream());
		oos.writeObject(o);
	}
	
	@RequestMapping(value="rpc")
	@ResponseBody
	public int add(){
		RpcAdd add=RpcClient.rpcRefer(RpcAdd.class);
		int result=add.add(1, 2);
		return result;
	}
}

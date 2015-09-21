package com.bian.rpc.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bian.rpc.server.RpcServer;
import com.bian.rpc.service.impl.RpcAddImpl;

@Controller
@RequestMapping("/rpc")
public class RpcController {
	
	@RequestMapping("/add")
	public void RpcAdd(HttpServletRequest request,HttpServletResponse response) throws IOException, ClassNotFoundException{
		ObjectInputStream ois=new ObjectInputStream(request.getInputStream());
		String methodName=ois.readUTF();
		Class<?>[] paramterTypes=(Class<?>[]) ois.readObject();
		Object[] arguments=(Object[]) ois.readObject();
		Object o=RpcServer.rpcInvoke(new RpcAddImpl(), methodName, paramterTypes, arguments);
		ObjectOutputStream oos=new ObjectOutputStream(response.getOutputStream());
		oos.writeObject(o);
	}
}

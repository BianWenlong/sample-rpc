package com.bian.rpc.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
@Component
public class ServerStart implements ApplicationListener<ApplicationEvent>{
	
	private static final Logger logger=Logger.getLogger(ServerStart.class);
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextRefreshedEvent || event instanceof ContextStartedEvent){
			try {
				RpcServer.start(54330);
			} catch (ClassNotFoundException e) {
				logger.error("找不到API的class");
			} catch (IOException e) {
				logger.error("网络连接错误"+e.getMessage());
			}
		}
	}

}

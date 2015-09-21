package com.bian.rpc.service.impl;

import com.bian.rpc.service.api.RpcAdd;

public class RpcAddImpl implements RpcAdd{

	@Override
	public int add(int a, int b) {
		return a+b;
	}

}

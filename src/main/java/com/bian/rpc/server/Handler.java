package com.bian.rpc.server;

import java.nio.channels.SelectionKey;

public interface Handler {
	public void doService(SelectionKey key);
}

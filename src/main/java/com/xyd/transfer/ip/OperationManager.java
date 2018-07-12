package com.xyd.transfer.ip;

import java.util.Map;

import io.netty.channel.socket.SocketChannel;

public interface OperationManager<T extends OperationProcessor> {
	T getProcessor(SocketChannel channel, String resourceCode, String physicalAddress);
}

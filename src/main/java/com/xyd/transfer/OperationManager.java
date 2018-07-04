package com.xyd.transfer;

import io.netty.channel.socket.SocketChannel;

public interface OperationManager<T extends OperationProcessor> {
	T getProcessor(SocketChannel channel, String resourceCode);
}

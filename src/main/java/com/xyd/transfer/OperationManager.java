package com.xyd.transfer;

import java.util.Map;

import com.xyd.transfer.ip.datapack.ParamType;

import io.netty.channel.socket.SocketChannel;

public interface OperationManager<T extends OperationProcessor> {
	T getProcessor(SocketChannel channel, Map<ParamType, String>params);
}

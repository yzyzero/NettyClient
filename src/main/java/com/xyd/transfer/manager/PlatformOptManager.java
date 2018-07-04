package com.xyd.transfer.manager;

import com.xyd.transfer.OperationManager;

import io.netty.channel.socket.SocketChannel;

public class PlatformOptManager implements OperationManager<PlatformOptProcessor> {

	@Override
	public PlatformOptProcessor getProcessor(SocketChannel channel, String resourceCode) {
		return new PlatformOptProcessor(this, channel, resourceCode);
	}
	
	protected void fireOffline(PlatformOptProcessor processor) {
		
	}

}

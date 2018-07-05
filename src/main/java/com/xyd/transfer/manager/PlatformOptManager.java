package com.xyd.transfer.manager;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyd.dao.TerminalDao;
import com.xyd.model.Terminal;
import com.xyd.transfer.OperationManager;
import com.xyd.transfer.ip.datapack.ParamType;

import io.netty.channel.socket.SocketChannel;

@Service
public class PlatformOptManager implements OperationManager<PlatformOptProcessor> {
	
    @Autowired
    private TerminalDao dao;

	@Override
	public PlatformOptProcessor getProcessor(SocketChannel channel, Map<ParamType,String> params) {
		return new PlatformOptProcessor(this, channel, params);
	}
	
	protected void fireOffline(PlatformOptProcessor processor) {
		
	}
	
	public void writeConfig(String physicalAddress, String resourceCode) {
		List<Terminal> terminals = dao.findById(physicalAddress);
		for(Terminal terminal: terminals) {
			System.out.println(physicalAddress + ": " + resourceCode);
			terminal.setSource(resourceCode);
			dao.save(terminal);
		}
	}

}

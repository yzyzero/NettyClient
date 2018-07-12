package com.xyd.transfer.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyd.resource.dao.TerminalDao;
import com.xyd.resource.model.Terminal;
import com.xyd.transfer.ip.OperationManager;

import io.netty.channel.socket.SocketChannel;

@Service
public class PlatformOptManager implements OperationManager<PlatformOptProcessor> {
	
    @Autowired
    private TerminalDao dao;

	@Override
	public PlatformOptProcessor getProcessor(SocketChannel channel, String resourceCode, String physicalAddress) {
		return new PlatformOptProcessor(this, channel, resourceCode, physicalAddress);
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
	
	public String queryResourceCode(String physicalAddress) {
		String resourceCode = "";
		List<Terminal> terminals = dao.findById(physicalAddress);
		for(Terminal terminal: terminals) {
			resourceCode = terminal.getSource();
			break;
		}
		return resourceCode;
	}
}

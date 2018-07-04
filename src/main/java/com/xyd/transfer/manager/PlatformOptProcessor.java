package com.xyd.transfer.manager;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyd.transfer.OperationProcessor;
import com.xyd.transfer.ip.datapack.OperationType;
import com.xyd.transfer.ip.datapack.PackType;
import com.xyd.transfer.ip.datapack.RawPack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.SocketChannel;

public class PlatformOptProcessor extends OperationProcessor {
	private static final Logger logger = LoggerFactory.getLogger(PlatformOptProcessor.class);
	private final PlatformOptManager m_Manager;
	
	private String physicalAddress = null;
	private String broadcastId = null;
	
	private static AtomicInteger retCount = new AtomicInteger(1);

	public PlatformOptProcessor(PlatformOptManager manager, SocketChannel channel, String resourceCode) {
		super(channel, resourceCode);
		m_Manager = manager;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public String getBroadcastId() {
		return broadcastId;
	}

	@Override
	protected void operate(RawPack rawpack) throws Exception {
		// 解析数据包
    	if(rawpack.getType().equals(PackType.REQUEST)) {
	    	switch (rawpack.getOperation()) {
	    	case TERMINAL_CONFIG:	    		
	    		ByteBuf buf = rawpack.getBuf();
	    		byte[] req = new byte[buf.readableBytes()];
	    		buf.readBytes(req);
	    		String id = Hex.encodeHexString(req).substring(6, 24);
	    		StringBuffer targets = new StringBuffer();
	    		for(String target : rawpack.getTargets()) {
	    			targets.append(target);
	    			targets.append(",");
	    		}
	    		System.out.println( id + ": " + targets.toString() + ": " + retCount.getAndIncrement());
	    		break;
			case HEARTBEAT:
//	        	TerminalHeartbeat hearbeat = new TerminalHeartbeat(rawpack);
//	        	if(physicalAddress == null) { // 建立连接后第一个心跳包，上线处理
//	        		setStatus(hearbeat.getStatus());
//	        		physicalAddress = hearbeat.getPhysicalAddress();
//	        		m_Manager.fireOnline(this);
//	        	} else if(!getStatus().equals(hearbeat.getStatus())) {
//	        		setStatus(hearbeat.getStatus());
//	        		m_Manager.fireStatusChange(this);
//	        	} else if(!physicalAddress.equals(hearbeat.getPhysicalAddress()) || !getCode().equals(hearbeat.getSource())) {
//	        		throw new Exception("接收到与本连接物理地址或资源编码不符的数据！");
//	        	}
				break;
			case TERMINAL_FAILURE_RECOVERY: 
//				m_Manager.fireFailure(this, new TerminalFailureReport(rawpack)); 
				break;
			case TERMINAL_TASK_SWITCH:
				// TODO 处理任务改变
//				TerminalTaskReport task = new TerminalTaskReport(rawpack);
//				if(task.getSign() == 1) {
//					broadcastId = task.getMessageID();
//				} else if(task.getSign() == 2) {
//					broadcastId = null;
//				}
//				m_Manager.fireTask(this, task);
				break;
			case TERMINAL_BROADCAST_REPORT: 
//				m_Manager.fireBroadcastReport(this, new TerminalBroadcastReport(rawpack)); 
				break;
			default:
				logger.error("未定义业务类型：");
			}
    	} else if(rawpack.getType().equals(PackType.RESPONSE)) {
    		// TODO 完成应答数据处理
	    	switch (rawpack.getOperation()) {
//			case TERMINAL_BROADCAST_QUERY:
//				TerminalBroadcast bcRecords = new TerminalBroadcast(rawpack);
//				System.out.println("广播记录" + bcRecords.getResourceCode());
//				break;
//			case TERMINAL_STATUS_QUERY: 
//				TerminalStatus status = new TerminalStatus(rawpack);
//				System.out.println("终端状态参数" + status.getResultCode());
//				break;
			default:
				if(!rawpack.getOperation().equals(OperationType.NONE)) {
					//notifyResponse(new ResponsePack(rawpack)); break;
				}
			}
    	}
	}

	@Override
	protected void fireOffline() {
		m_Manager.fireOffline(this);
	}
}

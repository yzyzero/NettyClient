package com.xyd.transfer.ip;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import com.xyd.transfer.ip.OperationType;
import com.xyd.transfer.ip.datapack.ResponseStatusQuery;
import com.xyd.transfer.ip.parameter.Enumerate;
import com.xyd.transfer.ip.parameter.IPParameter;

public abstract class OperationProcessor extends ChannelInboundHandlerAdapter {
	private final String code;
	private final String regionCode;
	private final SocketChannel channel;
	
	private Status status = Status.OFFLINE;
	
	public OperationProcessor(){
		this.channel = null;
		this.code = null;
		this.regionCode = null;
	}
	
	public OperationProcessor(SocketChannel channel, String resourceCode) {
		this.channel = channel;
		this.code = resourceCode;
		this.regionCode = code.substring(4, 16);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RawPack) {
        	RawPack pack = (RawPack) msg;
        	Exception exception = null;
        	try {
	        	operate(pack);
        	} catch (Exception e) {
        		exception = e;
			}
        	if(PackType.REQUEST.equals(pack.getType())) {
        		switch(pack.getOperation()) {
        			case TERMINAL_STATUS_QUERY:
        				IPParameter parameter = Enumerate.RESOURCE_CODE.createInstance();
        				parameter.setValue(code);
        				ResponseStatusQuery queryResp = new ResponseStatusQuery(pack.getSessionID(), pack.getSource(), parameter);
        				writeByCode(queryResp);
        				break;
        			default:
        	        	ResponsePack response = new ResponsePack(pack.getSessionID(), code, new String[] {pack.getSource()}, pack.getOperation());
        	        	if(exception == null) {
        	        		response.setCode(Error.SUCCEED);
        	        	} else {
        	        		response.setCode(Error.ERROR30);
        	        		response.setDescription(exception.getMessage());
        	        	}
        				writeByCode(response);
        				break;
        		}
        	}

			ReferenceCountUtil.release(pack.getBuf());
        }

        ctx.fireChannelRead(msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		status = Status.OFFLINE;
		ctx.close();
		fireOffline();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
	
	public ChannelFuture close() {
		return channel.close();
	}

	public String getCode() {
		return code;
	}
	
	public String getRegionCode() {
		return regionCode;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ChannelFuture writeByCode(SendPack pack) throws PackEncodeException {
		pack.setTargets(code);
		return writeOriginalCode(pack);
	}
	
	public ChannelFuture writeOriginalCode(SendPack pack) throws PackEncodeException {
		ByteBuf buffer = pack.toBuffer();
		try {
			return channel.writeAndFlush(pack.toBuffer());
		} finally {
			ReferenceCountUtil.release(buffer);
		}
	}
	
	protected abstract void operate(RawPack pack) throws Exception;
	protected abstract void fireOffline();
}

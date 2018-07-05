package com.xyd.transfer;

import java.util.Map;

import com.xyd.transfer.ip.datapack.Error;
import com.xyd.transfer.ip.datapack.ParamType;
import com.xyd.transfer.ip.datapack.RawPack;
import com.xyd.transfer.ip.datapack.ResponsePack;
import com.xyd.transfer.ip.datapack.SendPack;
import com.xyd.transfer.ip.datapack.Status;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

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
	
	public OperationProcessor(SocketChannel channel, Map<ParamType, String> params) {
		this.channel = channel;
		this.code = params.get(ParamType.resourceCode);
		this.regionCode = code.substring(4, 16);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RawPack) {
        	RawPack pack = (RawPack) msg;
        	ResponsePack response = new ResponsePack(pack.getSessionID(), code, new String[] {pack.getSource()}, pack.getOperation());
        	try {
        		operate(pack);
        		response.setCode(Error.SUCCEED);
        	} catch (Exception e) {
        		e.printStackTrace();
        		response.setCode(Error.ERROR30);
        		response.setDescription(e.getMessage());
			}
			ReferenceCountUtil.release(pack.getBuf());
			
			writeByCode(response);
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

package com.xyd.transfer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyd.transfer.ip.datapack.ClientHeartbeat;
import com.xyd.transfer.ip.datapack.ResponsePack;
import com.xyd.transfer.ip.datapack.SendPack;
import com.xyd.transfer.ip.datapack.Status;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private volatile ScheduledFuture<?> heartBeat;
	
	private AtomicInteger sessionid = new AtomicInteger(1);
	private String physicalAddress;
	private String source;
	private String[] targets;
	
	private static AtomicInteger retCount = new AtomicInteger(1);
	private static AtomicInteger sendCount = new AtomicInteger(1);
	
	public HeartBeatReqHandler(String physicalAddress, String source, String[] targets) {
		this.physicalAddress = physicalAddress;
		this.source = source;
		this.targets = targets;
//		logger.info("new HeartBeatReqHandler()");
	}
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//    	System.out.println("channelRead");
    	if(!(msg instanceof ByteBuf)){
    		logger.error("msg类型错误，不是ByteBuf.");
    		return;
    	}
        ByteBuf buf = (ByteBuf) msg;
        try {
        	byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            System.out.println(Hex.encodeHexString(req) + ": " + physicalAddress + ": " + retCount.getAndIncrement());
            ctx.fireChannelRead(msg);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
        	//ReferenceCountUtil.release(buf);//防止内存溢出
        	//buf.release();//防止内存溢出
        	//ctx.close();//长连接不要关闭，短连接才关闭
        }
        //ResponsePack response = new ResponsePack(pack.getSessionID(), code, new String[] {pack.getSource()}, pack.getOperation());
        //ctx.fireChannelRead(msg);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		logger.info("------------------------------------------------------");
		System.out.println("channelActive. "+sendCount.getAndIncrement());
		heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.SECONDS);
	}
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//    	logger.info("channelInactive");
    	if(heartBeat!=null) {
    		heartBeat.cancel(true);
    		heartBeat = null;
    	}
//    	logger.info("------------------------------------------------------");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	logger.error("client caught exception", cause);
    	if(heartBeat!=null) {
    		heartBeat.cancel(true);
    		heartBeat = null;
    	}
    	ctx.fireExceptionCaught(cause);
    }
    
    private class HeartBeatTask implements Runnable{
    	private final ChannelHandlerContext ctx;
    	
    	public HeartBeatTask(final ChannelHandlerContext ctx) {
    		this.ctx = ctx;
    	}

		@Override
		public void run() {
//			System.out.println("HeartBeat Begin...");
			sendHeartBeat();
//			System.out.println("HeartBeat End. ");
		}
		
//		public void buildHeartBeat() {
//			
//		}
		
		private void sendHeartBeat() {
			int sessionID = sessionid.getAndIncrement();
			byte first = 1;
			
			if(source!=null && targets!=null &&physicalAddress!=null) {
				SendPack pack = new ClientHeartbeat(sessionID, source, targets, Status.IDLE, first, physicalAddress);
				try {
//					if(ctx.channel().isActive()) {
//					}else {
//						System.out.println("socketChannel is not active.");
//					}
					ByteBuf msg = pack.toBuffer();
					ctx.writeAndFlush(msg);
				} catch (PackEncodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				StringBuffer buf = new StringBuffer();
				buf.append("参数错误: ");
				buf.append("source=");
				buf.append(source);
				buf.append("; targets=");
				if(targets!=null && targets.length > 0) {
					for(String target: targets) {
						buf.append(target);
						buf.append(",");
					}
				}
				buf.append("; physicalAddress=");
				buf.append(physicalAddress);
				System.out.println(buf.toString());
			}
		}
    }
}

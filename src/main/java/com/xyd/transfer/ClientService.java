package com.xyd.transfer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyd.model.Category;
import com.xyd.model.Terminal;
import com.xyd.transfer.ip.datapack.ParamType;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
//import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ResourceLeakDetector;

public class ClientService implements Runnable {
	private Logger logger = LoggerFactory.getLogger(getClass());

	//private Terminal terminal = new Terminal();
	private String host;
	private Integer port;
	private String physicalAddress;
	private String source;
	private String targets;
	private boolean startup;
	
	private Map<Category, OperationManager<?>> m_EventHandlers;
	Map<ParamType,String> params = new ConcurrentHashMap<ParamType, String>();

//	private int timeoutSeconds = 30; // Unit: Second

	// private ClientService() {
	//
	// }

	public ClientService(String host, Integer port, String physicalAddress, String source, String targets,
			boolean startup) {
		this.host = host;
		this.port = port;
		this.physicalAddress = physicalAddress;
		this.source = source;
		this.targets = targets;
		this.startup = startup;
		
		params.put(ParamType.physicalAddress, physicalAddress);
		params.put(ParamType.source, source);
		params.put(ParamType.targets, targets);
	}
	
	public void setEventHandler(Map<Category, OperationManager<?>> handlers) {
		this.m_EventHandlers = handlers;
	}

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private EventLoopGroup group = new NioEventLoopGroup();

	public void connect(String host, int port) throws Exception {
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 10, 2, -12, 0));
							pipeline.addLast(new HeartBeatReqHandler(params));//ByteBuf
							pipeline.addLast(new PackDecoder(m_EventHandlers, physicalAddress));//RawPack
						}
					});
			b.remoteAddress(host, port);
			ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
			ChannelFuture future = b.connect();
//			logger.info("client connect to host:{}, port:{}", host, port);
			future.sync();
			if (future.isSuccess()) {
//				System.out.println("Connect to server. Succeed!  成功");
			}
			future.channel().closeFuture().sync();
			System.out.println(physicalAddress + " Reconnect to server. ");
		} finally {
			//shutdown
//			group.shutdownGracefully();
			
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(15);
						try {
							connect(host, port);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public void stop() {
		// Shut down the event loop to terminate all threads.
		group.shutdownGracefully();
	}

	@Override
	public void run() {
//		System.out.println(physicalAddress+": "+startup);
		if(startup) {
			try {
				connect(host, port);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

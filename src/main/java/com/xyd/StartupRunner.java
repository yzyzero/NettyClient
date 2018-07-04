package com.xyd;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.xyd.dao.TerminalDao;
import com.xyd.model.Terminal;
import com.xyd.transfer.ClientService;

//import cn.tass.yingjgb.YingJGBCALLDLL;
//import org.apache.commons.codec.binary.Base64;

@Component
@Order(1)
public class StartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);
    
	@Value("${application.socket.user:server}")
	private String username;

	@Value("${application.socket.pass:123456}")
	private String password;

	@Value("${application.socket.host:127.0.0.1}")
	private String host = "127.0.0.1";

	@Value("${application.socket.port:8788}")
	private int port = 8788;
	
	@Value("${application.socket.startup:true}")
	private boolean startup = true;
	
//	@Value("${application.socket.source}")
//	private String source;
//	
//	@Value("${application.socket.targets}")
//	private String targets;
//	
//	@Value("${application.socket.physicalAddress}")
//	private String physicalAddress;
	
    private int threadMaxCount = 1000;

    @Autowired
    private TerminalDao dao;
    
	@Override
	public void run(String... args) throws Exception {
		if(args.length > 0) {
			try {
				if(StringUtils.isNumber(args[0])) {
					threadMaxCount = Integer.parseInt(args[0]);
				}else {
					System.out.println("命令行参数: "+args[0]);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		logger.info("Startup Runner ......threadMaxCount="+threadMaxCount);
		
		List<Runnable> threadList = new ArrayList<Runnable>();
		List<Terminal> tList = dao.findAll();
		for(Terminal terminal: tList) {
			terminal.setHost(host);
			terminal.setPort(port);
			logger.info("id={}, source={}, targets={}, startup={}", terminal.getId(), terminal.getSource(), terminal.getTargets(), terminal.getStartup());
			ClientService thread = new ClientService(host, port, terminal.getId(), terminal.getSource(), terminal.getTargets(), terminal.getStartup());
			threadList.add(thread);
		}
		ExecutorService executorService = Executors.newFixedThreadPool(threadMaxCount);
		for(Runnable thread : threadList) {
			executorService.execute(thread);
		}
		logger.info("Runner End.          threadMaxCount="+threadMaxCount);
		executorService.shutdown();
	}
}

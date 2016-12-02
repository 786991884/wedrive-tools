package com.terminal.netty;

import com.terminal.cache.CacheManager;
import com.terminal.cache.StatisticalData;
import com.terminal.entity.TerminalStatus;
import com.terminal.func.DataInitialization;
import com.terminal.func.GenerateTerminalData;
import com.terminal.log.LogManager;
import com.terminal.mbean.IoAcceptorStat;
import com.terminal.util.ConfigUtils;
import com.terminal.util.OSinfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Lenovo
 * @date 2016-11-16
 * @modify
 * @copyright
 */
@Component
public class NettyClientServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientServer.class);

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Bootstrap bootstrap;

    @Autowired
    private IoAcceptorStat ioAcceptorStat;

    private GlobalTrafficShapingHandler trafficHandler;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DataInitialization dataInitialization;

    @Autowired
    private GenerateTerminalData generateTerminalData;

    @Autowired
    private NettyClientHandler nettyClientHandler;

    @Autowired
    private LogManager logManager;

    @Autowired
    private ConfigUtils configUtils;

    public static AtomicLong connfailNum = new AtomicLong(0);

    public static AtomicLong connsuccNum = new AtomicLong(0);

    public static AtomicLong reconnNum = new AtomicLong(0);

    private void connect() {
        try {

            logger.info("模拟终端开始启动...");
            dataInitialization.init();
            bootstrap = new Bootstrap();
            trafficHandler = new GlobalTrafficShapingHandler(Executors.newScheduledThreadPool(1), 1000);
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            //pipeline.addLast(new FixedLengthFrameDecoder(5));
                            //pipeline.addLast(new StringDecoder());
                            //sc.pipeline().addLast(new ClienHeartBeattHandler());
                            //pipeline.addLast(new ClientHandler());
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    final EventLoop eventLoop = ctx.channel().eventLoop();
                                    reconnNum.getAndIncrement();
                                    logger.info("listener:Reconnect");
                                    eventLoop.schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            doConnect();
                                        }
                                    }, 1L, TimeUnit.SECONDS);
                                    super.channelInactive(ctx);
                                }
                            });
                            //流量统计Handler
                            //GlobalTrafficShapingHandler提供的方法trafficCounter()可以用来获取TrafficCounter对象，
//                           // TrafficCounter提供了常用的一些方法：
                            pipeline.addLast(trafficHandler);
                            pipeline.addLast(new JT808Decoder());
                            pipeline.addLast("handler", nettyClientHandler);
                        }
                    });
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //EventLoopGroup是共享的，也就是说这些连接共用一个NIO线程组EventLoopGroup，
            // 当某个链路发生异常或者关闭时，只需要关闭并释放Channel本身即可，
            // 不能同时销毁Channel所使用的NioEventLoop和所在的线程组EventLoopGroup
            //workerGroup.shutdownGracefully();
        }
    }

    public void doConnect() {
        try {
            ChannelFuture future = null;
            for (int i = 0; i < cacheManager.getTerminals().length; i++) {
                long terminalId = cacheManager.getTerminals()[i];
                TerminalStatus status = cacheManager.getTerminalCache().get(terminalId);
                StatisticalData.connectTimes++;
                StatisticalData.connectTotalTimes++;
                logger.info("" + i);
                future = bootstrap.connect(configUtils.getServerTcpIP(), configUtils.getServerTcpPort()).addListener((ChannelFuture channelFuture) -> {
                    if (channelFuture.cause() != null) {
                        connfailNum.getAndIncrement();
                        logger.info("Failed to connect: " + channelFuture.cause());
                    }
                    if (!channelFuture.isSuccess()) {
                        reconnNum.getAndIncrement();
                        logger.info("listener:Reconnect");
                        workerGroup.schedule(() -> doConnect(), 1L, TimeUnit.SECONDS);
                    }
                    if (channelFuture.isSuccess()) {
                        Channel channel = channelFuture.channel();
                        status.setChannelIsConnecting(true);
                        status.setChannelActive(false);
                        logger.info(terminalId + ": 建立TCP链路成功, 链路标识为： " + channel.hashCode());
                        status.setChannel(channel);
                        status.setChannelActive(true);
                        status.setChannelIsConnecting(false);
                        connsuccNum.getAndIncrement();
                        logger.info("Connect to server successfully!");
                    }
                }).sync();
            }
            long current = new Date().getTime() / 1000;
            StatisticalData.startDate = current;
            StatisticalData.stageStartDate = current;
            generateTerminalData.init();
            //logManager.start();
            //startMBean();
            logger.info("终端链路建立完毕...");
            saveAuthCode();
            //channelList.add(future.channel());
            future.channel().closeFuture().sync();

            // Start the client.
            //ChannelFuture cf = b.connect("192.168.85.38", 30503).sync();
            // Wait until the connection is closed.
            //cf.channel().closeFuture().sync();

            logger.info("begin to start server end...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void start() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }, "nettyclient");
        t1.start();
    }

    public void saveAuthCode() {
        while (true) {
            try {
                Thread.sleep(10000);
                //String path = this.getClass().getResource("/").getPath() + "cache";
                //ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cache" + File.separator + "auth.dat"));
                String path = "cache" + File.separator + "auth.dat";
                if (OSinfo.isWindows()) {
                    path = configUtils.getAuthPath();
                } else if (OSinfo.isLinux()) {
                    logger.info("linux");
                } else if (OSinfo.isDigitalUnix()) {
                    logger.info("unix");
                }
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
                out.writeObject(cacheManager.getAuthCode());
                out.close();
                if (cacheManager.getAuthCode().size() >= configUtils.getTerminalNumber()) {
                    break;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * java MBean 进行流量统计
     */
    private void startMBean() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName acceptorName = new ObjectName(ioAcceptorStat.getClass().getPackage().getName() + ":type=IoAcceptorStat");
            mBeanServer.registerMBean(ioAcceptorStat, acceptorName);
        } catch (Exception e) {
            logger.error("java MBean error", e);
        }
    }

    public TrafficCounter getTrafficCounter() {
        return trafficHandler.trafficCounter();
    }
}
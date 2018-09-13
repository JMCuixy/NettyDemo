package org.netty.demo.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * Created by XiuYin.Cui on 2018/9/10.
 */
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                //引导该 NioDatagramChannel（无连接的）
                .channel(NioDatagramChannel.class)
                // 设置 SO_BROADCAST 套接字选项
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws InterruptedException, IOException {
        //绑定 Channel，UDP 协议的连接用 bind() 方法
        Channel channel = bootstrap.bind(0).sync().channel();
        long pointer = 0;
        //长轮询 监听是否有新的日志文件生成
        while (true) {
            long length = file.length();
            if (length < pointer) {
                // 如果有必要，将文件指针设置到该文件的最后一个字节
                pointer = length;
            } else {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                // 确保当前的文件指针，以确保没有任何的旧数据被发送
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    //对于每个日志条目，写入一个 LogEvent 到 Channel 中，最后加入一个换行符号
                    channel.writeAndFlush(new LogEvent(file.getAbsolutePath(), line + System.getProperty("line.separator")));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                // 休眠一秒，如果被中断，则退出循环，否则重新处理它
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                while (!Thread.interrupted()) {
                    break;
                }
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress socketAddress = new InetSocketAddress("255.255.255.255", 8888);
        File file = new File("E:\\2018-09-12.log");
        LogEventBroadcaster logEventBroadcaster = new LogEventBroadcaster(socketAddress, file);
        try {
            logEventBroadcaster.run();
        } finally {
            logEventBroadcaster.stop();
        }
    }
}

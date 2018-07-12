package org.netty.demo.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * Created by XiuYin.Cui on 2018/6/6.
 * Netty 的引导类为应用程序的网络层配置提供了容器，这涉及将一个进程绑定到某个指定端口，
 * 或者将一个进程连接到另一个运行在某个指定主机的指定端口上运行的线程
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        //1、创建EventLoopGroup以进行事件的处理，如接受新连接以及读/写数据
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final AttributeKey<String> uuid = AttributeKey.newInstance("UUID");
            //2、创建ServerBootstrap，引导和绑定服务器
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group, group)
                    //3、指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    //4、使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //5、添加一个 EchoServerHandler 到子 Channel的 ChannelPipeline
                    //当一个新的连接被接受时，一个新的子Channel将会被创建，而 ChannelInitializer 将会把一个你的EchoServerHandler 的实例添加到该 Channel 的 ChannelPipeline 中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //这个方法提供了一种或者多种 ChannelHandler 添加到一个 ChannelPipeline 中的简便方法。
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(serverHandler);
                        }
                    });
            bootstrap.attr(uuid,"clientUuid");
            //优化bootstarp配置
            //是否启用心跳保活机制。在双方TCP套接字建立连接后并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //超时属性
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
            //重用缓冲区
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            //自动调整下一次缓冲区建立时分配的空间大小，避免内存的浪费
            bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
            //当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度,默认值50。
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            //用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
            bootstrap.option(ChannelOption.TCP_NODELAY, true);


            //6、异步地绑定服务器，调用sync()方法阻塞等待直到绑定完成
            ChannelFuture channelFuture = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + "started and listening for connections on" + channelFuture.channel().localAddress());
            //7、获取 Channel 的 CloseFuture，并且阻塞当前线程直到它完成
            channelFuture.channel().closeFuture().sync();

        } finally {
            //8、关闭 EventLoopGroup 释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoServer(9999).start();
    }
}

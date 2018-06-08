package org.netty.demo.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(serverHandler);
                        }
                    });
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

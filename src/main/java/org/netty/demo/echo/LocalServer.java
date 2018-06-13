package org.netty.demo.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;

/**
 * Created by XiuYin.Cui on 2018/6/12.
 */
public class LocalServer {

    public static void main(String[] args) throws InterruptedException {
        new LocalServer().server();
    }

    public void server() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new DefaultEventLoop();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group, group)
                    .channel(LocalServerChannel.class)
                    .childHandler(new ChannelInitializer<LocalChannel>() {
                        @Override
                        protected void initChannel(LocalChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(serverHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(new LocalAddress("foo")).sync();
            System.out.println(EchoServer.class.getName() + "--started and listening for connections on--" + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }
}

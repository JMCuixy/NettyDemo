package org.netty.demo.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Created by XiuYin.Cui on 2018/6/12.
 */
public class LocalClient {

    public static void main(String[] args) throws InterruptedException {
       new LocalClient().client();
    }

    public void client() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(LocalChannel.class)
                    .handler(new ChannelInitializer<LocalChannel>() {
                        @Override
                        protected void initChannel(LocalChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(new LocalAddress("foo")).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}

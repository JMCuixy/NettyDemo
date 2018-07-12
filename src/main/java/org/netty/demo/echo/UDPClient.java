package org.netty.demo.echo;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by XiuYin.Cui on 2018/7/11.
 */
public class UDPClient {

    private final Logger logger = LoggerFactory.getLogger(UDPClient.class);

    private final int port;

    public UDPClient(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        // UDP协议使用OioEventLoopGroup
        bootstrap.group(new OioEventLoopGroup());
        // UDP协议使用OioDatagramChannel
        bootstrap.channel(OioDatagramChannel.class);
        bootstrap.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
            @Override
            protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {

            }
        });
        // UDP 协议是无连接的协议，只需要提供端口
        bootstrap.remoteAddress(new InetSocketAddress(port));

        ChannelFuture channelFuture = bootstrap.bind().sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()){
                    logger.info("Channel bound");
                } else {
                    logger.error("bind attempt failed");
                    future.cause().printStackTrace();
                }
            }
        });


    }
}

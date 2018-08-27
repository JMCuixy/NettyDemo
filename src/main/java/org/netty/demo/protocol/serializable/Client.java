package org.netty.demo.protocol.serializable;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class Client {


    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new MyChannelHandler());
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
            System.out.println("客户端优雅的释放了线程资源...");
        }

    }

    /**
     * 网络事件处理器
     */
    private static final class MyChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            System.out.println("MyChannelHandler");
            // 添加Jboss的序列化，编解码工具
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
            // 处理网络IO
            ch.pipeline().addLast(new ClientHandler());
        }
    }

    /**
     * 业务处理器
     */
    public static final class ClientHandler extends ChannelHandlerAdapter {

        // 客户端与服务端，连接成功的售后
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 发送消息
            Message request1 = new Message("666");
            ctx.writeAndFlush(request1).addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("成功发送到服务端消息");
                    } else {
                        System.out.println("失败服务端消息失败:" + future.cause().getMessage());
                        future.cause().printStackTrace();
                    }
                }
            });
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try {
                Message response = (Message) msg;
                System.out.println(response);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static void main(String[] args) throws Exception {
        new Client().connect(8765, "127.0.0.1");
    }
}

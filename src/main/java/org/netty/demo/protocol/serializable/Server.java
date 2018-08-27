package org.netty.demo.protocol.serializable;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by XiuYin.Cui on 2018/8/27.
 */
public class Server {


    public void start(int port) throws Exception {
        // 配置NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 服务器辅助启动类配置
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChildChannelHandler());
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * 网络事件处理器
     */
    private static final class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 添加Jboss的序列化，编解码工具
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
            // 处理网络IO
            ch.pipeline().addLast(new ServerHandler());
        }
    }

    /**
     * 自定义处理器
     */
    private static final class ServerHandler extends ChannelHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 用于获取客户端发来的数据信息
            Message body = (Message) msg;
            System.out.println("Server接受的客户端的信息 :" + body.toString());

            // 写数据给客户端
            Message response = new Message("欢迎您，与服务端连接成功");
            // 当服务端完成写操作后，关闭与客户端的连接
            ctx.writeAndFlush(response);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }


    public static void main(String[] args) throws Exception {
        new Server().start(8765);
    }
}

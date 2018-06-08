package org.netty.demo.echo;

import com.sun.org.apache.xpath.internal.operations.String;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


/**
 * Created by XiuYin.Cui on 2018/6/6.
 */
@ChannelHandler.Sharable //标记该类的实例可以被多个Channel共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 当从服务器接收到一条消息时被调用
     *
     * @param ctx
     * @param msg ByteBuf (Netty 的字节容器) 作为一个面向流的协议，TCP 保证了字节数组将会按照服务器发送它们的顺序接收
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Client" + ctx.channel().remoteAddress() + "connected");
        System.out.println(msg.toString(CharsetUtil.UTF_8));
    }

    /**
     * 在到服务器的连接已经建立之后将被调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rock!", CharsetUtil.UTF_8));
    }


    /**
     * 在处理过程中引发异常时被调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

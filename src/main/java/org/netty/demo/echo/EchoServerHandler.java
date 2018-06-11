package org.netty.demo.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.nio.file.FileSystemNotFoundException;


/**
 * Created by XiuYin.Cui on 2018/6/6.
 * 定义响应入站事件的方法 需要实现 ChannelInboundHandler接口
 * ChannelHandlerAdapter 提供了一些 channelInboundHandler 的默认实现
 */

@ChannelHandler.Sharable //标识一个Channel-Handler 可以被多个Channel安全的共享
public class EchoServerHandler extends ChannelHandlerAdapter {


    /**
     * 对于每个传入的消息都要调用
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received：" + in.toString(CharsetUtil.UTF_8));
        //将接收到的消息写给发送者，而不冲刷出站消息
        //ChannelHandlerContext 发送消息。导致消息向下一个ChannelHandler流动
        //Channel 发送消息将会导致消息从 ChannelPipeline的尾端开始流动
        ChannelFuture cf = ctx.write(in);
        //添加监听器以便在写完成后接收通知
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("Write Successful");
                } else {
                    System.err.println("Write error");
                    future.cause().printStackTrace();
                }
            }
        });
    }

    /**
     * 通知 ChannelHandlerAdapter 最后一次对channel-Read()的调用是当前批量读取中的最后一条消息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //暂存于ChannelOutboundBuffer中的消息，在下一次调用flush()或者writeAndFlush()方法时将会尝试写出到套接字
        //将这份暂存消息冲刷到远程节点，并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间，有异常抛出时会调用
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

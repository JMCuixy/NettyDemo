package org.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * Created by XiuYin.Cui on 2018/6/6.
 * <p>
 * 被回调触发的ConnectHandler
 */
public class ConnectHandler extends SimpleChannelInboundHandler {


    protected void messageReceived(ChannelHandlerContext ctx, Object o) throws Exception {
        System.out.println("Client" + ctx.channel().remoteAddress() + "connected");
    }

    public void channelFutureListener() {
        Channel channel = new Channel() {
            public ChannelId id() {
                return null;
            }

            public EventLoop eventLoop() {
                return null;
            }

            public Channel parent() {
                return null;
            }

            public ChannelConfig config() {
                return null;
            }

            public boolean isOpen() {
                return false;
            }

            public boolean isRegistered() {
                return false;
            }

            public boolean isActive() {
                return false;
            }

            public ChannelMetadata metadata() {
                return null;
            }

            public SocketAddress localAddress() {
                return null;
            }

            public SocketAddress remoteAddress() {
                return null;
            }

            public ChannelFuture closeFuture() {
                return null;
            }

            public boolean isWritable() {
                return false;
            }

            public Unsafe unsafe() {
                return null;
            }

            public ChannelPipeline pipeline() {
                return null;
            }

            public ByteBufAllocator alloc() {
                return null;
            }

            public ChannelPromise newPromise() {
                return null;
            }

            public ChannelProgressivePromise newProgressivePromise() {
                return null;
            }

            public ChannelFuture newSucceededFuture() {
                return null;
            }

            public ChannelFuture newFailedFuture(Throwable throwable) {
                return null;
            }

            public ChannelPromise voidPromise() {
                return null;
            }

            public ChannelFuture bind(SocketAddress socketAddress) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1) {
                return null;
            }

            public ChannelFuture disconnect() {
                return null;
            }

            public ChannelFuture close() {
                return null;
            }

            public ChannelFuture deregister() {
                return null;
            }

            public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture disconnect(ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture close(ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture deregister(ChannelPromise channelPromise) {
                return null;
            }

            public Channel read() {
                return null;
            }

            public ChannelFuture write(Object o) {
                return null;
            }

            public ChannelFuture write(Object o, ChannelPromise channelPromise) {
                return null;
            }

            public Channel flush() {
                return null;
            }

            public ChannelFuture writeAndFlush(Object o, ChannelPromise channelPromise) {
                return null;
            }

            public ChannelFuture writeAndFlush(Object o) {
                return null;
            }

            public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
                return null;
            }

            public <T> boolean hasAttr(AttributeKey<T> attributeKey) {
                return false;
            }

            public int compareTo(Channel o) {
                return 0;
            }
        };

        ChannelFuture connect = channel.connect(new InetSocketAddress("192.168.1.22", 25));
        //注册一个ChannelFutureListener，以便在操作完成时获得通知
        connect.addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                //检查操作的状态
                if (channelFuture.isSuccess()) {
                    //如果操作是成功的，则创建一个ByteBuf以持有数据
                    ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    //将数据异步发送到远程结点。返回一个ChannelFuture
                    ChannelFuture wf = channelFuture.channel().writeAndFlush(buffer);
                } else {
                    Throwable cause = channelFuture.cause();
                    cause.printStackTrace();
                }
            }
        });

    }
}

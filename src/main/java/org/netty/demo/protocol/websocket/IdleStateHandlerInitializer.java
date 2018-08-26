package org.netty.demo.protocol.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by XiuYin.Cui on 2018/8/26.
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                //当连接在60秒内没有接收到消息时，进会触发一个 IdleStateEvent 事件，被 HeartbeatHandler 的 userEventTriggered 方法处理
                new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS),
                new HeartbeatHandler()
        );

    }


    public static final class HeartbeatHandler extends ChannelHandlerAdapter {
        //发送到远程节点的心跳信息
        private static final ByteBuf HEARTBEAT_SEQUENCE =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                        "HEARTBEAT", CharsetUtil.UTF_8));

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                //发送心跳信息，并在发送失败时关闭该连接
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}

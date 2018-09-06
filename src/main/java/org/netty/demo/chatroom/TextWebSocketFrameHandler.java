package org.netty.demo.chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.util.Iterator;

/**
 * Created by XiuYin.Cui on 2018/9/5.
 * <p>
 * WebSocket 帧：WebSocket 以帧的方式传输数据，每一帧代表消息的一部分。一个完整的消息可能会包含许多帧
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //增加消息的引用计数（保留消息），并将他写到 ChannelGroup 中所有已经连接的客户端
        Iterator<Channel> iterator = group.iterator();
        while (iterator.hasNext()) {
            Channel channel = iterator.next();
            if (!(ctx.channel() == channel)) {
                channel.writeAndFlush(msg.retain());
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //是否握手成功，升级为 Websocket 协议
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            //握手成功，移除 HttpRequestHandler，因此将不会接收到任何消息
            ctx.pipeline().remove(HttpRequestHandler.class);
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            group.add(ctx.channel());
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            if (stateEvent.state() == IdleState.READER_IDLE) {
                ByteBuf byteBuf = ctx.alloc().directBuffer();
                byteBuf.writeBytes("已离线，系统将你剔出聊天室...".getBytes(CharsetUtil.UTF_8));
                group.remove(ctx.channel());
                ctx.channel().writeAndFlush(byteBuf);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}
